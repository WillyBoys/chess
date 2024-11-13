
// THIS IS THE MAIN INTERACTIONS BETWEEN THE PROGRAM AND CLIENT
public class UserInteraction {
    private final ServerFacade server;
    private final String serverUrl;

    public UserInteraction(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    
}
