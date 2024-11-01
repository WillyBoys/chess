package dataaccess;

import model.AuthData;
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
    public void getDBSizeSuccess() throws DataAccessException {
        authDAO.createAuth("testUser1");
        authDAO.createAuth("testUser2");
        assertEquals(authDAO.getDBSize(), 2);
    }

    //SQL User Tests
    
}
