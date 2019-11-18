package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    @FXML
    public Button loginButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button registerButton;

    @FXML
    private void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void goToRegistration(ActionEvent actionEvent) {
        System.out.println("Idz do rejestracji!");
    }
    @FXML
    public void goToLogin(ActionEvent actionEvent) {
        System.out.println("Idż do logowania!");
    }
}
