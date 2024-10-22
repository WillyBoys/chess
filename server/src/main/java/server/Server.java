package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
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
            String message = error.getMessage();
            if (message.equals("Error: already taken")) {
                res.status(403);
                return new Gson().toJson(error);
            }
        }
        return "404";
    }

    private Object loginUser(Request req, Response res) throws DataAccessException {
        res.type("application/json");
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = userServ.login(user);
        res.status(200);
        return new Gson().toJson(auth);
    }

    private Object logoutUser(Request req, Response res) throws DataAccessException {
        res.type("application/json");
        var auth = new Gson().fromJson(req.body(), AuthData.class);
        userServ.logout(auth);
        res.status(200);
        return "";
    }

    private Object listGames(Request req, Response res) {
        res.type("application/json");
        var auth = new Gson().fromJson(req.body(), AuthData.class);
        var list = gameServ.listGames(auth).toArray();
        return new Gson().toJson(Map.of("game:", list));
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        res.type("application/json");
        GameData info = new Gson().fromJson(req.body(), GameData.class);
        String name = info.gameName();
        var auth = new Gson().fromJson(req.body(), AuthData.class);
        var id = gameServ.createGame(name, auth);
        return new Gson().toJson(id);
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
        } catch (DataAccessException error) {
            if (error.equals("PUT MESSAGE HERE")) {
                //DO STUFF
            }
        }
        return "";
    }


    private Object clear(Request req, Response res) {
        res.type("application/json");

        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
