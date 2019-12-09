package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.Restriction;
import sample.TransactionValidator;
import sample.WindowOperation;
import sample.sqloperation.SqlConnection;
import sample.sqloperation.SqlOperation;

import static sample.controllers.MainPanelController.id;

import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RestrictionPanelController {

    @FXML
    private TableColumn firstColumn;
    @FXML
    private TableColumn secondColumn;
    @FXML
    private TableColumn thirdColumn;
    @FXML
    private TableColumn fourthColumn;
    @FXML
    private TableView tableView;
    @FXML
    private TextField payment;
    @FXML
    private TextField comment;
    @FXML
    private ComboBox categoryController;


    private WindowOperation windowOperation;
    private SqlOperation sqlOperation;
    private SqlConnection sqlConnection;
    private TransactionValidator validator;

    public RestrictionPanelController() {
        windowOperation = new WindowOperation();
        sqlOperation = new SqlOperation();
        sqlConnection = new SqlConnection();
        validator = new TransactionValidator();
    }


    public void initialize() throws SQLException {
        sqlOperation.addCategoriesToMenu(categoryController, sqlConnection.getConnection());
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("restrictionValue"));
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("actualValue"));
        fourthColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        getDataToTable();
    }

    @FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        windowOperation.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
    }

    @FXML
    public void saveRestriction(ActionEvent actionEvent) throws SQLException {

        if (checkCorrectness()) {

            if (sqlOperation.checkCurrentRestriction(sqlConnection.getConnection(), id, categoryController))
                sqlOperation.updateRestrictionDatabase(sqlConnection.getConnection(), windowOperation, payment, comment, categoryController, id);
            else
                sqlOperation.insertRestrictionToDatabase(sqlConnection.getConnection(), windowOperation, payment, comment, categoryController, id);
            getDataToTable();
            sqlOperation.checkIfExceeded(sqlConnection.getConnection(), windowOperation, id, categoryController);
            payment.clear();
            comment.clear();
            categoryController.getSelectionModel().clearSelection();
        }
    }

    private Boolean checkCorrectness() {
        if (categoryController.getSelectionModel().isEmpty()) {
            windowOperation.warrningWindow("Błąd", "Nie podano kategorii",
                    "Wypełnij wymagane pole", Alert.AlertType.ERROR);
            return false;
        } else if (!validator.checkTexField(payment.getText().replaceAll(",", "."))) {
            windowOperation.warrningWindow("Błąd", "Nie podano poprawnej kwoty",
                    "Wypełnij wymagane pole", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    public void deleteResrtrictionButton(ActionEvent actionEvent) throws SQLException {
        if (categoryController.getSelectionModel().isEmpty())
            windowOperation.warrningWindow("Błąd", "Nie podano kategorii",
                    "Uzupełnij wymagane pole.", Alert.AlertType.ERROR);
        else if (!sqlOperation.checkCurrentRestriction(sqlConnection.getConnection(), id, categoryController)) {
            windowOperation.warrningWindow("Błąd", "Ograniczenie na daną kategorię nie istnieje",
                    "Upewnij się że podajesz poprawną kategorię.", Alert.AlertType.ERROR);
        } else {
            sqlOperation.deleteRestrictionFromDatabase(sqlConnection.getConnection(), windowOperation, id, categoryController);
            getDataToTable();
            payment.clear();
            comment.clear();
            categoryController.getSelectionModel().clearSelection();
        }
    }

    private void getDataToTable() throws SQLException {
        tableView.getItems().clear();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select (select nazwa_kategorii from kategorie where p.id_kategorii=id_kategorii), wartosc_ograniczenia, alarm" +
                ", (select sum(p.kwota) from platnosci p join transakcje t on p.id_platnosci=t.id_platnosci where t.id_klienta="
                + id + "and t.id_kategorii=p.id_kategorii and t.data between extract(year from current_date) || '/' || extract(month from current_date) || '/01' and current_date) from ograniczenia p where id_klienta=" + id);
        while (rs.next()) {
            tableView.getItems().add(new Restriction(rs.getString(1), rs.getDouble(2), rs.getDouble(4), rs.getString(3)));
        }
        rs.close();
        stmt.close();
    }

    @FXML
    public void tableViewAction(MouseEvent mouseEvent) {
        try {
            TablePosition pos = (TablePosition) tableView.getSelectionModel().getSelectedCells().get(0);

            if (!pos.toString().contains("null")) {
                int row = pos.getRow();

                // Item here is the table view type:
                Restriction item = (Restriction) tableView.getItems().get(row);

                //TableColumn col = pos.getTableColumn();

                // this gives the value in the selected cell:
                String data = (String) firstColumn.getCellObservableValue(item).getValue();
                categoryController.getSelectionModel().select(data);
                data = secondColumn.getCellObservableValue(item).getValue().toString();
                payment.setText(data);
                data = fourthColumn.getCellObservableValue(item).getValue().toString();
                comment.setText(data);
            }
        } catch (Exception e) {
            ;
        }

    }
}
