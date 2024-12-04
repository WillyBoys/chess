package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    SqlAuthDAO authDAO;

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connectGame(action.getAuthToken(), session);
            case MAKE_MOVE -> makeMove(action.getAuthToken());
            case LEAVE -> leaveGame(action.getAuthToken());
            case RESIGN -> resignGame(action.getAuthToken());
        }
    }

    private void connectGame(String authToken, Session session) throws IOException, DataAccessException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);
        connections.add(auth.username(), session);
        var message = String.format("%s has joined the game", auth.username());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, message);
        connections.broadcast(username, notification);
    }

    private void makeMove(String authToken) throws IOException, DataAccessException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);
        var message = String.format("%s made a move", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);
    }

    public void leaveGame(String authToken) throws IOException {
        try {
            AuthData auth = new AuthData(authToken, null);
            String username = authDAO.getUsername(auth);
            var message = String.format("%s left the game", username);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new IOException();
        }
    }

    public void resignGame(String authToken) throws DataAccessException, IOException {
        AuthData auth = new AuthData(authToken, null);
        String username = authDAO.getUsername(auth);
        var message = String.format("%s resigned the game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast("", notification);
    }
}
