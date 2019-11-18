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

    private void goToNextWindow(ActionEvent actionEvent, String path) throws IOException {
        Parent log_root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(log_root, 600, 700);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void backButtonAction(ActionEvent actionEvent) throws IOException {
        goToNextWindow(actionEvent, "/resources/sample.fxml");
    }
}
