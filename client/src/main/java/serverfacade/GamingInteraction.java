package serverfacade;

import exception.ResponseException;
import websocket.NotificationHandler;

import java.util.Arrays;

//This is the main interaction between program and client while in a game
public class GamingInteraction {

    public GamingInteraction(String serverUrl, NotificationHandler notificationHandler) {

    }

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
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String redrawChessBoard() {
        //THIS WILL REDRAW THE CHESS BOARD IN THE CURRENT STATE
    }

    public String leaveGame() {
        //THIS WILL TAKE THE USER OUT OF THE GAME
    }

    public String makeMove(String... params) {
        //Take in the START and END position for this part
        //Make the Move and update the Chess Board
    }

    public String resignGame() {
        //First ask them to confirm that they want to resign
        //If YES, resign the game and the opponent wins
        //If NO, return to the gameplay state
    }

    public String highlightMoves(String... params) {
        //Create the ChessPosition from the input
        //Highlight the available moves
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