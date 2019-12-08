package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.*;
import sample.sqloperation.SqlConnection;
import sample.sqloperation.SqlOperation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;

import static sample.controllers.NicknameController.loginRegistration;
import static sample.controllers.LoginController.loginLogin;

//TODO
// poprawa menu z wyborem oceny,
// indeksy
// modyfikacja danych
// przy cofaniu rejestracyjne wartości
public class MainPanelController {

    @FXML
    public CheckBox checkBox;
    @FXML
    public TableColumn sixthColumn;
    @FXML
    private DatePicker date1;
    @FXML
    private DatePicker date2;
    @FXML
    private ComboBox comboCategory1;
    @FXML
    private ComboBox comboRate1;
    @FXML
    private ComboBox comboStore1;
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

    public static Integer id;
    private SqlConnection sqlConnection;
    private WindowOperation windowOperation;
    private LocalDate localDate;
    private SqlOperation sqlOperation;
    private RightPanel rightPanel;
    private boolean last = true;

    public MainPanelController() throws SQLException {
        windowOperation = new WindowOperation();
        sqlOperation = new SqlOperation();
        localDate = LocalDate.now();
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


    public void initialize() throws SQLException, ParseException {
        sqlOperation.addStoresToMenu(comboStore, sqlConnection.getConnection());
        sqlOperation.addStoresToMenu(comboStore1, sqlConnection.getConnection());
        sqlOperation.addCategoriesToMenu(comboCategory, sqlConnection.getConnection());
        sqlOperation.addCategoriesToMenu(comboCategory1, sqlConnection.getConnection());
        comboRate1.getItems().add(0);
        addRateToMenu(comboRate);
        addRateToMenu(comboRate1);
        sqlOperation.addPaymentToMenu(comboPayment, sqlConnection.getConnection());
        if(checkBox.isSelected())
        {
            comboCategory1.setDisable(true);
            comboStore1.setDisable(true);
            comboRate1.setDisable(true);
        }
        checkBoxAction();
        if (sqlOperation.idInSubscription(id))
            sqlOperation.setSubscribedValude(id, comboStore, comboCategory, comboRate, comboPayment, comment);
        dateDate.setValue(localDate);
    }

    private void addRateToMenu(ComboBox comboBox) {
        //comboBox.getItems().removeAll(comboBox.getItems());
        for (int i = 1; i < 6; i++) {
            comboBox.getItems().add(i);
        }
    }

    @FXML
    private void addStore() throws SQLException {
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
    private void addCategory() throws SQLException {
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

        id = null;
        loginLogin = null;
        loginRegistration = null;
        sqlConnection.closeConnection();
        windowOperation.goToNextWindow(actionEvent, "/resources/sample.fxml", 600, 700);
    }

    @FXML
    public void removeStore() throws SQLException {
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
    public void removeCategory() throws SQLException {
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
    public void confirmButton() throws SQLException, ParseException {
        TransactionValidator transactionValidator = new TransactionValidator();


        if (transactionValidator.checkValidate(money.getText().replaceAll(",", "."), comboStore, comboCategory, comboRate, comboPayment, dateDate)) {
            sqlOperation.addTransaction(sqlConnection.getConnection(), windowOperation, id, money.getText(),
                    comboStore.getValue().toString().toUpperCase().split(" ")[0], comboCategory.getValue().toString().toUpperCase(),
                    dateDate.getValue().toString(), comboPayment.getValue().toString().toUpperCase(),
                    comboRate.getValue().toString(), comment.getText().toUpperCase());


            sqlOperation.checkIfExceeded(sqlConnection.getConnection(), windowOperation, id, comboCategory);

            clearAllFields();
            checkBoxAction();
            dateDate.setValue(localDate);
            if (sqlOperation.idInSubscription(id))
                sqlOperation.setSubscribedValude(id, comboStore, comboCategory, comboRate, comboPayment, comment);


        }
    }

    private void clearAllFields() {
        money.clear();
        comboStore.getSelectionModel().clearSelection();
        comboCategory.getSelectionModel().clearSelection();
        dateDate.getEditor().clear();
        dateDate.setValue(null);
        comboPayment.getSelectionModel().clearSelection();
        comment.clear();
    }

    @FXML
    private void checkBoxAction() throws SQLException, ParseException {
        if(checkBox.isSelected())
        {
            comboCategory1.setDisable(true);
            comboStore1.setDisable(true);
            comboRate1.setDisable(true);
            if(!last) {
                date1.getEditor().clear();
                date2.getEditor().clear();
                date1.setValue(null);
                date2.setValue(null);
                last = true;
            }
        }
        else
        {
            comboCategory1.setDisable(false);
            comboStore1.setDisable(false);
            comboRate1.setDisable(false);
            last = false;
        }
        tableView.getItems().clear();
        rightPanel.showAllTransactions(firstColumn, secondColumn, thirdColumn, fourthColumn, fifthColumn, sixthColumn, tableView, id, checkBox.isSelected(),
                comboStore1, comboCategory1, comboRate1, date1, date2);
    }

    @FXML
    public void addSubscription(ActionEvent actionEvent) throws IOException {
        windowOperation.goToNextWindow(actionEvent, "/resources/subscription.fxml", 600, 700);
    }


    @FXML
    public void editPersonalData(ActionEvent actionEvent) throws IOException {
        windowOperation.goToNextWindow(actionEvent, "/resources/personalInformation_panel.fxml", 600, 700);
    }

    public void comboCategoryAction() throws SQLException, ParseException {
        checkBoxAction();
    }

    @FXML
    public void comboStoreAction() throws SQLException, ParseException {
        checkBoxAction();
    }

    @FXML
    public void comboRateAction() throws SQLException, ParseException {
        checkBoxAction();
    }

    public void addRestriction(ActionEvent actionEvent) throws IOException {
        windowOperation.goToNextWindow(actionEvent, "/resources/restriction_panel.fxml", 600, 700);
    }

    @FXML
    private void date1Action() throws SQLException, ParseException {
        if(date2.getValue() != null && date1.getValue() != null && checkDateValidation()) checkBoxAction();
    }

    @FXML
    private void date2Action() throws SQLException, ParseException {
        if(date1.getValue() != null && date2.getValue() != null && checkDateValidation()) checkBoxAction();
    }

    private boolean checkDateValidation() {
        if(date2.getValue().compareTo(date1.getValue()) >= 0) return true;
        else {
            date1.getEditor().clear();
            date2.getEditor().clear();
            windowOperation.warrningWindow("Bład", "Niepoprawnie wybrane daty", "Popraw wybrane przez Ciebie daty", Alert.AlertType.ERROR);
            date1.setValue(null);
            date2.setValue(null);
            return false;
        }
    }
}