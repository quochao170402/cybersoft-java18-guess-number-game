package cybersoft.javabackend.java18.game.dao.impl;

import cybersoft.javabackend.java18.game.dao.DBConnect;
import cybersoft.javabackend.java18.game.dao.GuessDao;
import cybersoft.javabackend.java18.game.model.Guess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuessDaoImpl implements GuessDao {
    private static GuessDaoImpl dao = null;
    private final Connection connection;

    private GuessDaoImpl(){
        connection = DBConnect.getINSTANCE().getConnection();
    }

    public static GuessDaoImpl getInstance(){
        if (dao == null) dao = new GuessDaoImpl();
        return dao;
    }

    @Override
    public List<Guess> findByGameId(String id) {
        if (connection == null) {
            return null;
        }

        List<Guess> guesses = new ArrayList<>();
        String query = "select * from guess where game = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                guesses.add(new Guess(
                        result.getString("game"),
                        result.getString("player"),
                        result.getInt("number"),
                        result.getString("result"),
                        result.getTimestamp("time").toLocalDateTime()
                ));
            }
            return guesses;
        } catch (SQLException e) {
            return null;
        }

    }

    @Override
    public boolean insert(Guess guess) {
        if (connection == null){
            return false;
        }

        String query = "insert into guess(id, number, result, game, player, time) " +
                "values (?,?,?,?,?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UUID.randomUUID().toString());
            statement.setInt(2,guess.getNumber());
            statement.setString(3,guess.getResult());
            statement.setString(4,guess.getGameId());
            statement.setString(5,guess.getUsername());
            statement.setTimestamp(6, Timestamp.valueOf(guess.getTime()));
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }

    }
}
