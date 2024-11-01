package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    private AuthService authService;
    private GameService gameService;
    private UserService userService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        this.userDAO = new SqlUserDAO();
        this.authDAO = new SqlAuthDAO();
        this.gameDAO = new SqlGameDAO();
        authService = new AuthService(authDAO);
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }

    //SQL Auth Tests
    @Test
    public void authClear() throws DataAccessException {
        authDAO.createAuth("testUser");

        authDAO.clear();

        assertEquals(0, authDAO.getDBSize());
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        authDAO.createAuth("testUser");
        assertEquals(1, authDAO.getDBSize());
    }

    @Test
    public void createAuthFail() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }

    @Test
    public void getAuthSuccess() throws DataAccessException {
        AuthData stuff = authDAO.createAuth("testUser");
        AuthData retrieved = authDAO.getAuth(stuff);
        assertEquals(stuff, retrieved);
    }

    @Test
    public void getAuthFail() {
        assertThrows(DataAccessException.class, () -> authDAO.getAuth(null));
    }

    @Test
    public void getUsernameSuccess() throws DataAccessException {
        AuthData testAuth = authDAO.createAuth("testAuth");
        assertEquals(authDAO.getUsername(testAuth), "testAuth");
    }

    @Test
    public void getUsernameFail() {
        assertThrows(DataAccessException.class, () -> authDAO.getUsername(null));
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException {
        AuthData testAuth = authDAO.createAuth("testUser");

        authDAO.deleteAuth(testAuth);

        assertEquals(0, authDAO.getDBSize());
    }

    @Test
    public void deleteAuthFail() {
        assertThrows(DataAccessException.class, () -> authDAO.deleteAuth(null));
    }

    @Test
    public void getAuthDBSizeSuccess() throws DataAccessException {
        authDAO.createAuth("testUser1");
        authDAO.createAuth("testUser2");
        assertEquals(authDAO.getDBSize(), 2);
    }

    //SQL User Tests
    @Test
    public void userClear() throws DataAccessException {
        UserData testUser = new UserData("testUser", "testPassword", "user@email.com");
        userDAO.createUser(testUser);

        userDAO.clear();

        assertEquals(0, userDAO.getDBSize());
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        UserData testUser = new UserData("testUser", "testPassword", "user@email.com");
        userDAO.createUser(testUser);

        assertEquals(1, userDAO.getDBSize());
    }

    @Test
    public void createUserFail() {
        assertThrows(DataAccessException.class, () -> userDAO.createUser(null));
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        UserData testUser = new UserData("testUser", "testPassword", "user@email.com");
        userDAO.createUser(testUser);
        UserData received = userDAO.getUser("testUser");

        assertEquals(testUser, received);

    }

    @Test
    public void getUserFail() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userDAO.getUser(null));
    }

    @Test
    public void getUserDBSizeSuccess() throws DataAccessException {
        UserData testUser = new UserData("testUser", "testPassword", "user@email.com");
        UserData testUser2 = new UserData("testUser2", "testPassword2", "user2@email.com");
        userDAO.createUser(testUser);
        userDAO.createUser(testUser2);
        assertEquals(userDAO.getDBSize(), 2);
    }
}
