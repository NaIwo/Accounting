package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlOperation {

    public boolean checkIfStoreExists(Connection connection, TextField store) throws SQLException {
        boolean result;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from sklepy where nazwa_sklepu='" + store.getText().toUpperCase() + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    public void addStoreToDatabase(Connection connection, TextField store, Integer id, WindowOperation windowOperation) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("insert into sklepy values ('" + store.getText().toUpperCase() + "', 0 ,'" + id + "')");
        stmt.close();
        store.clear();
        windowOperation.warrningWindow("Dodano sklep", "Operacja przebiegła pomyślnie", "Sklep został dodany do bazy danych", Alert.AlertType.INFORMATION);
    }

    public boolean checkIfCategoryExists(Connection connection, TextField category) throws SQLException {
        boolean result;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from kategorie where nazwa_kategorii='" + category.getText().toUpperCase() + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    public void addCategoryToDatabase(Connection connection, TextField category, Integer id, WindowOperation windowOperation) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("insert into kategorie(nazwa_kategorii, id_klienta) values ('" + category.getText().toUpperCase() + "','" + id + "')");
        stmt.close();
        category.clear();
        windowOperation.warrningWindow("Dodano kategorię", "Operacja przebiegła pomyślnie", "Kategoria został dodany do bazy danych", Alert.AlertType.INFORMATION);
    }
}
