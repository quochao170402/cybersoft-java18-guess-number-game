package cybersoft.javabackend.java18.game.repository.impl;

import cybersoft.javabackend.java18.game.config.MySQLConnection;
import cybersoft.javabackend.java18.game.exception.DatabaseNotFoundException;
import cybersoft.javabackend.java18.game.repository.JdbcExecute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AbstractRepository<T> {

    /**
     * Method execute query to get a list data from database
     *
     * @param processor Functional interface will connect database and process query
     * @return List of data
     */
    public List<T> executeQuery(JdbcExecute<List<T>> processor) {
        try (Connection connection = MySQLConnection.getConnection()) {
            return processor.processQuery(connection);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }

    /**
     * Method execute query to get a single data from database
     *
     * @param processor Functional interface will connect database and process query
     * @return Only object if found or null.
     */
    public T executeQuerySingle(JdbcExecute<T> processor) {
        try (Connection connection = MySQLConnection.getConnection()) {
            return processor.processQuery(connection);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }

    /**
     * Method execute query to update data
     *
     * @param processor Functional interface will connect database and process query
     * @return Number of record effected by processor
     */
    public int executeUpdate(JdbcExecute<Integer> processor) {
        try (Connection connection = MySQLConnection.getConnection()) {
            return processor.processQuery(connection);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }

    /**
     * Method count record of a table or view
     *
     * @param processor Functional interface will connect database and process query
     * @return Number of record. If execute error will return 0
     */
    public int executeCountRecord(JdbcExecute<Integer> processor) {
        try (Connection connection = MySQLConnection.getConnection()) {
            return processor.processQuery(connection);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }
}
