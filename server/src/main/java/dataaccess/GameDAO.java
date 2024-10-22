package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();

    void createGame();

    GameData getGame();

    ArrayList<GameData> listGame();

    void updateGame();
}
