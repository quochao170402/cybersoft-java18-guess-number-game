package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.config.MySQLConnection;
import cybersoft.javabackend.java18.game.model.Guess;
import cybersoft.javabackend.java18.game.repository.GuessRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuessRepositoryImpl implements GuessRepository {

    private static GuessRepository repository = null;

    private GuessRepositoryImpl() {
    }

    public static GuessRepository getRepository() {
        if (repository == null) repository = new GuessRepositoryImpl();
        return repository;
    }

    @Override
    public List<Guess> findByGameId(String id) {
        /* JDBC Connection */
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to find guesses by game id
            String query = """
                    select session_id, value, moment
                    from guess
                    where session_id = ?
                    """;

            // create prepared to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);

            // get result from result set
            ResultSet results = statement.executeQuery();
            List<Guess> guesses = new ArrayList<>();
            while (results.next()) {
                guesses.add(new Guess(
                        results.getString(1),
                        results.getInt(2),
                        results.getTimestamp(3).toLocalDateTime()
                ));
            }
            return guesses;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }

    @Override
    public void insert(Guess guess) {
        /* JDBC Connection */
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to insert guess to database
            String query = """
                    insert into guess(value, moment, session_id)
                    values (?, ?, ?);
                    """;

            // create prepared to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, guess.getNumber());
            statement.setTimestamp(2, Timestamp.valueOf(guess.getTime()));
            statement.setString(3, guess.getGameId());

            // get result from result set
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }


}
