package serverFacade;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

// THIS IS THE MAIN INTERACTIONS BETWEEN THE PROGRAM AND CLIENT
public class UserInteraction {
    private final ServerFacade server;
    private final String serverUrl;
    private boolean loggedIn = false;
    private GameData gameData;
    private AuthData authData;
    private UserData userData;

    public UserInteraction(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> registerUser(params);
                case "login" -> loginUser(params);
                case "logout" -> logoutUser();
                case "list" -> listGames();
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "clear" -> clear();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length >= 3) {
            userData = new UserData(params[0], params[1], params[2]);
            authData = server.registerUser(userData);
            loggedIn = true;
            return String.format("You have registered as %s.\n", userData.username());
        }
        throw new ResponseException(400, "Expected: <Username> <Password> <Email>\n");
    }

    public String loginUser(String... params) throws ResponseException {
        if (params.length >= 2) {
            userData = new UserData(params[0], params[1], null);
            authData = server.loginUser(userData);
            loggedIn = true;
            return String.format("You signed in as %s.\n", userData.username());
        }
        throw new ResponseException(400, "Expected: <Username> <Password>\n");
    }

    public String logoutUser() throws ResponseException {
        assertTrue(loggedIn);
        server.logoutUser(authData);
        loggedIn = false;
        return String.format("Logged out %s. We hate to see you go.\n", userData.username());
    }

    public String listGames() throws ResponseException {
        assertTrue(loggedIn);
        var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();

    }

    public String createGame(String... params) throws ResponseException {
        assertTrue(loggedIn);
        if (params.length >= 1) {
            String gameName = params[0];
            server.createGame(gameName);
        }
        throw new ResponseException(400, "Expected: <GameName>\n");
    }

    public String joinGame(String... params) throws ResponseException {
        assertTrue(loggedIn);
        if (params.length >= 2) {
            int gameID = Integer.parseInt(params[0]);
            var join = new JoinGameRequest(gameID, userData.username(), params[1]);
            server.joinGame(join);
        }
        throw new ResponseException(400, "Expected: <GameID> <WHITE or BLACK>\n");
    }

    public String clear() throws ResponseException {
        assertTrue(loggedIn);
        server.clear();
        loggedIn = false;
        return String.format("Databases Cleared by %s\n", userData.username());
    }

    public String help() throws ResponseException {
        if (!loggedIn) {
            //Put the first options here
            return """
                    - Register New User: register <Username> <Password> <email>
                    - Log Into Previous Account: login <Username> <Password>
                    - Terminate the Program: quit
                    """;
        }
        return """
                - Logout: logout
                - List all Games: list
                - Create New Game: create <GameName>
                - Join a Game: join <GameID> <WHITE or BLACK>
                - Terminate the Program: quit
                """;
    }
}
