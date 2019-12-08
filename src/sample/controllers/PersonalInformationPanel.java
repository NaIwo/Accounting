package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample.Validator;
import sample.WindowOperation;
import sample.sqloperation.SqlConnection;
import sample.sqloperation.SqlOperation;

import static sample.controllers.MainPanelController.id;

import java.io.IOException;
import java.sql.SQLException;

public class PersonalInformationPanel {

    @FXML
    private TextField nameLabel;
    @FXML
    private TextField surnameLabel;
    @FXML
    private TextField streetLabel;
    @FXML
    private TextField houseLabel;
    @FXML
    private TextField homeLabel;
    @FXML
    private TextField postcodeLabel;
    @FXML
    private TextField cityLabel;
    @FXML
    private TextField emailLabel;
    @FXML
    private TextField phoneoneLabel;
    @FXML
    private TextField phonetwoLabel;
    @FXML
    private TextField haslo;
    @FXML
    private TextField hasloConfirm;
    @FXML
    private TextField login;
    private SqlOperation sqlOperation;
    private SqlConnection sqlConnection;
    private WindowOperation windowOperation;
    private Validator validation;

    public PersonalInformationPanel() {

    }

    public void initialize() throws SQLException {
        sqlOperation = new SqlOperation();
        sqlConnection = new SqlConnection();
        windowOperation = new WindowOperation();
        sqlOperation.readUserData(sqlConnection.getConnection(), id, nameLabel, surnameLabel, streetLabel, houseLabel, homeLabel, postcodeLabel,
                cityLabel, emailLabel, phoneoneLabel, phonetwoLabel, haslo, hasloConfirm, login);
    }

    @FXML
    public void changeButtonAction(ActionEvent actionEvent) throws SQLException, IOException {
        TextField[] allTextFields = new TextField[]{nameLabel, surnameLabel, streetLabel, houseLabel, homeLabel, postcodeLabel,
                cityLabel, emailLabel, phoneoneLabel, phonetwoLabel, haslo, hasloConfirm, login};
        validation = new Validator(allTextFields);
        if(validation.checkAllValidation(windowOperation) && checkLogin())
        {
            if(sqlOperation.checkIfLoginUpdatingExists(sqlConnection, login, id))
            {
                windowOperation.warrningWindow("Ups","Podany login już istnieje", "Podaj ponownie", Alert.AlertType.ERROR);
            }
            else
            {
                sqlOperation.updateUserInformation(sqlConnection.getConnection(), windowOperation, id, allTextFields);
                windowOperation.goToNextWindow(actionEvent,"/resources/main_panel.fxml", 1200, 700);
            }
        }
    }
    private boolean checkLogin()
    {
        if(login.getText().replaceAll(" ", "").matches("") || haslo.getText().replaceAll(" ", "").matches(""))
        {
            windowOperation.warrningWindow("Ups", "Zbyt mało danych!", "Uzupełnij wszystkie pola.", Alert.AlertType.WARNING);
            return false;
        }
        else if (!haslo.getText().equals(hasloConfirm.getText())) {
            windowOperation.warrningWindow("Ups", "Hasła nie są zgodne!", "Spróbuj ponownie.", Alert.AlertType.WARNING);
                return false;
            } else
                return true;
    }
    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        WindowOperation windowOperation = new WindowOperation();
        windowOperation.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
    }
}
