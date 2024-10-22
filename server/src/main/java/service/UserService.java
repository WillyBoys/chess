package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) {
        MemoryUserDAO.createUser(user);
        AuthDAO.createAuth();
        AuthDAO.getAuth();
    }

    public AuthData login(UserData user) {
        UserDAO.getUser(user);
        AuthDAO.createAuth();
        AuthDAO.getAuth();
    }

    public void logout(AuthData auth) {
        AuthDAO.getAuth();
        AuthDAO.deleteAuth();
    }
}
