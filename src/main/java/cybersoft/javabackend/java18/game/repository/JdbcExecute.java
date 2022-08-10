package cybersoft.javabackend.java18.game.repository;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface JdbcExecute<T> {
    /**
     * The processor execute query
     *
     * @param connection Connection connect to MySQL database
     * @return Object obtained from the result set, which the statement returns after executed
     * @throws SQLException when connecting database error
     */
    T processQuery(Connection connection) throws SQLException;
}
