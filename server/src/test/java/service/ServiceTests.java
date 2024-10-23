package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passoff.model.TestCreateRequest;
import service.*;
import dataaccess.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    private AuthService authService;
    private GameService gameService;
    private UserService userService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    private String existingAuth;

    @BeforeEach
    public void Setup() {
        this.userDAO = new MemoryUserDAO();
        this.authDAO = new MemoryAuthDAO();
        this.gameDAO = new MemoryGameDAO();
        authService = new AuthService(authDAO);
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);

    }

    @Test
    public void testClearDataSuccess() throws DataAccessException {

        AuthData testAuth = new AuthData("1234", "testUser");

        gameService.createGame("SomeGame", testAuth);
        gameService.createGame("AnotherGame", testAuth);

        UserData user = new UserData("testUser", "password", "email@test.com");
        userService.register(user);

        gameService.clear();
        authService.clear();
        userService.clear();
        assertTrue(authDAO.getDBSize() == 0);
        assertTrue(gameService.listGames(testAuth).isEmpty());
        assertTrue(userDAO.getDBSize() == 0);

    }

    @Test
    public void registerUserSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData authData = userService.register(user);
        assertNotNull(authData);
        assertEquals("testUser", authData.username());
    }
}
