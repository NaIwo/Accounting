package sample;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.sqloperation.SqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RightPanel {

    private SqlConnection sqlConnection;

    public RightPanel() {
        sqlConnection = new SqlConnection();
    }

    public void showAllTransactions(TableColumn firstColumn, TableColumn secondColumn, TableColumn thirdColumn, TableColumn fourthColumn,
                                    TableColumn fifthColumn, TableView tableView, Integer id) throws SQLException {
        firstColumn.setText("Nazwa_Sklepu");
        secondColumn.setText("Nazwa_Kategorii");
        thirdColumn.setText("Data");
        fourthColumn.setText("Kwota");
        fifthColumn.setText("Ocena");
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("store"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        fourthColumn.setCellValueFactory(new PropertyValueFactory<>("money"));
        fifthColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        getDataToTable(id, tableView);
    }

    private void getDataToTable(Integer id, TableView tableView) throws SQLException {
        Transaction transaction = new Transaction();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select nazwa_sklepu, (select nazwa_kategorii from kategorie where id_kategorii=p.id_kategorii), data, " +
                "(select kwota from platnosci where p.id_platnosci=id_platnosci), ocena from transakcje p where id_klienta=" + id + "order by data desc");
        while (rs.next()) {
            transaction.set(rs.getString(1), rs.getString(2), rs.getString(3).substring(0, 10), rs.getDouble(4), rs.getInt(5));
            tableView.getItems().add(transaction);
        }
        rs.close();
        stmt.close();
    }
}
