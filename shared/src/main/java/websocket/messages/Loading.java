package websocket.messages;

import chess.ChessGame;

public class Loading extends ServerMessage {
    public ChessGame game;

    public Loading(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
