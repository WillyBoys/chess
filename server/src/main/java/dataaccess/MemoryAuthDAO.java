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
    public void clear() {
        AuthDB.clear();
    }

    @Override
    public AuthData createAuth(String username) {
        String token = generateToken();
        AuthData data = new AuthData(token, username);
        AuthDB.put(token, data);
        return data;
    }

    @Override
    public AuthData getAuth(AuthData auth) {
        return AuthDB.get(auth.authToken());
    }

    @Override
    public void deleteAuth(AuthData auth) {
        AuthDB.remove(auth.authToken());
    }
}

