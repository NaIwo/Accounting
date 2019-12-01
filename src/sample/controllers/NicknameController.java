package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import java.io.IOException;

public class NicknameController {

    @FXML
    private TextField loginLabel;
    @FXML
    private TextField passwordLabel;
    @FXML
    private TextField password2Label;


    @FXML
    private void okButtonAction(ActionEvent actionEvent) {

        Controller window_operation = new Controller();

        if (loginLabel.getText().replaceAll(" ", "").matches("") || passwordLabel.getText().replaceAll(" ", "").matches("")) {
            window_operation.warrningWindow("Ups", "Zbyt mało danych!", "Uzupełnij wszystkie pola.");
        } else if (!passwordLabel.getText().equals(password2Label.getText())) {
            window_operation.warrningWindow("Ups", "Hasła nie są zgodne!", "Spróbuj ponownie.");
        } else {
            System.out.println("w końcu kurła");
        }
    }

    @FXML
    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        Controller window = new Controller();
        window.goToNextWindow(actionEvent, "/resources/registration_panel.fxml",600,700);
    }
}
