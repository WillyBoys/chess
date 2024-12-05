package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

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
        try {
            System.out.println(message);
            var action = new Gson().fromJson(message, UserGameCommand.class);
            switch (action.getCommandType()) {
                case CONNECT -> connectGame(action.getAuthToken(), action.getGameID(), session);
                case MAKE_MOVE -> makeMove(action.getAuthToken());
                case LEAVE -> leaveGame(action.getAuthToken());
                case RESIGN -> resignGame(action.getAuthToken());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void connectGame(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);
        GameData game = gameDAO.getGame(gameID);

        //Adds the connection via session
        connections.add(username, session);

        //Create a notification message for broadcasting
        var message = String.format("%s has joined the game", username);
        var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);

        //Create the "Load Game" message for the root client
        var loadGame = new Loading(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
        connections.self(username, loadGame);

    }

    //THIS ONE NEEDS SOME WORK. HOW TO INCORPORATE MAKEMOVE COMMAND
    private void makeMove(String authToken) throws IOException, DataAccessException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);
        var message = String.format("%s made a move", username);
        var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);
    }

    public void leaveGame(String authToken) throws IOException, DataAccessException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);

        //Creates the broadcast message
        var message = String.format("%s left the game", username);
        var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);

    }

    public void resignGame(String authToken) throws DataAccessException, IOException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);

        //Creates the broadcast message
        var message = String.format("%s resigned the game", username);
        var notification = new Notifying(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);
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
