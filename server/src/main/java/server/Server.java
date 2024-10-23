package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import javax.xml.crypto.Data;
import java.util.Map;

public class Server {
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    AuthService authServ = new AuthService(authDAO);
    UserService userServ = new UserService(userDAO, authDAO);
    GameService gameServ = new GameService(gameDAO, authDAO);

    public int run(int desiredPort) {

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerUser(Request req, Response res) {
        res.type("application/json");
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            var auth = userServ.register(user);
            res.status(200);
            return new Gson().toJson(auth);
        } catch (DataAccessException error) {
            return handleError(res, error);
        }
    }

    private Object loginUser(Request req, Response res) throws DataAccessException {
        res.type("application/json");
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);

            UserData storedUser = userDAO.getUser(user.username());
            if (storedUser == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }

            if (!storedUser.password().equals(user.password())) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }

            var auth = userServ.login(user);

            System.out.println("Generated auth token: " + auth.authToken());

            res.status(200);
            return new Gson().toJson(auth);
        } catch (DataAccessException error) {
            return handleError(res, error);
        }
    }

    private Object logoutUser(Request req, Response res) throws DataAccessException {
        res.type("application/json");

        // Get the Authorization token from the headers
        String auths = req.headers("Authorization");

        // Handle missing or empty Authorization header
        if (auths == null || auths.isEmpty()) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }

        // Create an AuthData object from the token
        AuthData auth = new AuthData(auths, null);

        // Check if the token exists in the AuthDB (indicating it's valid)
        AuthData retrievedAuth = authDAO.getAuth(auth);
        if (retrievedAuth == null) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }

        // Proceed with logout
        userServ.logout(retrievedAuth);
        res.status(200);  // Success: return 200 OK
        return new Gson().toJson(Map.of());
    }

    private Object listGames(Request req, Response res) {
        res.type("application/json");

        String auths = req.headers("Authorization");

        if (auths == null || auths.isEmpty()) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }

        AuthData auth = new AuthData(auths, null);

        try {
            // Validate token
            if (authDAO.getAuth(auth) == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }

            var list = gameServ.listGames(auth).toArray();
            res.status(200);
            return new Gson().toJson(Map.of("games", list));
        } catch (DataAccessException error) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: " + error.getMessage()));
        }
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        res.type("application/json");

        // Get the Authorization token from the headers
        String auths = req.headers("Authorization");

        if (auths == null || auths.isEmpty()) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Authorization header is missing"));
        }

        AuthData auth = new AuthData(auths, null);

        // Validate the token
        if (authDAO.getAuth(auth) == null) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Invalid or expired Authorization token"));
        }

        GameData info = new Gson().fromJson(req.body(), GameData.class);
        String gameName = info.gameName();

        String username = authServ.getUsername(auth);
        auth = new AuthData(auths, username);

        int gameId = gameServ.createGame(gameName, auth);
        res.status(200);
        return new Gson().toJson(Map.of("gameID", gameId));
    }

    private Object joinGame(Request req, Response res) {
        res.type("application/json");
        try {
            GameData info = new Gson().fromJson(req.body(), GameData.class);
            String gameName = info.gameName();
            AuthData auth = new Gson().fromJson(req.body(), AuthData.class);
            if (info.blackUsername() != null) {
                String user = info.blackUsername();
                String color = "black";
                gameServ.joinGame(gameName, user, color, auth);
            } else {
                String user = info.whiteUsername();
                String color = "white";
                gameServ.joinGame(gameName, user, color, auth);
            }
            res.status(200);
            return "";
        } catch (DataAccessException error) {
            return handleError(res, error);
        }
    }


    private Object clear(Request req, Response res) {
        res.type("application/json");
        authServ.clear();
        gameServ.clear();
        userServ.clear();
        res.status(200);
        return new Gson().toJson(Map.of());
    }

    private Object handleError(Response res, DataAccessException error) {
        String message = error.getMessage();
        if (message.equals("Error: unauthorized")) {
            res.status(401);
        } else if (message.equals("Error: bad request")) {
            res.status(400);
        } else if (message.equals("Error: already taken")) {
            res.status(403);
        } else {
            res.status(500);
        }
        return new Gson().toJson(Map.of("message", message));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
