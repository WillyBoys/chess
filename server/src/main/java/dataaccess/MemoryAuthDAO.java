package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    model.AuthData data;
    HashMap<String, AuthData> AuthDB = new HashMap<>();

    @Override
    public void clear() {
        AuthDB.clear();
    }

    @Override
    public void createAuth() {
        AuthDB.put(data.authToken(), data);
    }

    @Override
    public AuthData getAuth() {
        return AuthDB.get(data.authToken());
    }

    @Override
    public void deleteAuth() {
        AuthDB.remove(data.authToken());
    }
}

