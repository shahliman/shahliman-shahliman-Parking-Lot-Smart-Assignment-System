package parking.smart.assignment.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    // Extension-da daxil etdiyin məlumatları bura yaz:
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() throws SQLException {
        try {
            // Driver-in yükləndiyindən əmin oluruq
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver tapılmadı!", e);
        }
    }
}