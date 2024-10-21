package dataaccess;


import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;


interface UserDAO {
    void clear();

    void createUser();

    model.UserData getUser(String username);
}

interface GameDAO {
    void clear();

    void createGame();

    GameData getGame();

    ArrayList<GameData> listGame();

    void updateGame();
}

interface AuthDAO {
    void clear();

    void createAuth();

    AuthData getAuth();

    void deleteAuth();
}


class MemoryUserDAO implements UserDAO {
    HashMap<String, model.UserData> UserDB = new HashMap<>();
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

class MemoryGameDAO implements GameDAO {
    model.GameData data;
    HashMap<Integer, model.GameData> GameDB = new HashMap<>();

    @Override
    public void clear() {
        GameDB.clear();
    }

    @Override
    public void createGame() {
        GameDB.put(data.gameID(), data);
    }

    @Override
    public GameData getGame() {
        return GameDB.get(data.gameID());
    }

    @Override
    public ArrayList<GameData> listGame() {
        ArrayList<GameData> listGames = new ArrayList<>();
        for (int i = 0; i < GameDB.size(); i++) {
            listGames.add(GameDB.get(data.gameID()));
        }
        return listGames;
    }

    @Override
    public void updateGame() {
        GameDB.put(data.gameID(), data);
    }

    class MemoryAuthDAO implements AuthDAO {
        model.AuthData data;
        HashMap<String, model.AuthData> AuthDB = new HashMap<>();

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
}

public class DataAccess {


}
