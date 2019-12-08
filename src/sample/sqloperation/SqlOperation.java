package sample.sqloperation;

import javafx.scene.control.*;
import sample.WindowOperation;
import sample.controllers.RegistrationController;

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
                rate.getSelectionModel().select(rs.getInt(4) - 1);
            if (!rs.getString(5).equals("null"))
                comment.setText(rs.getString(5));
        }
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

    public boolean readData(Connection conn, TextField loginLabel, PasswordField passwordLabel) throws SQLException {
        boolean result;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT login, haslo from Dane_logowania where login='" + loginLabel.getText() + "' and haslo='" + passwordLabel.getText() + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    public void addUserToDatabase(RegistrationController registrationController, Connection sqlConnection, TextField loginLabel, TextField passwordLabel) throws SQLException {


        CallableStatement stmt = sqlConnection.prepareCall("{call NOWYUSER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

        stmt.setString(1, registrationController.getName());
        stmt.setString(2, registrationController.getSurname());
        stmt.setString(3, loginLabel.getText());
        stmt.setString(4, passwordLabel.getText());
        stmt.setString(5, registrationController.getStreet());
        stmt.setInt(6, registrationController.getHouse());
        if (registrationController.getHome() != null) stmt.setInt(7, registrationController.getHome());
        else stmt.setNull(7, Types.INTEGER);
        stmt.setInt(8, registrationController.getPostalcode());
        stmt.setString(9, registrationController.getCity());
        stmt.setString(10, registrationController.getEmail());
        if (registrationController.getPhoneone() != null) stmt.setInt(11, registrationController.getPhoneone());
        else stmt.setNull(11, Types.INTEGER);
        if (registrationController.getPhonetwo() != null) stmt.setInt(12, registrationController.getPhonetwo());
        else stmt.setNull(12, Types.INTEGER);

        stmt.execute();
        stmt.close();
    }

    public void readUserData(Connection connection, Integer id, TextField nameLabel, TextField surnameLabel, TextField streetLabel, TextField houseLabel, TextField homeLabel, TextField postcodeLabel,
                             TextField cityLabel, TextField emailLabel, TextField phoneoneLabel, TextField phonetwoLabel, TextField haslo, TextField hasloConfirm, TextField login) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT IMIE, NAZWISKO, K.LOGIN, HASLO, ULICA, NR_DOMU, NR_MIESZKANIA, KOD_POCZTOWY, MIEJSCOWOSC, EMAIL, NR_TELEFONU1, Nr_TELEFONU2 " +
                "FROM KLIENCI K " +
                "LEFT JOIN DANE_LOGOWANIA D ON K.LOGIN = D.LOGIN " +
                "LEFT JOIN ADRESY A ON A.ID_ADRESU = K.ID_ADRESU " +
                "LEFT JOIN KONTAKTY KT ON K.ID_KONTAKTU = KT.ID_KONTAKTU WHERE ID_KLIENTA = " + id);
        if (rs.next()) {
            nameLabel.setText(rs.getString(1));
            surnameLabel.setText(rs.getString(2));
            login.setText(rs.getString(3));
            haslo.setText(rs.getString(4));
            hasloConfirm.setText(rs.getString(4));
            streetLabel.setText(rs.getString(5));
            houseLabel.setText(rs.getString(6));
            if (rs.getInt(7) != 0) homeLabel.setText(rs.getString(7));
            postcodeLabel.setText(rs.getString(8).substring(0,2) + "-" + rs.getString(8).substring(2, rs.getString(8).length()));
            cityLabel.setText(rs.getString(9));
            emailLabel.setText(rs.getString(10));
            if (rs.getInt(11) != 0) phoneoneLabel.setText(rs.getString(10));
            if (rs.getInt(12) != 0) phonetwoLabel.setText(rs.getString(11));

        }
        rs.close();
        stmt.close();
    }

    public boolean checkIfLoginExists(SqlConnection sqlConnection, TextField loginLabel) throws SQLException {
        boolean result;
        sqlConnection.connect();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("Select login from dane_logowania where login='" + loginLabel.getText() + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    public boolean checkIfLoginUpdatingExists(SqlConnection sqlConnection, TextField loginLabel, Integer id) throws SQLException {
        boolean result;
        sqlConnection.connect();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT K.LOGIN FROM KLIENCI K LEFT JOIN DANE_LOGOWANIA D ON K.LOGIN = D.LOGIN " +
                "WHERE K.LOGIN = " + "'" + loginLabel.getText() + "'" + " AND K.ID_KLIENTA != " + id);
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    public void updateUserInformation(Connection connection, WindowOperation windowOperation, Integer id,  TextField[] allTextFields) throws SQLException {

        Statement info = connection.createStatement();
        ResultSet rs = info.executeQuery("SELECT A.ID_ADRESU, K.ID_KONTAKTU, D.LOGIN " +
                "FROM KLIENCI K " +
                "LEFT JOIN DANE_LOGOWANIA D ON K.LOGIN = D.LOGIN " +
                "LEFT JOIN ADRESY A ON A.ID_ADRESU = K.ID_ADRESU " +
                "LEFT JOIN KONTAKTY KT ON K.ID_KONTAKTU = KT.ID_KONTAKTU WHERE ID_KLIENTA = " + id);


        CallableStatement stmt = connection.prepareCall("{call updateUserInformation(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

        if(rs.next())
        {
            stmt.setString(1, allTextFields[0].getText());
            stmt.setString(2, allTextFields[1].getText());
            stmt.setString(3, allTextFields[12].getText());
            stmt.setString(4, allTextFields[10].getText());
            stmt.setString(5, allTextFields[2].getText());
            stmt.setInt(6, Integer.parseInt(allTextFields[3].getText().replaceAll(" ", "")));
            if(!allTextFields[4].getText().isEmpty()) stmt.setInt(7, Integer.parseInt(allTextFields[4].getText().replaceAll(" ", "")));
            else stmt.setNull(7, Types.INTEGER);
            stmt.setInt(8, Integer.parseInt(allTextFields[5].getText().replaceAll("-", "").replaceAll(" ", "")));
            stmt.setString(9, allTextFields[6].getText());
            stmt.setString(10, allTextFields[7].getText());
            if(!allTextFields[8].getText().isEmpty()) stmt.setInt(11, Integer.parseInt(allTextFields[8].getText()));
            else stmt.setNull(11, Types.INTEGER);
            if(!allTextFields[9].getText().isEmpty()) stmt.setInt(12, Integer.parseInt(allTextFields[9].getText()));
            else stmt.setNull(12, Types.INTEGER);
            stmt.setInt(13, Integer.parseInt(rs.getString(1).replaceAll(" ", "")));
            stmt.setInt(14, Integer.parseInt(rs.getString(2).replaceAll(" ","")));
            stmt.setString(15, rs.getString(3));
            stmt.setInt(16, id);
        }


        stmt.execute();
        stmt.close();
        info.close();
        windowOperation.warrningWindow("Dodano transakcję", "Operacja przebiegła pomyślnie", "Transakcja została dodana do bazy danych", Alert.AlertType.INFORMATION);
    }

    public void insertRestrictionToDatabase(Connection connection, WindowOperation windowOperation, TextField payment, TextField comment, ComboBox category, Integer id) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ID_KATEGORII FROM KATEGORIE WHERE NAZWA_KATEGORII = '" + category.getValue().toString().toUpperCase() + "'");
        if(rs.next()) {
            if(!comment.getText().isEmpty())
             stmt.executeUpdate("INSERT INTO OGRANICZENIA (ID_KLIENTA, ID_KATEGORII, WARTOSC_OGRANICZENIA, ALARM) VALUES" +
                        "(" + id + ", " + rs.getInt(1) + ", " + Integer.parseInt(payment.getText().replaceAll(" ", "").replaceAll(",", ".")) + ", '" + comment.getText() + "')");
            else
                stmt.executeUpdate("INSERT INTO OGRANICZENIA (ID_KLIENTA, ID_KATEGORII, WARTOSC_OGRANICZENIA, ALARM) VALUES" +
                        "(" + id + ", " + rs.getInt(1) + ", " + Integer.parseInt(payment.getText().replaceAll(" ", "").replaceAll(",", ".")) + ", 'Następnym razem bądź bardziej rozważny.')");
        }
        stmt.close();
        windowOperation.warrningWindow("Dodano ograniczenie", "Operacja przebiegła pomyślnie", "Ograniczenie zostało dodane do bazy danych", Alert.AlertType.INFORMATION);
    }


    public Boolean checkCurrentRestriction(Connection connection, Integer id, ComboBox category) throws SQLException {

        Statement stmt = connection.createStatement();
        ResultSet info = stmt.executeQuery("SELECT ID_KATEGORII FROM KATEGORIE WHERE NAZWA_KATEGORII = '" + category.getValue().toString().toUpperCase() + "'");
        ResultSet rs;
        if(info.next()) {
            rs = stmt.executeQuery("SELECT * FROM OGRANICZENIA O LEFT JOIN KATEGORIE K ON K.ID_KATEGORII=O.ID_KATEGORII " +
                    " WHERE O.ID_KATEGORII = " + info.getInt(1) + " AND O.ID_KLIENTA = " + id);
            if (rs.next()) {
                stmt.close();
                return true;
            } else {
                stmt.close();
                return false;
            }
        }
        else
            return false;
    }

    public void updateRestrictionDatabase(Connection connection, WindowOperation windowOperation, TextField payment, TextField comment, ComboBox category, Integer id) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ID_KATEGORII FROM KATEGORIE WHERE NAZWA_KATEGORII = '" + category.getValue().toString().toUpperCase() + "'");
        if(rs.next()) {
                if(!comment.getText().isEmpty())
                {
                    stmt.executeUpdate("UPDATE OGRANICZENIA " +
                            "SET WARTOSC_OGRANICZENIA = " + Integer.parseInt(payment.getText().replaceAll(" ", "").replaceAll(",", ".")) + " " +
                            ", ALARM = '" + comment.getText() +"' "+
                            "WHERE ID_KATEGORII = "+ rs.getInt(1) + " AND ID_KLIENTA = "+ id);
                }
                else
                {
                    stmt.executeUpdate("UPDATE OGRANICZENIA " +
                            "SET WARTOSC_OGRANICZENIA = " + Integer.parseInt(payment.getText().replaceAll(" ", "").replaceAll(",", ".")) + " " +
                            ", ALARM = 'Nastpnym razem bądź bardziej rozważny.' "+
                            "WHERE ID_KATEGORII = "+ rs.getInt(1) + " AND ID_KLIENTA = "+ id);
                }
        }
        stmt.close();
        windowOperation.warrningWindow("Zmieniono ograniczenie", "Operacja przebiegła pomyślnie", "Ograniczenie zostało zmienione w bazie danych", Alert.AlertType.INFORMATION);
    }

    public void deleteRestrictionFromDatabase(Connection connection, WindowOperation windowOperation, Integer id, ComboBox category) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ID_KATEGORII FROM KATEGORIE WHERE NAZWA_KATEGORII = '" + category.getValue().toString().toUpperCase() + "'");
        if(rs.next()) {
            stmt.executeUpdate("DELETE OGRANICZENIA " +
                    "WHERE ID_KLIENTA = " + id + " AND ID_KATEGORII = " + rs.getInt(1) );
        }
        stmt.close();
        windowOperation.warrningWindow("Usunięto ograniczenie", "Operacja przebiegła pomyślnie", "Ograniczenie zostało usunięte z bazy danych", Alert.AlertType.INFORMATION);
    }

    public float checkCurrentCategory(Connection connection, Integer id, ComboBox category) throws SQLException {

        float output;

        CallableStatement stmt = connection.prepareCall("{? = call laczna_wartosc_na_kategorie(?, ?)}");

        stmt.registerOutParameter(1, Types.FLOAT);

        stmt.setInt(2, id);
        stmt.setString(3, category.getValue().toString().toUpperCase());
        stmt.execute();
        output = stmt.getFloat(1);
        stmt.close();

        return output;
    }

    public void checkIfExceeded(Connection connection, WindowOperation windowOperation, Integer id, ComboBox category) throws SQLException {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT ID_KATEGORII FROM KATEGORIE WHERE NAZWA_KATEGORII = '" + category.getValue().toString().toUpperCase() + "'");
        if(rs.next()) {
            ResultSet out;
            out = stmt.executeQuery("SELECT WARTOSC_OGRANICZENIA, ALARM FROM OGRANICZENIA " +
                    " WHERE ID_KLIENTA = " + id + " AND ID_KATEGORII = " + rs.getInt(1));
            if(out.next())
            {
                if(checkCurrentCategory(connection, id, category) > out.getInt(1))
                    windowOperation.warrningWindow("UWAŻAJ!! ", "Przekroczono wartość ograniczenia!! ",
                            out.getString(2), Alert.AlertType.WARNING);

            }
        }
        stmt.close();

    }
}


