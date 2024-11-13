import chess.ChessGame;
import exception.ResponseException;

import java.util.Arrays;
import java.util.Collection;

// THIS IS THE MAIN INTERACTIONS BETWEEN THE PROGRAM AND CLIENT
public class UserInteraction {
    private final ServerFacade server;
    private final String serverUrl;

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
                case "logout" -> "logout";
                case "list" -> listGames();
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "clear" -> clear();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {

    }

    public String loginUser(String... params) throws ResponseException {

    }

    public String logoutUser() throws ResponseException {

    }

    public String listGames() throws ResponseException {

    }

    public String createGame(String... params) throws ResponseException {

    }

    public String joinGame(String... params) throws ResponseException {

    }

    public String clear() throws ResponseException {

    }

    public String help() throws ResponseException {

    }
}
