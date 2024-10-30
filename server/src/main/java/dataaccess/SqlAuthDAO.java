package dataaccess;

import model.AuthData;

import static dataaccess.DatabaseManager.executeUpdate;

public class SqlAuthDAO implements AuthDAO {

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE chess";
        executeUpdate(statement);
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
