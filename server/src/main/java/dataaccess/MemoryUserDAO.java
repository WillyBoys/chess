package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, UserData> UserDB = new HashMap<>();

    @Override
    public void clear() {
        UserDB.clear();
    }

    @Override
    public void createUser(UserData user) {
        UserDB.put(user.username(), user);
    }

    @Override
    public model.UserData getUser(String username) {
        return UserDB.get(username);
    }
}
