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

import javax.xml.crypto.Data;
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

    @Test
    public void registerUserFail() throws DataAccessException {
        UserData user = new UserData("testUser", null, "email@test.com");
        assertThrows(DataAccessException.class, () -> userService.register(user));
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        UserData user = new UserData("existingUser", "password", "email@test.com");
        userDAO.createUser(user);
        AuthData authData = userService.login(user);
        assertNotNull(authData);
    }

    @Test
    public void loginFail() throws DataAccessException {
        UserData user = new UserData("existingUser", "wrongPassword", "email@test.com");
        userDAO.createUser(new UserData("existingUser", "password", "email@test.com"));
        assertThrows(DataAccessException.class, () -> userService.login(user));
    }

    //NEEDS MORE WORK
    @Test
    public void logoutSuccess() throws DataAccessException {
        UserData user = new UserData("existingUser", "password", "email@test.com");
        AuthData auth = userService.register(user);
        userService.logout(auth);
        assertNotNull(authDAO.getAuth(auth));
    }

    @Test
    public void logoutFail() throws DataAccessException {
        UserData user = new UserData("existingUser", "password", "email@test.com");
        AuthData auth = userService.register(user);
        AuthData auths = new AuthData("2134", "testStuff");
        assertThrows(DataAccessException.class, () -> userService.logout(auths));
    }
}
