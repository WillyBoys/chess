package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        assertTrue(authDAO.getDBSize() == 0);
    }

    @Test
    public void createAuthSuccess() throws DataAccessException {
        authDAO.createAuth("testUser");
        assertTrue(authDAO.getDBSize() == 1);
    }

    @Test
    public void createAuthFail() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }
}
