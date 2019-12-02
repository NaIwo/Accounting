package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnection {

    private static Connection conn;

    public Connection getConnection() {
        return conn;
    }

    public boolean connect() {
        conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@admlab2.cs.put.poznan.pl:1521/dblab02_students.cs.put.poznan.pl", "inf136785", "Yeezaegha");
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }
}
