package dataaccess;

public interface UserDAO {
    void clear();

    void createUser();

    model.UserData getUser(String username);
}
