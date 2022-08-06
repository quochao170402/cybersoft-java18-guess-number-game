package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.config.MySQLConnection;
import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.repository.PlayerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRepositoryImpl implements PlayerRepository {
    private static PlayerRepository repository = null;

    private PlayerRepositoryImpl() {

    }

    public static PlayerRepository getRepository() {
        if (repository == null) repository = new PlayerRepositoryImpl();
        return repository;
    }

    @Override
    public Player findByUsername(String username) {
        /* JDBC connection*/
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to find player by username
            String query = """
                    select username, password, name
                    from player
                    where username = ?;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // return value from result set
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return new Player(results.getString("username"),
                        results.getString("password"),
                        results.getString("name"));
            } else return null;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }

    @Override
    public boolean existedByUsername(String username) {
        /* JDBC connection */
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {
            // write query to check player was existed by username
            String query = """
                    select username
                    from player
                    where username = ?;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            return statement.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }

    @Override
    public boolean insert(Player player) {
        /* JDBC connection */
        // create a connection to database
        try (Connection connection = MySQLConnection.getConnection()) {

            // write query to insert player to database
            String query = """
                    insert into player(username, password, name)
                    values (?, ?, ?);
                    """;

            // create a prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUsername());
            statement.setString(2, player.getPassword());
            statement.setString(3, player.getName());

            // execute prepared statement
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error while connecting database: %s", e.getMessage())
            );
        }
    }
}
