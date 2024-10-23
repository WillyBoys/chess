package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class AuthService {
    AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void clear() {
        authDAO.clear();
    }

    public String getUsername(AuthData auth) {
        String str = authDAO.getUsername(auth);
        return str;
    }


}
