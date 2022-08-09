package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.config.MySQLConnection;
import cybersoft.javabackend.java18.game.exception.DatabaseNotFoundException;
import cybersoft.javabackend.java18.game.repository.JdbcExecute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AbstractRepository<T> {
    public List<T> executeQuery(JdbcExecute<List<T>> processor) {
        try (Connection connection = MySQLConnection.getConnection()) {
            return processor.processQuery(connection);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }

    public T executeQuerySingle(JdbcExecute<T> processor) {
        try (Connection connection = MySQLConnection.getConnection()) {
            return processor.processQuery(connection);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }

    public int executeUpdate(JdbcExecute<Integer> processor) {
        try (Connection connection = MySQLConnection.getConnection()) {
            return processor.processQuery(connection);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }

    public int executeCountRecord(JdbcExecute<Integer> processor) {
        try (Connection connection = MySQLConnection.getConnection()) {
            return processor.processQuery(connection);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }
}
