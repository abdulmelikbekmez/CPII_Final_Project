package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAccess {

    private final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String password = "test123";

    protected Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl,username, password);
        return connection;
    }

    protected void updateDatabase(String query) throws SQLException {
        Connection c = getConnection();
        Statement s = c.createStatement();
        s.executeUpdate(query);
        s.close();
        c.close();
    }

}
