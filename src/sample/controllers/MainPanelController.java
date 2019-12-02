package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.SqlConnection;
import sample.SqlOperation;
import sample.WindowOperation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static sample.controllers.NicknameController.loginRegistration;
import static sample.controllers.LoginController.loginLogin;

public class MainPanelController {

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

    public void initialize() {
        comboCategory.getItems().removeAll(comboCategory.getItems());
        comboCategory.getItems().add("30");
        comboCategory.getItems().add("40");
        comboCategory.getSelectionModel().select("30");
    }

    @FXML
    private void addStore(ActionEvent actionEvent) throws SQLException {
        if (!store.getText().replaceAll(" ", "").equals("")) {
            if (!sqlOperation.checkIfStoreExists(sqlConnection.getConnection(), store))
                sqlOperation.addStoreToDatabase(sqlConnection.getConnection(), store, id, windowOperation);
            else {
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
        if(!store.getText().replaceAll(" ", "").equals("")) {
            if(sqlOperation.checkIfStoreWasAddedByUser(sqlConnection.getConnection(), store, id)) {
                sqlOperation.removeStore(sqlConnection.getConnection(), store, windowOperation);
            }
            else {
                windowOperation.warrningWindow("Błąd", "Podano błędne dane", "Podany sklep nie istnieje lub nie został dodany przez Ciebie", Alert.AlertType.ERROR);
                store.clear();
            }
        }
    }

    @FXML
    public void removeCategory(ActionEvent actionEvent) throws SQLException {
        if(!category.getText().replaceAll(" ", "").equals("")) {
            if(sqlOperation.checkIfCategoryWasAddedByUser(sqlConnection.getConnection(), category, id)) {
                sqlOperation.removeCategory(sqlConnection.getConnection(), category, windowOperation);
            }
            else {
                windowOperation.warrningWindow("Błąd", "Podano błędne dane", "Podana kategoria nie istnieje lub nie została dodana przez Ciebie", Alert.AlertType.ERROR);
                category.clear();
            }
        }
    }
}
