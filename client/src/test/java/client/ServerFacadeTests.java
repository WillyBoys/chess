package client;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import serverFacade.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setup() {
        server.run(8080);
        final ServerFacade fakeServer;
        GameData gameData;
        AuthData authData;
        UserData userData;

    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
