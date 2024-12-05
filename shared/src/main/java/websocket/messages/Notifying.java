package websocket.messages;

public class Notifying extends ServerMessage {
    private final String message;

    public Notifying(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
}
