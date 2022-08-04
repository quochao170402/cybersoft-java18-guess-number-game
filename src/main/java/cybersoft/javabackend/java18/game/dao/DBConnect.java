package cybersoft.javabackend.java18.game.dao;

import cybersoft.javabackend.java18.game.utils.JDBCConstant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static DBConnect connect = null;

    private DBConnect() {
    }

    public static DBConnect getINSTANCE() {
        if (connect == null) connect = new DBConnect();
        return connect;
    }

    public Connection getConnection() {
        String url = "jdbc:" + JDBCConstant.SERVER + "://localhost:" + JDBCConstant.HOST + "/" + JDBCConstant.DB_NAME;
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, JDBCConstant.USER_NAME, JDBCConstant.PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
