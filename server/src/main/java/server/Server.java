package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    AuthService authServ = new AuthService(authDAO);
    UserService userServ = new UserService(userDAO, authDAO);

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
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = userServ.register(user);
        res.status(200);
        return new Gson().toJson(auth);
    }

    private Object loginUser(Request req, Response res) {
        res.type("application/json");
    }

    private Object logoutUser(Request req, Response res) {

    }

    private Object listGames(Request req, Response res) {
        res.type("application/json");

    }

    private Object createGame(Request req, Response res) {

    }

    private Object joinGame(Request req, Response res) {

    }

    private Object clear(Request req, Response res) {

        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
