package cybersoft.javabackend.java18.game.mapper;

import cybersoft.javabackend.java18.game.model.GameSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class GameSessionMapper implements RowMapper<GameSession> {
    @Override
    public GameSession map(ResultSet results) throws SQLException {
        return new GameSession()
                .id(results.getString("id"))
                .targetNumber(results.getInt("target"))
                .startTime(getLocalDateTimeFromResultSet(results, "start_time"))
                .endTime(getLocalDateTimeFromResultSet(results, "end_time"))
                .isCompleted(results.getBoolean("completed"))
                .isActive(results.getBoolean("active"))
                .username(results.getString("username"));
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
