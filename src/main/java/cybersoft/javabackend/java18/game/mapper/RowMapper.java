package cybersoft.javabackend.java18.game.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
    public T map(ResultSet results) throws SQLException;
}
