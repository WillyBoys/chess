package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Collection;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;
    int id = 0;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listGames(AuthData auth) throws DataAccessException {
        var info = authDAO.getAuth(auth);
        if (info == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDAO.listGame();
    }

    public int createGame(String gameName, AuthData auth) throws DataAccessException {
        id++;

        ChessGame board = new ChessGame();
        GameData gameData = new GameData(id, null, null, gameName, board);

        gameDAO.createGame(gameData);

        return id;
    }

    public void joinGame(String name, String playerName, String color, AuthData auth) throws DataAccessException {
        var authenticatedUser = authDAO.getAuth(auth);
        if (authenticatedUser == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        GameData data = gameDAO.getGame(name);
        if (data == null) {
            throw new DataAccessException("Error: bad request");
        }

        if (color.equals("black")) {
            if (data.blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            GameData game = new GameData(data.gameID(), data.whiteUsername(), playerName, data.gameName(), data.game());
            gameDAO.updateGame(game);
        } else {
            if (data.whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            GameData game = new GameData(data.gameID(), playerName, data.blackUsername(), data.gameName(), data.game());
            gameDAO.updateGame(game);
        }
    }

    public void clear() {
        gameDAO.clear();
    }
}