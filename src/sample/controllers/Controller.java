package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.WindowOperation;

import java.io.IOException;

public class Controller {

    @FXML
    public Button loginButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button registerButton;

    private WindowOperation newWindow = new WindowOperation();



    @FXML
    private void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void goToRegistration(ActionEvent actionEvent) throws IOException {
        newWindow.goToNextWindow(actionEvent, "/resources/registration_panel.fxml", 600, 700);
    }

    @FXML
    private void goToLogin(ActionEvent actionEvent) throws IOException {
        newWindow.goToNextWindow(actionEvent, "/resources/log_panel.fxml", 600, 700);
    }
}
