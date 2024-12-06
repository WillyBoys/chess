package serverfacade;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.GameplayUI;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.Loading;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

//This is the main interaction between program and client while in a game
public class GamingInteraction {
    boolean inGame = true;
    private WebSocketFacade ws;
    String authToken;
    int gameID;

    public GamingInteraction(String authToken, int gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (inGame) {
            //Take in the input and evaluate it
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    //Evaluate the User Input
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redrawChessBoard();
                case "leave" -> leaveGame();
                case "move" -> makeMove(params);
                case "resign" -> resignGame();
                case "highlight" -> highlightMoves(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException | IOException ex) {
            return ex.getMessage();
        }
    }

    public String redrawChessBoard() {
        GameplayUI.main(ChessBoard);
        return "";
    }

    public String leaveGame() throws IOException {
        ws.leaveGame(authToken, gameID);
        return "";
    }

    public String makeMove(String... params) throws IOException {
        //Translate the Position to an actual ChessPosition

        ChessPosition startPosition = new ChessPosition(params[0]);
        ChessMove move = new ChessMove(startPosition, endPosition, null);
        ws.makeMove(authToken, gameID, move);
        return "";
    }

    public String resignGame() throws IOException {
        System.out.println("Are you sure you want to resign?");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        var tokens = line.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        if (cmd.equals("yes")) {
            ws.resignGame(authToken, gameID);
        }
        return "";
    }

    public String highlightMoves(String... params) {
        //Create the ChessPosition from the input
        //Highlight the available moves
        return "";
    }

    public String help() throws ResponseException {
        return """
                - Redraw the Chess Board: redraw
                - Make a Move: move <Start Position> <End Position>
                - Highlight the Legal Moves: highlight <Starting Position>
                - Leave the Game: leave
                - Resign the Game: resign
                """;
    }
}