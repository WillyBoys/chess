package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import service.GameService;

import java.sql.SQLException;
import java.util.ArrayList;

public class SqlGameDAO implements GameDAO {

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("TRUNCATE TABLE Game");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("INSERT INTO Game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, game.gameID());
            statement.setString(2, game.whiteUsername());
            statement.setString(3, game.blackUsername());
            statement.setString(4, game.gameName());
            var jState = new Gson().toJson(game.game());
            statement.setString(5, jState);
            statement.executeUpdate();
            return game.gameID();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM Game WHERE gameID=?";

            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame from = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, from);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGame() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM Game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        GameData id = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                        games.add(id);
                    }
                    return games;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("UPDATE Game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID = ?");
            statement.setString(1, data.whiteUsername());
            statement.setString(2, data.blackUsername());
            statement.setString(3, data.gameName());
            var jState = new Gson().toJson(data.game());
            statement.setString(4, jState);
            statement.setInt(5, data.gameID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
