package websocket.messages;

public class Erroring extends ServerMessage {
    private final String message;

    public Erroring(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
}
