package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.config.MySQLConnection;
import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.repository.GameSessionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameSessionRepositoryImpl implements GameSessionRepository {

    private static GameSessionRepository repository = null;

    private GameSessionRepositoryImpl() {
    }

    public static GameSessionRepository getRepository() {
        if (repository == null) repository = new GameSessionRepositoryImpl();
        return repository;
    }

    @Override
    public void insert(GameSession gameSession) {
        /* JDBC Connection */
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to insert game session to database
            String query = """
                    insert into game_session(id, target, username)
                    values (?, ?, ?);
                    """;

            // create a prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gameSession.getId());
            statement.setInt(2, gameSession.getTargetNumber());
            statement.setString(3, gameSession.getUsername());

        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }

    @Override
    public List<GameSession> findByUsername(String username) {
        /* JDBC Connection */
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to find games by username
            String query = """
                    select *
                    from game_session
                    where username = ?;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // get result set
            ResultSet results = statement.executeQuery();
            List<GameSession> gameSessions = new ArrayList<>();
            while (results.next()) {
                gameSessions.add(new GameSession(
                        results.getString("id"),
                        results.getInt("target_number"),
                        results.getTimestamp("start_time").toLocalDateTime(),
                        results.getTimestamp("end_time") == null ?
                                null : results.getTimestamp("end_time").toLocalDateTime(),
                        results.getBoolean("complete"),
                        results.getBoolean("active"),
                        username
                ));
            }

            return gameSessions;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }

    @Override
    public List<GameSession> findFinishedGames() {
        /* JDBC Connection */
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to find completed games
            String query = """
                    select *
                    from game_session
                    where completed = 1;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);

            // get result set
            ResultSet results = statement.executeQuery();
            List<GameSession> gameSessions = new ArrayList<>();
            while (results.next()) {
                gameSessions.add(new GameSession(
                        results.getString("id"),
                        results.getInt("target_number"),
                        results.getTimestamp("start_time").toLocalDateTime(),
                        results.getTimestamp("end_time").toLocalDateTime(),
                        results.getBoolean("complete"),
                        results.getBoolean("active"),
                        results.getString("username")
                ));
            }

            return gameSessions;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }

    @Override

    public void deactivateAllGameByUsername(String username) {
        /* JDBC Connection */
        // Create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to deactivate all games by username
            String query = """
                    update game_session
                    set active = 0
                    where username = ?;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }

    @Override
    public void finishedGameById(String id) {
        /* JDBC Connection */
        // Create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to finished game by game id
            String query = """
                    update game_session
                    set active = 0,
                    completed = 1
                    where id = ?;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }

    @Override
    public int count() {
        /* JDBC Connection */
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to count number of games
            String query = """
                    select count(*)
                    from game_session;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);

            // get result from result set
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getInt(1);
            } else return 0;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }
}
