package service;

import dataaccess.AuthDAO;

public class AuthService {
    AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void clear() {
        authDAO.clear();
    }


}
