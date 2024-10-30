package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;

    AuthData createAuth(String username);

    AuthData getAuth(AuthData auth);

    String getUsername(AuthData auth);

    void deleteAuth(AuthData auth);

    int getDBSize();
}
