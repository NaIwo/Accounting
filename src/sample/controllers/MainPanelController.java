package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.*;
import sample.sqloperation.SqlConnection;
import sample.sqloperation.SqlOperation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static sample.controllers.NicknameController.loginRegistration;
import static sample.controllers.LoginController.loginLogin;

public class MainPanelController {

    @FXML
    public CheckBox checkBox;
    @FXML
    public TableColumn sixthColumn;
    @FXML
    public ComboBox comboCategory1;
    @FXML
    public ComboBox comboRate1;
    @FXML
    public ComboBox comboPayment1;
    @FXML
    public ComboBox comboStore1;
    @FXML
    private TableView tableView;
    @FXML
    private ComboBox comboPayment;
    @FXML
    private TextField money;
    @FXML
    private DatePicker dateDate;
    @FXML
    private TableColumn firstColumn;
    @FXML
    private TableColumn secondColumn;
    @FXML
    private TableColumn thirdColumn;
    @FXML
    private TableColumn fourthColumn;
    @FXML
    private TableColumn fifthColumn;
    @FXML
    private ComboBox comboRate;
    @FXML
    private ComboBox comboStore;
    @FXML
    private ComboBox comboCategory;
    @FXML
    private TextField category;
    @FXML
    private TextField store;
    @FXML
    private TextField comment;

    private Integer id;
    private SqlConnection sqlConnection;
    private WindowOperation windowOperation;
    private SqlOperation sqlOperation;
    private RightPanel rightPanel;

    public MainPanelController() throws SQLException {
        windowOperation = new WindowOperation();
        sqlOperation = new SqlOperation();
        rightPanel = new RightPanel();
        String login = loginRegistration != null ? loginRegistration : loginLogin;
        sqlConnection = new SqlConnection();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("Select id_klienta from klienci where login='" + login + "'");
        rs.next();
        id = rs.getInt(1);
        rs.close();
        stmt.close();
    }

    public void initialize() throws SQLException {
        addStoresToMenu(comboStore);
        addStoresToMenu(comboStore1);
        addCategoriesToMenu(comboCategory);
        addCategoriesToMenu(comboCategory1);
        addRateToMenu(comboRate);
        addRateToMenu(comboRate1);
        addPaymentToMenu(comboPayment);
        addPaymentToMenu(comboPayment1);
    }

    private void addStoresToMenu(ComboBox comboBox) throws SQLException {
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nazwa_sklepu, ocena from sklepy order by nazwa_sklepu");
        comboBox.getItems().removeAll(comboBox.getItems());
        while (rs.next()) {
            comboBox.getItems().add(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }

    private void addCategoriesToMenu(ComboBox comboBox) throws SQLException {
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nazwa_kategorii from kategorie order by nazwa_kategorii");
        comboBox.getItems().removeAll(comboBox.getItems());
        while (rs.next()) {
            comboBox.getItems().add(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }

    private void addRateToMenu(ComboBox comboBox) {
        comboBox.getItems().removeAll(comboBox.getItems());
        for (int i = 1; i < 6; i++) {
            comboBox.getItems().add(i);
        }
    }

    private void addPaymentToMenu(ComboBox comboBox) throws SQLException {
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT distinct sposob from platnosci order by sposob");
        comboBox.getItems().removeAll(comboBox.getItems());
        while (rs.next()) {
            comboBox.getItems().add(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }

    @FXML
    private void addStore(ActionEvent actionEvent) throws SQLException {
        if (!store.getText().replaceAll(" ", "").equals("")) {
            if (!sqlOperation.checkIfStoreExists(sqlConnection.getConnection(), store)) {
                sqlOperation.addStoreToDatabase(sqlConnection.getConnection(), store, id, windowOperation);
                comboStore.getItems().add(store.getText().toUpperCase());
                comboStore1.getItems().add(store.getText().toUpperCase());
                store.clear();
            } else {
                windowOperation.warrningWindow("Błąd", "Podany sklep już istnieje", "Podaj ponownie nazwę sklepu", Alert.AlertType.ERROR);
                store.clear();
            }
        }
    }

    @FXML
    private void addCategory(ActionEvent actionEvent) throws SQLException {
        if (!category.getText().replaceAll(" ", "").equals("")) {
            if (!sqlOperation.checkIfCategoryExists(sqlConnection.getConnection(), category)) {
                sqlOperation.addCategoryToDatabase(sqlConnection.getConnection(), category, id, windowOperation);
                comboCategory.getItems().add(category.getText().toUpperCase());
                comboCategory1.getItems().add(category.getText().toUpperCase());
                category.clear();
            } else {
                windowOperation.warrningWindow("Błąd", "Podana kategoria już istnieje", "Podaj ponownie nazwę kategorii", Alert.AlertType.ERROR);
                category.clear();
            }
        }
    }

    @FXML
    public void closeButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        loginLogin = null;
        loginRegistration = null;
        sqlConnection.closeConnection();
        windowOperation.goToNextWindow(actionEvent, "/resources/sample.fxml", 600, 700);
    }

    @FXML
    public void removeStore(ActionEvent actionEvent) throws SQLException {
        if (!store.getText().replaceAll(" ", "").equals("")) {
            if (sqlOperation.checkIfStoreWasAddedByUser(sqlConnection.getConnection(), store, id)) {
                sqlOperation.removeStore(sqlConnection.getConnection(), store, windowOperation);
                comboStore.getItems().remove(store.getText().toUpperCase());
                comboStore1.getItems().remove(store.getText().toUpperCase());
                store.clear();
            } else {
                windowOperation.warrningWindow("Błąd", "Podano błędne dane", "Podany sklep nie istnieje lub nie został dodany przez Ciebie", Alert.AlertType.ERROR);
                store.clear();
            }
        }
    }

    @FXML
    public void removeCategory(ActionEvent actionEvent) throws SQLException {
        if (!category.getText().replaceAll(" ", "").equals("")) {
            if (sqlOperation.checkIfCategoryWasAddedByUser(sqlConnection.getConnection(), category, id)) {
                sqlOperation.removeCategory(sqlConnection.getConnection(), category, windowOperation);
                comboCategory.getItems().remove(category.getText().toUpperCase());
                comboCategory1.getItems().remove(category.getText().toUpperCase());
                category.clear();
            } else {
                windowOperation.warrningWindow("Błąd", "Podano błędne dane", "Podana kategoria nie istnieje lub nie została dodana przez Ciebie", Alert.AlertType.ERROR);
                category.clear();
            }
        }
    }

    @FXML
    public void confirmButton(ActionEvent actionEvent) throws SQLException {
        TransactionValidator transactionValidator = new TransactionValidator();
        if (transactionValidator.checkValidate(money.getText().replaceAll(",", "."), comboStore, comboCategory, comboRate, comboPayment, dateDate)) {
            sqlOperation.addTransaction(sqlConnection.getConnection(), windowOperation, id, money.getText(),
                    comboStore.getValue().toString().toUpperCase().split(" ")[0], comboCategory.getValue().toString().toUpperCase(),
                    dateDate.getValue().toString(), comboPayment.getValue().toString().toUpperCase(),
                    comboRate.getValue().toString(), comment.getText().toUpperCase());
            clearAllFields();
            addStoresToMenu(comboStore);
            addStoresToMenu(comboStore1);
            checkBoxAction();
            //TODO
            //Sprawdzenie ograniczen i wyswietlnie komunikatu
            //Dodanie subskrypcji
            //Edycja danych
        }
    }

    private void clearAllFields() {
        money.clear();
        comboStore.getSelectionModel().clearSelection();
        comboCategory.getSelectionModel().clearSelection();
        dateDate.getEditor().clear();
        comboPayment.getSelectionModel().clearSelection();
        comment.clear();
    }

    @FXML
    public void checkBoxAction() throws SQLException {
        tableView.getItems().clear();
        rightPanel.showAllTransactions(firstColumn, secondColumn, thirdColumn, fourthColumn, fifthColumn, sixthColumn, tableView, id, checkBox.isSelected());
    }

}
