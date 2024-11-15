package serverFacade;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import ui.GameplayUI;

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
                case "observe" -> observeGame(params);
                case "clear" -> clear();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {
        try {
            if (authData == null) {
                if (params.length >= 3) {
                    userData = new UserData(params[0], params[1], params[2]);
                    authData = server.registerUser(userData);
                    loggedIn = true;
                    return String.format("You have registered as %s.\n", userData.username());
                }
            } else {
                return "You are logged in already. Logout to register a new user\n";
            }
        } catch (ResponseException e) {
            throw new ResponseException(400, "Expected: <Username> <Password> <Email>\n");
        }
        return "There was an error trying to register.\n";
    }

    public String loginUser(String... params) throws ResponseException {
        try {
            if (authData == null) {
                if (params.length >= 2) {
                    userData = new UserData(params[0], params[1], null);
                    authData = server.loginUser(userData);
                    loggedIn = true;
                    return String.format("You signed in as %s.\n", userData.username());
                }
            } else {
                return "You are already logged in. Logout to login as someone else.\n";
            }
        } catch (ResponseException e) {
            throw new ResponseException(400, "That didn't work. Expected: <Username> <Password>\n");
        }
        return "It is possible that you didn't log in correctly.\n";
    }

    public String logoutUser() throws ResponseException {
        try {
            if (authData == null) {
                return "You are not logged in.\n";
            } else {
                server.logoutUser(authData);
                loggedIn = false;
                return String.format("Logged out %s. We hate to see you go.\n", userData.username());
            }
        } catch (ResponseException e) {
            return "There was an error logging you out.\n";
        }
    }

    public String listGames() throws ResponseException {
        if (authData == null) {
            return "You are not logged in.\n";
        } else {
            int gameCount = 0;
            GameList games = server.listGames(authData);
            var result = new StringBuilder();
            var gson = new Gson();
            for (var game : games.games()) {
                gameCount++;
                result.append(gameCount);
                result.append(". ");
                result.append(gson.toJson(game.gameID())).append('|');
                result.append(gson.toJson(game.gameName())).append('|');
                result.append(gson.toJson(game.whiteUsername())).append('|');
                result.append(gson.toJson(game.blackUsername())).append('\n');
            }
            return result.toString();
        }

    }

    public String createGame(String... params) throws ResponseException {
        if (authData == null) {
            return "You are not logged in.\n";
        } else {
            try {
                if (params.length >= 1) {
                    String name = params[0];
                    GameData gameName = new GameData(0, null, null, name, null);
                    server.createGame(gameName, authData);
                }
            } catch (ResponseException e) {
                throw new ResponseException(400, "Expected: <GameName>\n");
            }
            return ("The game was created. You can now join it.\n");
        }
    }

    public String joinGame(String... params) throws ResponseException {
        if (authData == null) {
            return "You are not logged in.\n";
        } else if (params.length < 2) {
            return "There was not enough information to join the game.\n";
        } else {
            try {
                if (params.length >= 2) {
                    int gameID = Integer.parseInt(params[0]);
                    var join = new JoinGameRequest(gameID, userData.username(), params[1]);
                    server.joinGame(join, authData);
                }
            } catch (ResponseException e) {
                throw new ResponseException(400, "There was an error. Expected: <GameID> <WHITE or BLACK>\n");
            }
            GameplayUI.main(params);
            return "Enjoy your game and good luck!\n";
        }
    }

    public String observeGame(String... params) {
        if (authData == null) {
            return "You are not logged in.\n";
        } else {
            if (params.length > 0) {
                GameplayUI.main(params);
                return "Enjoy the game!";
            }
        }
        return null;
    }

    public String clear() throws ResponseException {
        if (authData == null) {
            return "You are not logged in.\n";
        } else {
            server.clear();
            loggedIn = false;
            return String.format("Databases Cleared by %s\n", userData.username());
        }
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
                - Observe a Game: observe <GameID>
                - Terminate the Program: quit
                """;
    }
}
