package server.websocket;

import chess.ChessMove;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.EOFException;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    SqlAuthDAO authDAO;
    SqlGameDAO gameDAO;

    public WebSocketHandler(SqlAuthDAO authDAO, SqlGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        System.out.println(message);
        var action = new Gson().fromJson(message, UserGameCommand.class);
        try {
            switch (action.getCommandType()) {
                case CONNECT -> connectGame(action.getAuthToken(), action.getGameID(), session);
                case MAKE_MOVE -> makeMove(message, action.getGameID(), session, action.getAuthToken());
                case LEAVE -> leaveGame(action.getAuthToken(), action.getGameID(), session);
                case RESIGN -> resignGame(action.getAuthToken(), action.getGameID(), session);
            }
        } catch (Exception ex) {
            errorHandler(ex.getMessage(), action.getGameID(), session);
        }

    }

    private void connectGame(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);
        if (username == null) {
            errorHandler("Error: Auth Token is not valid", gameID, session);
        } else {
            GameData game = gameDAO.getGame(gameID);

            //Adds the connection via session
            connections.add(gameID, session);

            //Create a notification message for broadcasting
            var message = String.format("%s has joined the game", username);
            var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(gameID, session, notification);

            //Create the "Load Game" message for the root client
            var loadGame = new Loading(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
            connections.self(gameID, session, loadGame);
        }
    }

    //THIS ONE NEEDS SOME WORK. HOW TO INCORPORATE MAKEMOVE COMMAND
    private void makeMove(String message, int gameID, Session session, String authToken) throws IOException, DataAccessException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);
        if (username == null) {
            throw new DataAccessException("Error: Auth Token is not valid");
        }
        var info = new Gson().fromJson(message, MakeMove.class);
        try {
            //Get the Game info and Make the Move
            GameData game = gameDAO.getGame(gameID);
            ChessMove move = info.getMove();
            game.game().makeMove(move);

            ChessPiece piece = game.game().getBoard().getPiece(move.getEndPosition());
            var currentColor = piece.getTeamColor();

            //Check Whether a Color is in Check
            if (game.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                var messageStuff = "Black is in Check";
                var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, messageStuff);
                connections.broadcast(gameID, session, notification);
                connections.self(gameID, session, notification);
            } else if (game.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                var messageStuff = "White is in Check";
                var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, messageStuff);
                connections.broadcast(gameID, session, notification);
                connections.self(gameID, session, notification);
            }

            //Check whether a color is in checkmate
            if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                var messageStuff = "Black is in Checkmate";
                var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, messageStuff);
                connections.broadcast(gameID, session, notification);
                connections.self(gameID, session, notification);
            } else if (game.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                var messageStuff = "White is in Checkmate";
                var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, messageStuff);
                connections.broadcast(gameID, session, notification);
                connections.self(gameID, session, notification);
            }

            //Check whether a color is in stalemate
            if (game.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
                var messageStuff = "Black is in Stalemate";
                var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, messageStuff);
                connections.broadcast(gameID, session, notification);
                connections.self(gameID, session, notification);
            } else if (game.game().isInStalemate(ChessGame.TeamColor.WHITE)) {
                var messageStuff = "White is in Stalemate";
                var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, messageStuff);
                connections.broadcast(gameID, session, notification);
                connections.self(gameID, session, notification);
            }

            //Notify the other players
            var messageStuff = String.format("%s has made a move", username);
            var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, messageStuff);
            connections.broadcast(gameID, session, notification);

            //Load the Game for the root client
            var loadGame = new Loading(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
            connections.self(gameID, session, loadGame);
            connections.broadcast(gameID, session, loadGame);
        } catch (Exception ex) {
            errorHandler(ex.getMessage(), gameID, session);
        }

    }

    public void leaveGame(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);

        //Creates the broadcast message
        var message = String.format("%s left the game", username);
        var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameID, session, notification);

        //Update the Game to Show Logged Out
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData.whiteUsername().equals(username)) {
            GameData gameData1 = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(gameData1);
        } else if (gameData.blackUsername().equals(username)) {
            GameData gameData1 = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            gameDAO.updateGame(gameData1);
        }


        //Remove the Connection
        connections.remove(session);

    }

    public void resignGame(String authToken, int gameID, Session session) throws DataAccessException, IOException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);

        //Creates the broadcast message
        var message = String.format("%s resigned the game", username);
        var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameID, session, notification);

        //Create the root client message


        //Remove the Connection
        connections.remove(session);
    }

    public void errorHandler(String message, int gameID, Session session) throws IOException {
        System.out.println(message);
        if (message.equals("Error: Game ID is not valid")) {
            connections.add(gameID, session);
            var errorMessage = String.format("Error: You have entered an incorrect ID");
            var errorNotification = new Erroring(ServerMessage.ServerMessageType.ERROR, errorMessage);
            connections.self(gameID, session, errorNotification);
            connections.remove(session);
        } else if (message.equals("Error: Auth Token is not valid")) {
            connections.add(gameID, session);
            var errorMessage = String.format("Error: Auth Token is not valid");
            var errorNotification = new Erroring(ServerMessage.ServerMessageType.ERROR, errorMessage);
            connections.self(gameID, session, errorNotification);
            connections.remove(session);
        } else if (message.equals("Invalid Move")) {
            connections.add(gameID, session);
            var errorMessage = String.format("Error: You have made an invalid move");
            var errorNotification = new Erroring(ServerMessage.ServerMessageType.ERROR, errorMessage);
            connections.self(gameID, session, errorNotification);
            connections.remove(session);
        }
    }
}
//
//package server.websocket;
//
//import com.google.gson.Gson;
//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.*;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import websocket.commands.UserGameCommand;
//import websocket.messages.ServerMessage;
//
//@WebSocket
//public class WebSocketHandler {
//    private static Map<Session, String> clients = new ConcurrentHashMap<>();
//
//    @OnWebSocketConnect
//    public void onConnect(Session session) throws Exception {
////        System.out.println("New client connected: " + session.getRemoteAddress().getAddress());
////        clients.put(session, session.getRemoteAddress().getAddress().toString());
//    }
//
//    @OnWebSocketClose
//    public void onClose(Session session, int statusCode, String reason) {
////        System.out.println("Client disconnected: " + clients.get(session));
////        clients.remove(session);
//    }
//
//    @OnWebSocketMessage
//    public void onMessage(Session session, String message) throws IOException {
//        System.out.println("Message from " + clients.get(session) + ": " + message);
//        var cmd = new Gson().fromJson(message, UserGameCommand.class);
//        switch (cmd.getCommandType()) {
//            case CONNECT:
//                System.out.println("CONNECT");
//                break;
//            case MAKE_MOVE:
//                System.out.println("MAKE MOVE");
//                break;
//            case LEAVE:
//                System.out.println("LEAVE");
//                break;
//            case RESIGN:
//                System.out.println("RESIGN");
//                break;
//        }
//    }
//
//    @OnWebSocketError
//    public void onError(Session session, Throwable error) {
//        System.out.println("Error for client " + clients.get(session) + ": " + error.getMessage());
//    }
//}
