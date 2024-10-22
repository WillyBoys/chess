package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> UserDB = new HashMap<>();
    model.UserData data;

    @Override
    public void clear() {
        UserDB.clear();
    }

    @Override
    public void createUser() {
        UserDB.put(data.username(), data);
    }

    @Override
    public model.UserData getUser(String username) {
        return UserDB.get(username);
    }
}
