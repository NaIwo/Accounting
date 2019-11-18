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
import java.sql.Statement;

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
    private void goToLogin(ActionEvent actionEvent) throws IOException {
        Parent log_root = FXMLLoader.load(getClass().getResource("/resources/log_panel.fxml"));
        Scene scene = new Scene(log_root, 600, 700);
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
