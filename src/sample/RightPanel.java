package sample;


import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.sqloperation.SqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class RightPanel {

    private SqlConnection sqlConnection;

    public RightPanel() {
        sqlConnection = new SqlConnection();
    }

    public void showAllTransactions(TableColumn firstColumn, TableColumn secondColumn, TableColumn thirdColumn, TableColumn fourthColumn,
                                    TableColumn fifthColumn, TableColumn sixthColumn, TableView tableView, Integer id, boolean mode,
                                    ComboBox comboStore, ComboBox comboCategory, ComboBox comboRate, DatePicker date1, DatePicker date2) throws SQLException, ParseException {
        firstColumn.setText("Nazwa_Sklepu");
        secondColumn.setText("Nazwa_Kategorii");
        thirdColumn.setText("Data");
        fourthColumn.setText("Kwota");
        fifthColumn.setText("Ocena");
        sixthColumn.setText("Komentarz");
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("store"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        fourthColumn.setCellValueFactory(new PropertyValueFactory<>("money"));
        fifthColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        sixthColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        getDataToTable(id, tableView, mode, comboStore, comboCategory, comboRate, date1, date2);
    }

    private void getDataToTable(Integer id, TableView tableView, boolean mode, ComboBox comboStore, ComboBox comboCategory,
                                ComboBox comboRate, DatePicker date1, DatePicker date2) throws SQLException, ParseException {
        DatePicker datePicker = new DatePicker();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select nazwa_sklepu, (select nazwa_kategorii from kategorie where id_kategorii=p.id_kategorii), data, " +
                "(select kwota from platnosci where p.id_platnosci=id_platnosci), ocena, komentarz from transakcje p where id_klienta=" + id + "order by data desc");
        while (rs.next()) {
            Date date = formatter.parse(rs.getString(3).substring(0, 10));
            if (date1.getValue() != null && date2.getValue() != null) {
                if (date2.getValue().compareTo(convertToLocalDate(date)) >= 0 && date1.getValue().compareTo(convertToLocalDate(date)) <= 0)
                    tableView.getItems().add(new Transaction(rs.getString(1), rs.getString(2), rs.getString(3).substring(0, 10), rs.getDouble(4), rs.getInt(5), rs.getString(6)));
            } else
                tableView.getItems().add(new Transaction(rs.getString(1), rs.getString(2), rs.getString(3).substring(0, 10), rs.getDouble(4), rs.getInt(5), rs.getString(6)));
        }
        rs.close();
        stmt.close();
        if (!mode)
            getCondensedData(tableView, comboStore, comboCategory, comboRate);
    }

    private void getCondensedData(TableView tableView, ComboBox comboStore, ComboBox comboCategory, ComboBox comboRate) {
        String store;
        String category;
        int rate;
        double ocena;
        double suma;
        ArrayList<Transaction> list = new ArrayList<Transaction>();


        if (comboStore.getSelectionModel().isEmpty()) store = "";
        else store = comboStore.getValue().toString().toUpperCase();
        if (comboCategory.getSelectionModel().isEmpty()) category = "";
        else category = comboCategory.getValue().toString().toUpperCase();
        if (comboRate.getSelectionModel().isEmpty()) rate = 0;
        else rate = Integer.parseInt(comboRate.getValue().toString());

        for (int i = 0; i < tableView.getItems().size(); i++) {
            Transaction transaction = (Transaction) tableView.getItems().get(i);
            if ((store.equals(transaction.getStore()) || store.equals("")) && (category.equals(transaction.getCategory()) || category.equals("")) &&
                    (rate == transaction.getRate() || rate == 0))
                list.add(transaction);
        }

        tableView.getItems().clear();
        ocena = 0;
        suma = 0;
        for (Transaction transaction : list) {
            tableView.getItems().add(transaction);
            suma += transaction.getMoney();
            ocena += transaction.getRate();
        }
        ocena = ocena / list.size();
        if (list.size() == 0)
            ocena = 0;

        suma = Math.round(suma * 100.0) / 100.0;
        ocena = Math.round(ocena * 100.0) / 100.0;

        tableView.getItems().add(new Transaction(null, null, null, null, null, null));
        tableView.getItems().add(new Transaction("----", "Podsumowanie", "-----", null, null, null));
        tableView.getItems().add(new Transaction("Suma wydana", "Liczba płatności", "Srednia ocena", null, null, null));
        tableView.getItems().add(new Transaction(String.valueOf(suma), String.valueOf(list.size()), String.valueOf(ocena), null, null, null));

    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
