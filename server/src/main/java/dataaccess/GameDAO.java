package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;

    int createGame(GameData game) throws DataAccessException;

    GameData getGame(int gameID);

    ArrayList<GameData> listGame();

    void updateGame(GameData data);
}
