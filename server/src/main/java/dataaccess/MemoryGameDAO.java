package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Object, GameData> GameDB = new HashMap<>();

    @Override
    public void clear() {
        GameDB.clear();
    }

    @Override
    public int createGame(GameData game) {
        GameDB.put(game.gameID(), game);
        return game.gameID();
    }

    @Override
    public GameData getGame(String gameID) {
        return GameDB.get(gameID);
    }

    @Override
    public ArrayList<GameData> listGame() {
        return new ArrayList<>(GameDB.values());
    }

    @Override
    public void updateGame(GameData data) {
        GameDB.put(data.gameID(), data);
    }
}
