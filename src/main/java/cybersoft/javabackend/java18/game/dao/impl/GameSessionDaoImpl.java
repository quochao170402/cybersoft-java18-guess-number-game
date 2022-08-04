package cybersoft.javabackend.java18.game.dao.impl;

import cybersoft.javabackend.java18.game.dao.DBConnect;
import cybersoft.javabackend.java18.game.dao.GameSessionDao;
import cybersoft.javabackend.java18.game.model.GameSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameSessionDaoImpl implements GameSessionDao {

    private static GameSessionDaoImpl dao = null;
    private final Connection connection;

    private GameSessionDaoImpl() {
        connection = DBConnect.getINSTANCE().getConnection();
    }

    public static GameSessionDaoImpl getInstance() {
        if (dao == null) dao = new GameSessionDaoImpl();
        return dao;
    }

    @Override
    public void insert(GameSession gameSession) {
        if (connection == null) {
            return;
        }

        String query = "insert into game(id, target_number, complete, active, start_time, end_time, player) " +
                "values (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gameSession.getId());
            statement.setInt(2, gameSession.getTargetNumber());
            statement.setBoolean(3, gameSession.isCompleted());
            statement.setBoolean(4, gameSession.isActive());
            statement.setTimestamp(5, Timestamp.valueOf(gameSession.getStartTime()));
            statement.setTimestamp(6,
                    gameSession.getEndTime() == null ?
                    null : Timestamp.valueOf(gameSession.getEndTime()));
            statement.setString(7,gameSession.getUsername());
        } catch (SQLException ignored) {
        }
    }

    @Override
    public List<GameSession> findByUsername(String username) {
        if (connection == null){
            return new ArrayList<>();
        }
        List<GameSession> games = new ArrayList<>();
        String query = "select * from game where player = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                games.add(new GameSession(
                        result.getString("id"),
                        result.getInt("target_number"),
                        result.getTimestamp("start_time").toLocalDateTime(),
                        result.getTimestamp("end_time") == null ?
                                null : result.getTimestamp("end_time").toLocalDateTime(),
                        result.getBoolean("complete"),
                        result.getBoolean("active"),
                        username
                ));
            }
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return games;
    }

    @Override
    public List<GameSession> findFinishedGame(){
        if (connection == null){
            return new ArrayList<>();
        }
        List<GameSession> games = new ArrayList<>();
        String query = "select * from game where complete = true";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                games.add(new GameSession(
                        result.getString("id"),
                        result.getInt("target_number"),
                        result.getTimestamp("start_time").toLocalDateTime(),
                        result.getTimestamp("end_time").toLocalDateTime(),
                        result.getBoolean("complete"),
                        result.getBoolean("active"),
                        result.getString("player")
                ));
            }
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return games;
    }

    /**
     * Method deactivate all player's games by username
     * @param username username of player need deactivate games
     */
    @Override
    public void deactivateAllGameByUsername(String username) {
        if (connection == null) {
            return;
        }

        String query = "update game set complete = false where username = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.executeUpdate();
        } catch (SQLException ignored) {

        }
    }

    @Override
    public int count() {
        if (connection == null) {
            return -1;
        }

        String query = "select count(*) from game";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return result.getInt(1);
            }else return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public void finishedGame(GameSession gameSession) {
        if (connection == null) {
            return;
        }

        String query = "update game set complete = true, active = false, end_time = ? where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1,Timestamp.valueOf(gameSession.getEndTime()));
            statement.setString(2,gameSession.getId());
            statement.executeUpdate();
        } catch (SQLException ignored) {

        }
    }
}
