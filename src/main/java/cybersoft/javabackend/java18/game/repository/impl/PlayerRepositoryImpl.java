package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.mapper.PlayerMapper;
import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.repository.PlayerRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepositoryImpl extends AbstractRepository<Player> implements PlayerRepository {
    private static PlayerRepository repository = null;
    private PlayerMapper mapper;

    private PlayerRepositoryImpl() {
        mapper = new PlayerMapper();
    }

    public static PlayerRepository getRepository() {
        if (repository == null) repository = new PlayerRepositoryImpl();
        return repository;
    }

    @Override
    public Player findByUsername(String username) {
        /* JDBC connection*/
        // create a connection to database
        return executeQuerySingle(connection -> {
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
                return mapper.map(results);
            } else return null;
        });
    }

    @Override
    public boolean existedByUsername(String username) {
        /* JDBC connection */
        // create a connection to database
        return executeQuerySingle(connection -> {
            // write query to check player was existed by username
            String query = """
                    select username
                    from player
                    where username = ?;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // return value from result set
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return new Player();
            } else return null;
        }) != null;
    }

    @Override
    public boolean insert(Player player) {
        /* JDBC connection */
        // create a connection to database
        return executeUpdate(connection -> {
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
            return statement.executeUpdate();
        }) != 0;
    }

    @Override
    public List<Player> getAll() {
        return executeQuery(connection -> {
            String query = """
                    select * from player;
                    """;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            List<Player> players = new ArrayList<>();
            while (results.next()) {
                players.add(mapper.map(results));
            }
            return players;
        });
    }
}
