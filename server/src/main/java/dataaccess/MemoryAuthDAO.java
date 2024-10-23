package dataaccess;

import model.AuthData;

import java.util.UUID;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }


    HashMap<String, AuthData> AuthDB = new HashMap<>();


    @Override
    public AuthData createAuth(String username) {
        String token = generateToken();
        AuthData data = new AuthData(token, username);
        AuthDB.put(token, data);
        return data;
    }

    @Override
    public AuthData getAuth(AuthData auth) {
        if (auth == null || auth.authToken() == null) {
            return null;
        }
        return AuthDB.get(auth.authToken());
    }

    @Override
    public String getUsername(AuthData auth) {
        AuthData retrievedAuth = AuthDB.get(auth.authToken());
        return retrievedAuth != null ? retrievedAuth.username() : null;


    }

    @Override
    public void deleteAuth(AuthData auth) {
        if (AuthDB.containsKey(auth.authToken())) {
            AuthDB.remove(auth.authToken());
        }
    }

    @Override
    public void clear() {
        AuthDB.clear();
    }
}

