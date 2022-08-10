package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.mapper.PlayerMapper;
import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.repository.PlayerRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlayerRepositoryImpl extends AbstractRepository<Player> implements PlayerRepository {
    private static PlayerRepository repository = null;

    // Mapper will map data from result set to the domain object
    private final PlayerMapper mapper;

    private PlayerRepositoryImpl() {
        mapper = new PlayerMapper();
    }

    public static PlayerRepository getRepository() {
        if (repository == null) repository = new PlayerRepositoryImpl();
        return repository;
    }

    /**
     * {@inheritDoc}
     *
     * @param username player username
     * @return A Player with username or null
     */
    @Override
    public Player findByUsername(String username) {
        return executeQuerySingle(connection -> {
            // write query to find player by username
            final String query = """
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

    /**
     * {@inheritDoc}
     *
     * @param username player username
     * @return true if player with username existed or false
     */
    @Override
    public boolean existedByUsername(String username) {
        // create a connection to database
        return executeQuerySingle(connection -> {
            // write query to check player was existed by username
            final String query = """
                    select username
                    from player
                    where username = ?;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // return value from result set
            ResultSet results = statement.executeQuery();
            if (results.next() && results.getString("username").equals(username)) {
                return new Player();
            } else return null;
        }) != null;
    }

    /**
     * {@inheritDoc}
     *
     * @param player data will insert to database
     * @return true if inserted successful player to database or false
     */
    @Override
    public boolean insert(Player player) {
        // create a connection to database
        return executeUpdate(connection -> {
            // write query to insert player to database
            final String query = """
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
}
