package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();

    AuthData createAuth(String username);

    AuthData getAuth(AuthData auth);

    String getUsername(AuthData auth);

    void deleteAuth(AuthData auth);
}
