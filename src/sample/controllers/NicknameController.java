package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sample.sqloperation.SqlConnection;
import sample.WindowOperation;

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


    @FXML
    private void okButtonAction(ActionEvent actionEvent) {

        WindowOperation window_operation = new WindowOperation();
        if (loginLabel.getText().replaceAll(" ", "").matches("") || passwordLabel.getText().replaceAll(" ", "").matches("")) {
            window_operation.warrningWindow("Ups", "Zbyt mało danych!", "Uzupełnij wszystkie pola.", Alert.AlertType.WARNING);
        } else if (!passwordLabel.getText().equals(password2Label.getText())) {
            window_operation.warrningWindow("Ups", "Hasła nie są zgodne!", "Spróbuj ponownie.", Alert.AlertType.WARNING);
        } else {
            try {
                if(!checkIfLoginExists()) {
                    addUserToDatabase();
                    window_operation.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
                }
                else window_operation.warrningWindow("Ups","Podany login już istnieje", "Podaj ponownie", Alert.AlertType.ERROR);
            } catch (SQLException | IOException ex) {
                System.out.println("Nie udało się dodać użytkownika do bazy danych");
            }
        }
    }

    private void addUserToDatabase() throws SQLException {
        loginRegistration = loginLabel.getText();
        RegistrationController registrationController = new RegistrationController();
        CallableStatement stmt = sqlConnection.getConnection().prepareCall("{call NOWYUSER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

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

    private boolean checkIfLoginExists() throws SQLException {
        boolean result;
        sqlConnection = new SqlConnection();
        sqlConnection.connect();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("Select login from dane_logowania where login='" + loginLabel.getText() + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    @FXML
    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        WindowOperation window = new WindowOperation();
        window.goToNextWindow(actionEvent, "/resources/registration_panel.fxml", 600, 700);
    }
}
