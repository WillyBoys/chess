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
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        UserData data = userDAO.getUser(user.username());
        if (data != null) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(user);
        AuthData auth = authDAO.createAuth(user.username());
        if (auth == null) {
            throw new DataAccessException(("Error: bad request"));
        }
        return authDAO.getAuth(auth);
    }

    public AuthData login(UserData user) throws DataAccessException {
        var info = userDAO.getUser(user.username());
        if (info == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        String password = info.password();
        if (password.equals(user.password())) {
            AuthData auth = authDAO.createAuth(user.username());
            return authDAO.getAuth(auth);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void logout(AuthData auth) throws DataAccessException {
        var info = authDAO.getAuth(auth);
        if (info == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDAO.deleteAuth(auth);
    }

    public void clear() {
        userDAO.clear();
    }
}
