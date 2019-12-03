package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.SqlConnection;
import sample.SqlOperation;
import sample.TransactionValidator;
import sample.WindowOperation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static sample.controllers.NicknameController.loginRegistration;
import static sample.controllers.LoginController.loginLogin;

public class MainPanelController {

    @FXML
    public ComboBox comboPayment;
    @FXML
    public TextField money;
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
    private Integer id;
    private SqlConnection sqlConnection;
    private WindowOperation windowOperation;
    private SqlOperation sqlOperation;

    public MainPanelController() throws SQLException {
        windowOperation = new WindowOperation();
        sqlOperation = new SqlOperation();
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
        addStoresToMenu();
        addCategoriesToMenu();
        addRateToMenu();
        addPaymentToMenu();
    }

    private void addStoresToMenu() throws SQLException {
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nazwa_sklepu from sklepy order by nazwa_sklepu");
        comboStore.getItems().removeAll(comboStore.getItems());
        while (rs.next()) {
            comboStore.getItems().add(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }

    private void addCategoriesToMenu() throws SQLException {
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nazwa_kategorii from kategorie order by nazwa_kategorii");
        comboCategory.getItems().removeAll(comboCategory.getItems());
        while (rs.next()) {
            comboCategory.getItems().add(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }

    private void addRateToMenu() {
        comboRate.getItems().removeAll(comboCategory.getItems());
        for (int i = 1; i < 6; i++) {
            comboRate.getItems().add(i);
        }
    }

    private void addPaymentToMenu() throws SQLException {
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT distinct sposob from platnosci order by sposob");
        comboPayment.getItems().removeAll(comboPayment.getItems());
        while (rs.next()) {
            comboPayment.getItems().add(rs.getString(1));
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
                category.clear();
            } else {
                windowOperation.warrningWindow("Błąd", "Podano błędne dane", "Podana kategoria nie istnieje lub nie została dodana przez Ciebie", Alert.AlertType.ERROR);
                category.clear();
            }
        }
    }

    @FXML
    public void confirmButton(ActionEvent actionEvent) {
        TransactionValidator transactionValidator = new TransactionValidator();
        if (transactionValidator.checkValidate(money.getText()))
            System.out.println("Siema");
    }
}
