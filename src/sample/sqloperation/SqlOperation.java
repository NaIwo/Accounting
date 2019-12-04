package sample.sqloperation;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.WindowOperation;

import java.sql.*;

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
        windowOperation.warrningWindow("Dodano kategorię", "Operacja przebiegła pomyślnie", "Kategoria został dodany do bazy danych", Alert.AlertType.INFORMATION);
    }

    public boolean checkIfStoreWasAddedByUser(Connection connection, TextField store, Integer id) throws SQLException {
        boolean result;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from sklepy where nazwa_sklepu='" + store.getText().toUpperCase() + "' and id_klienta='" + id + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    public void removeStore(Connection connection, TextField store, WindowOperation windowOperation) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("delete from sklepy where nazwa_sklepu='" + store.getText().toUpperCase() + "'");
        stmt.close();
        windowOperation.warrningWindow("Usunięto sklep", "Operacja przebiegła pomyślnie", "Sklep został usunięty z bazy danych", Alert.AlertType.INFORMATION);
    }

    public boolean checkIfCategoryWasAddedByUser(Connection connection, TextField category, Integer id) throws SQLException {
        boolean result;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from kategorie where nazwa_kategorii='" + category.getText().toUpperCase() + "' and id_klienta='" + id + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    public void removeCategory(Connection connection, TextField category, WindowOperation windowOperation) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("delete from kategorie where nazwa_kategorii='" + category.getText().toUpperCase() + "'");
        stmt.close();
        windowOperation.warrningWindow("Usunięto kategorię", "Operacja przebiegła pomyślnie", "Kategoria został usunięta z bazy danych", Alert.AlertType.INFORMATION);
    }

    public void addTransaction(Connection connection, WindowOperation windowOperation, int id, String kwota, String sklep, String kategoria, String data,
                               String sposob, String ocena, String komentarz) throws SQLException {

        CallableStatement stmt = connection.prepareCall("{call NowaTransakcja(?, ?, ?, ?, ?, ?, ?, ?)}");

        stmt.setInt(1, id);
        stmt.setString(2, kwota.replaceAll("\\.", ","));
        stmt.setString(3, sklep);
        stmt.setString(4, kategoria);
        stmt.setString(5, data.replaceAll("-", "/"));
        stmt.setString(6, sposob);
        stmt.setString(7, ocena);
        if (komentarz != null) stmt.setString(8, komentarz);
        else stmt.setNull(8, Types.VARCHAR);


        stmt.execute();
        stmt.close();
        windowOperation.warrningWindow("Dodano transakcję", "Operacja przebiegła pomyślnie", "Transakcja została dodana do bazy danych", Alert.AlertType.INFORMATION);
    }

    public void addStoresToMenu(ComboBox comboBox, Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nazwa_sklepu, ocena from sklepy order by nazwa_sklepu");
        comboBox.getItems().removeAll(comboBox.getItems());
        while (rs.next()) {
            comboBox.getItems().add(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }

    public void addCategoriesToMenu(ComboBox comboBox, Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nazwa_kategorii from kategorie order by nazwa_kategorii");
        comboBox.getItems().removeAll(comboBox.getItems());
        while (rs.next()) {
            comboBox.getItems().add(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }

    public void addPaymentToMenu(ComboBox comboBox, Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT distinct sposob from platnosci order by sposob");
        comboBox.getItems().removeAll(comboBox.getItems());
        while (rs.next()) {
            comboBox.getItems().add(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }
}
