package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import serverfacade.GamingInteraction;
import ui.GameplayUI;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    try {
                        switch (serverMessage.getServerMessageType()) {
                            case NOTIFICATION:
                                handleNotification(message);
                                break;
                            case ERROR:
                                handleError(message);
                                break;
                            case LOAD_GAME:
                                handleLoadGame(message);
                                break;
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException("I am still getting things together");
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void handleNotification(String message) throws ResponseException {
        try {
            var action = new Gson().fromJson(message, Notifying.class);
            System.out.println(action.getMessage());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    public void handleError(String serverMessage) throws ResponseException {
        try {
            var action = new Gson().fromJson(serverMessage, Erroring.class);
            System.out.println(action.getErrorMessage());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void handleLoadGame(String serverMessage) throws ResponseException {
        try {
            var action = new Gson().fromJson(serverMessage, Loading.class);
            System.out.println(action.getColor());
            GameData gameData = new GameData(0, null, null, null, action.game, false);
            GameplayUI.displayGame(gameData, action.getColor());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void connectGame(String authToken, int gameID) throws IOException {
        var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        var action = new MakeMove(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
    }

    public void leaveGame(String authToken, int gameID) throws IOException {
        var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
        this.session.close();
    }

    public void resignGame(String authToken, int gameID) throws IOException {
        var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
        this.session.close();
    }
}
