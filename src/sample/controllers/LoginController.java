package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button closeButton;



    @FXML
    private void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        Controller  window = new Controller();
        window.goToNextWindow(actionEvent, "/resources/sample.fxml");
    }

    @FXML
    private void loginButtonAction(ActionEvent actionEvent){
        System.out.println("Zaloguj mnie");
    }
}
