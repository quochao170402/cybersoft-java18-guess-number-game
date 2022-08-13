package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.mapper.GuessMapper;
import cybersoft.javabackend.java18.game.mapper.RowMapper;
import cybersoft.javabackend.java18.game.model.Guess;
import cybersoft.javabackend.java18.game.repository.AbstractRepository;
import cybersoft.javabackend.java18.game.repository.GuessRepository;
import cybersoft.javabackend.java18.game.utils.JspUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class GuessRepositoryImpl extends AbstractRepository<Guess> implements GuessRepository {

    private static GuessRepository repository = null;

    // Row mapper will map data from result set to domain object
    private final RowMapper<Guess> mapper;

    private GuessRepositoryImpl() {
        mapper = new GuessMapper();
    }

    public static GuessRepository getRepository() {
        if (repository == null) repository = new GuessRepositoryImpl();
        return repository;
    }

    /**
     * {@inheritDoc}
     *
     * @param id game session id
     * @return List guesses of game session. Always return list, if not found return empty list.
     */
    @Override
    public List<Guess> findByGameId(String id) {
        /* JDBC Connection */
        // create a connection to database
        return executeQuery(connection -> {
            // write query to find guesses by game id
            final String query = """
                    select session_id, value, result, moment
                    from guess
                    where session_id = ?
                    order by id desc
                    """;

            // create prepared to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);

            // get result from result set
            ResultSet results = statement.executeQuery();
            List<Guess> guesses = new ArrayList<>();
            while (results.next()) {
                guesses.add(mapper.map(results));
            }
            return guesses;
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param guess guess need insert
     */
    @Override
    public void insert(Guess guess) {
        // create a connection to database
        executeUpdate(connection -> {
            // write query to insert guess to database
            final String query = """
                    insert into guess(value, moment, result, session_id)
                    values (?, ?, ?, ?);
                    """;

            // create prepared to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, guess.getNumber());
            statement.setTimestamp(2, Timestamp.from(
                    guess.getTime().toInstant(ZoneOffset.of("+07:00"))));
            statement.setInt(3, guess.getResult());
            statement.setString(4, guess.getGameId());

            return statement.executeUpdate();
        });
    }

    @Override
    public List<Guess> findByGameIdWithPagination(String gameId, int page) {
        return executeQuery(connection -> {
            // write query to find guesses by game id with pagination
            final String query = """
                    select session_id, value, result, moment
                    from guess
                    where session_id = ?
                    limit ? offset ?
                    """;

            // create prepared to execute query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gameId);
            statement.setInt(2, JspUtils.DEFAULT_PAGE_SIZE);
            statement.setInt(3, (page - 1) * JspUtils.DEFAULT_PAGE_SIZE);

            // get result from result set
            ResultSet results = statement.executeQuery();
            List<Guess> guesses = new ArrayList<>();
            while (results.next()) {
                guesses.add(mapper.map(results));
            }
            return guesses;
        });
    }

    @Override
    public int countGuessesByGameId(String gameId) {
        return executeCountRecord(connection -> {
            final String query = """
                    select count(*) as total
                    from guess
                    where session_id = ?
                    """;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gameId);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getInt("total");
            }
            return 0;
        });
    }

}
