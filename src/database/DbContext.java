package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbContext {

    public Connection connect(String dbUrl, String dbUsername, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
