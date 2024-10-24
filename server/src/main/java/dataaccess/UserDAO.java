package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear();

    void createUser(UserData data);

    model.UserData getUser(String username);

    int getDBSize();
}
