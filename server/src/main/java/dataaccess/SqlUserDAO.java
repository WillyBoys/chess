package dataaccess;

import model.AuthData;

public class SqlUserDAO implements AuthDAO {

    @Override
    public void clear() {

    }

    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    @Override
    public AuthData getAuth(AuthData auth) {
        return null;
    }

    @Override
    public String getUsername(AuthData auth) {
        return "";
    }

    @Override
    public void deleteAuth(AuthData auth) {

    }

    @Override
    public int getDBSize() {
        return 0;
    }
}
