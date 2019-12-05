package sample.sqloperation;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.WindowOperation;

import javax.sql.PooledConnection;
import java.sql.*;
import java.time.LocalDate;

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

    public void insertSubscriptionToDatabase(Connection connection, WindowOperation windowOperation, int id, ComboBox store, ComboBox category, ComboBox rate, ComboBox payment, String comment) throws SQLException {
        Statement stmt = connection.createStatement();

        String insert_store;
        String insert_category;
        String insert_payment;
        if (store.getSelectionModel().isEmpty())
            insert_store = null;
        else
            insert_store = store.getValue().toString();

        if (category.getSelectionModel().isEmpty())
            insert_category = null;
        else
            insert_category = category.getValue().toString();

        if (payment.getSelectionModel().isEmpty())
            insert_payment = null;
        else
            insert_payment = payment.getValue().toString();


        if (rate.getSelectionModel().isEmpty()) {
            stmt.executeUpdate("insert into subskrypcje(id_klienta, nazwa_sklepu, nazwa_kategorii, sposob_platnosci, ocena, komentarz) " +
                    "values (" + id + ",'" + insert_store + "','" + insert_category + "','" + insert_payment + "'," + null + ",'" + comment + "')");
        } else {
            stmt.executeUpdate("insert into subskrypcje(id_klienta, nazwa_sklepu, nazwa_kategorii, sposob_platnosci, ocena, komentarz) " +
                    "values (" + id + ",'" + insert_store + "','" + insert_category + "','" + insert_payment + "'," + Integer.parseInt(rate.getValue().toString()) + ",'" + comment + "')");
        }


        stmt.close();
        windowOperation.warrningWindow("Dodano kategorię", "Operacja przebiegła pomyślnie", "Kategoria został dodany do bazy danych", Alert.AlertType.INFORMATION);
    }

    public void updateSubscriptionToDatabase(Connection connection, WindowOperation windowOperation, int id, ComboBox store, ComboBox category, ComboBox rate, ComboBox payment, String comment) throws SQLException {
        Statement stmt = connection.createStatement();

        String insert_store;
        String insert_category;
        String insert_payment;

        if (store.getSelectionModel().isEmpty())
            insert_store = null;
        else
            insert_store = store.getValue().toString();

        if (category.getSelectionModel().isEmpty())
            insert_category = null;
        else
            insert_category = category.getValue().toString();

        if (payment.getSelectionModel().isEmpty())
            insert_payment = null;
        else
            insert_payment = payment.getValue().toString();


        if (rate.getSelectionModel().isEmpty()) {
            stmt.executeUpdate("UPDATE subskrypcje  " +
                    "SET  nazwa_sklepu = '" + insert_store + "', nazwa_kategorii = '" + insert_category + "', sposob_platnosci = '" + insert_payment + "', komentarz = '" + comment + "' where id_klienta = " + id);
        } else {
            stmt.executeUpdate("UPDATE subskrypcje  " +
                    "SET  nazwa_sklepu = '" + insert_store + "', nazwa_kategorii = '" + insert_category + "', sposob_platnosci = '" + insert_payment + "', ocena = " + Integer.parseInt(rate.getValue().toString()) + ", komentarz = '" + comment + "'where id_klienta = " + id);
        }


        stmt.close();
        windowOperation.warrningWindow("Dodano kategorię", "Operacja przebiegła pomyślnie", "Kategoria został dodany do bazy danych", Alert.AlertType.INFORMATION);
    }

    public void setSubscribedValude(int id, ComboBox store, ComboBox category, ComboBox rate, ComboBox payment, TextField comment) throws SQLException {
        SqlConnection sqlConnection = new SqlConnection();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nazwa_sklepu, nazwa_kategorii, sposob_platnosci, ocena, komentarz from SUBSKRYPCJE where id_klienta = " + id);

        if (rs.next()) {
            if (!rs.getString(1).equals("null"))
                store.getSelectionModel().select(rs.getString(1));
            if (!rs.getString(2).equals("null"))
                category.getSelectionModel().select(rs.getString(2));
            if (!rs.getString(3).equals("null"))
                payment.getSelectionModel().select(rs.getString(3));
            if (rs.getInt(4) != 0)
                rate.getSelectionModel().select(rs.getInt(4));
            if (!rs.getString(5).equals("null"))
                comment.setText(rs.getString(5));
        }

        rs.close();
        stmt.close();
    }

    public Boolean idInSubscription(int id) throws SQLException {
        SqlConnection sqlConnection = new SqlConnection();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from subskrypcje where id_klienta = " + id);
        if (rs.next()) {
            rs.close();
            stmt.close();
            return true;
        }
        rs.close();
        stmt.close();
        return false;
    }
}


