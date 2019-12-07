package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample.sqloperation.SqlConnection;
import sample.WindowOperation;
import sample.sqloperation.SqlOperation;

import java.io.IOException;
import java.sql.*;

public class NicknameController {

    @FXML
    private TextField loginLabel;
    @FXML
    private TextField passwordLabel;
    @FXML
    private TextField password2Label;
    public static String loginRegistration;
    private SqlConnection sqlConnection;
    private SqlOperation operation;


    @FXML
    private void okButtonAction(ActionEvent actionEvent) {
        RegistrationController registrationController = new RegistrationController();
        WindowOperation window_operation = new WindowOperation();
        sqlConnection = new SqlConnection();
        operation = new SqlOperation();

        if (loginLabel.getText().replaceAll(" ", "").matches("") || passwordLabel.getText().replaceAll(" ", "").matches("")) {
            window_operation.warrningWindow("Ups", "Zbyt mało danych!", "Uzupełnij wszystkie pola.", Alert.AlertType.WARNING);
        } else if (!passwordLabel.getText().equals(password2Label.getText())) {
            window_operation.warrningWindow("Ups", "Hasła nie są zgodne!", "Spróbuj ponownie.", Alert.AlertType.WARNING);
        } else {
            try {
                if(!operation.checkIfLoginExists(sqlConnection, loginLabel)) {
                    loginRegistration = loginLabel.getText();
                    operation.addUserToDatabase(registrationController, sqlConnection.getConnection(), loginLabel, passwordLabel);
                    registrationController.clearVariable();
                    window_operation.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
                }
                else window_operation.warrningWindow("Ups","Podany login już istnieje", "Podaj ponownie", Alert.AlertType.ERROR);
            } catch (SQLException | IOException ex) {
                System.out.println("Nie udało się dodać użytkownika do bazy danych");
            }
        }
    }





    @FXML
    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        WindowOperation window = new WindowOperation();
        window.goToNextWindow(actionEvent, "/resources/registration_panel.fxml", 600, 700);
    }
}
