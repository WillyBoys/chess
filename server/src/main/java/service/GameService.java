package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;
    int id = 0;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listGames(AuthData auth) {
        return gameDAO.listGame();
    }

    public int createGame(String game, AuthData auth) throws DataAccessException {
        id++;
        ChessGame board = new ChessGame();
        GameData data = new GameData(id, null, null, game, board);
        var info = authDAO.getAuth(auth);
        if (info == null) {
            throw new DataAccessException("Error: Unable to Create Game. You are not logged in.");
        }
        gameDAO.createGame(data);
        return id;
    }

    public void joinGame(String name, String playerColor, AuthData auth) {
        
    }
}
