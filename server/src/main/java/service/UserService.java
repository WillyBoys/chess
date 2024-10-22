package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) {
        userDAO.createUser(user);
        authDAO.createAuth();
        return authDAO.getAuth();
    }

    public AuthData login(UserData user) {
        userDAO.getUser(user.username());
        authDAO.createAuth();
        return authDAO.getAuth();
    }

    public void logout(AuthData auth) {
        authDAO.getAuth();
        authDAO.deleteAuth();
    }
}
