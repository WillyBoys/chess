package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    model.GameData data;
    HashMap<Integer, GameData> GameDB = new HashMap<>();

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
}
