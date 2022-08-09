package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.mapper.GameSessionMapper;
import cybersoft.javabackend.java18.game.mapper.RowMapper;
import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.repository.GameSessionRepository;
import cybersoft.javabackend.java18.game.utils.JspUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class GameSessionRepositoryImpl extends AbstractRepository<GameSession> implements GameSessionRepository {

    private static GameSessionRepository repository = null;
    private RowMapper<GameSession> mapper;

    private GameSessionRepositoryImpl() {
        mapper = new GameSessionMapper();
    }

    public static GameSessionRepository getRepository() {
        if (repository == null) repository = new GameSessionRepositoryImpl();
        return repository;
    }

    @Override
    public void insert(GameSession gameSession) {
        executeUpdate(connection -> {
            // write query to insert game session to database
            String query = """
                    insert into game_session
                    (id, target, start_time, completed, active, username)
                    values(?, ?, ?, ?, ?, ?)
                    """;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gameSession.getId());
            statement.setInt(2, gameSession.getTargetNumber());
            statement.setTimestamp(3, Timestamp.from(
                    gameSession.getStartTime().toInstant(ZoneOffset.of("+07:00")))
            );
            statement.setBoolean(4, gameSession.isCompleted());
            statement.setBoolean(5, gameSession.isActive());
            statement.setString(6, gameSession.getUsername());

            return statement.executeUpdate();
        });
    }

    @Override
    public List<GameSession> findByUsername(String username) {
        /* JDBC Connection */
        return executeQuery(connection -> {
            // write query to find games by username
            String query = """
                    select id, target, start_time, end_time, completed, active, username
                    from game_session
                    where username = ?;
                    """;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet results = statement.executeQuery();
            List<GameSession> gameSessions = new ArrayList<>();
            while (results.next()) {
                gameSessions.add(mapper.map(results));
            }
            return gameSessions;
        });
    }

    @Override
    public List<GameSession> rankingByPagination(int page) {
        /* JDBC Connection */
        return executeQuery(connection -> {
            // write query to find completed games
            String query = """
                    select id, target, start_time, end_time, username, completed, active
                    from ranking
                    where completed = 1
                    limit ? offset ?
                    """;
            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, JspUtils.DEFAULT_PAGE_SIZE);
            statement.setInt(2, (page - 1) * JspUtils.DEFAULT_PAGE_SIZE);

            // get result set
            ResultSet results = statement.executeQuery();
            List<GameSession> gameSessions = new ArrayList<>();
            while (results.next()) {
                gameSessions.add(mapper.map(results));
            }
            return gameSessions;
        });
    }

    @Override
    public void deactivateAllGameByUsername(String username) {
        /* JDBC Connection */
        // Create a connection to database
        executeUpdate(connection -> {
            // write query to deactivate all games by username
            String query = """
                    update game_session
                    set active = 0
                    where username = ?;
                    """;
            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            return statement.executeUpdate();
        });
    }

    @Override
    public void finishedGameById(String id) {
        /* JDBC Connection */
        // Create a connection to database
        executeUpdate(connection -> {
            // write query to completed game by game id
            String query = """
                    update game_session
                    set active = 0,
                    completed = 1,
                    end_time = ?
                    where id = ?;
                    """;

            // create prepared statement to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, Timestamp.from(
                    LocalDateTime.now().toInstant(ZoneOffset.of("+07:00"))));
            statement.setString(2, id);

            return statement.executeUpdate();
        });
    }

    // how implement count method using abstract repository
    @Override
    public int count() {
        /* JDBC Connection */
        // create a connection to database
        return executeCountRecord(connection -> {
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
        });
    }

    @Override
    public int getRankSize() {
        return executeCountRecord(connection -> {
            // write query to count number of game in rank view
            String query = """
                    select count(*) as size
                    from ranking
                    """;
            PreparedStatement statement = connection.prepareStatement(query);

            // get result from result set
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getInt("size");
            } else return 0;
        });
    }
}
