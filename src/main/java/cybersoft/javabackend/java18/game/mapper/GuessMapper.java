package cybersoft.javabackend.java18.game.mapper;

import cybersoft.javabackend.java18.game.model.Guess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class GuessMapper implements RowMapper<Guess> {
    @Override
    public Guess map(ResultSet results) throws SQLException {
        return new Guess()
                .gameId(results.getString("session_id"))
                .number(results.getInt("value"))
                .result(results.getInt("result"))
                .time(getLocalDateTimeFromResultSet(results, "moment"));
    }

    private LocalDateTime getLocalDateTimeFromResultSet(ResultSet resultSet, String column) {
        try {
            Timestamp timestamp = resultSet.getTimestamp(column);
            if (timestamp == null) return null;
            return timestamp.toLocalDateTime();
        } catch (SQLException e) {
            return null;
        }
    }
}
