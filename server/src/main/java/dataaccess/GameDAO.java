package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();

    int createGame(GameData game);

    GameData getGame();

    ArrayList<GameData> listGame();

    void updateGame();
}
