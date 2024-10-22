package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        UserData data = userDAO.getUser(user.username());
        if (data != null) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(user);
        AuthData auth = authDAO.createAuth(user.username());
        return authDAO.getAuth(auth);
    }

    public AuthData login(UserData user) throws DataAccessException {
        var info = userDAO.getUser(user.username());
        if (info == null) {
            throw new DataAccessException("Login Error: The username or password is incorrect");
        }
        AuthData auth = authDAO.createAuth(user.username());
        return authDAO.getAuth(auth);
    }

    public void logout(AuthData auth) throws DataAccessException {
        var info = authDAO.getAuth(auth);
        if (info == null) {
            throw new DataAccessException("Logout Error: Unable to log out");
        }
        authDAO.deleteAuth(auth);
    }

    public void clear() {
        userDAO.clear();
    }
}
