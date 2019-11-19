package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {

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
    private void backButtonAction(ActionEvent actionEvent) throws IOException {
        LoginController window = new LoginController();
        window.backButtonAction(actionEvent);
    }

    @FXML
    private void registrationButtonAction(ActionEvent actionEvent) throws IOException {
        Controller window_operation = new Controller();
        if (checkFieldFulfillment(window_operation) &&
                checkCorrectStringArguments(window_operation) &&
                checkHouseLabel(window_operation) &&
                checkEmail(window_operation) &&
                checkPostCode(window_operation) &&
                checkPhoneNumber(window_operation)) {
            window_operation.goToNextWindow(actionEvent, "/resources/nickname_panel.fxml");
        }
    }
    private boolean checkPostCode(Controller window_operation)
    {
        try
        {
            Integer.parseInt(postcodeLabel.getText().replaceAll("-", "").replaceAll(" ", ""));
        return true;
        }
        catch (NumberFormatException nfe)
        {
            window_operation.warrningWindow("Ups", "Podano niepoprawny kod pocztowy.", "Sprobuj podac ponownie.");
            return false;
        }
    }

    private boolean checkEmail(Controller window_operation) {
        if (emailLabel.getText().contains("@"))
            return true;
        else {
            window_operation.warrningWindow("Ups", "Podano niepoprawny adres email.", "Sprawdz poprawnosc i sprobuj podac ponownie.");
            return false;
        }
    }

    private boolean checkHouseLabel(Controller window_operation) {
        if (!homeLabel.getText().replaceAll(" ", "").equals("")) {
            try {
                Integer.parseInt(homeLabel.getText().replaceAll(" ", ""));
                Integer.parseInt(houseLabel.getText().replaceAll(" ", ""));

                return true;

            } catch (NumberFormatException nfe) {
                window_operation.warrningWindow("Ups", "Podano niepoprawny numer domu lub mieszkania.", "Sprawdz poprawnosc i sprobuj podac ponownie.");
                return false;
            }
        } else {
            try {
                Integer.parseInt(houseLabel.getText().replaceAll(" ", ""));

                return true;
            } catch (NumberFormatException nfe) {
                window_operation.warrningWindow("Ups", "Podano niepoprawny numer domu.", "Sprobuj podac ponownie.");
                return false;
            }
        }

    }

    private boolean checkCorrectStringArguments(Controller window_operation) {
        if (!nameLabel.getText().replaceAll("[0-9]", "").equals(nameLabel.getText()) ||
                !surnameLabel.getText().replaceAll("[0-9]", "").equals(surnameLabel.getText()) ||
                !streetLabel.getText().replaceAll("[0-9]", "").equals(streetLabel.getText()) ||
                !cityLabel.getText().replaceAll("[0-9]", "").equals(cityLabel.getText())) {

            window_operation.warrningWindow("Ups", "Podano błędne wartości pól tekstowych", "Spróbuj podać ponownie.");
            return false;
        } else
            return true;
    }

    private boolean checkFieldFulfillment(Controller window_operation) {
        if (nameLabel.getText().replaceAll(" ", "").matches("") ||
                surnameLabel.getText().replaceAll(" ", "").matches("") ||
                streetLabel.getText().replaceAll(" ", "").matches("") ||
                houseLabel.getText().replaceAll(" ", "").matches("") ||
                postcodeLabel.getText().replaceAll(" ", "").matches("") ||
                cityLabel.getText().replaceAll(" ", "").matches("") ||
                emailLabel.getText().replaceAll(" ", "").matches("")) {

            window_operation.warrningWindow("Ups", "Nie wypelniono koniecznych pol.", "Sprawdź poprawność danych i uzupełnij ponownie.");
            return false;
        } else
            return true;
    }

    private boolean checkPhoneNumber(Controller window_operation) {
        try {
            if (!phoneoneLabel.getText().replaceAll(" ", "").matches(""))
                Integer.parseInt(phoneoneLabel.getText());
            if (!phonetwoLabel.getText().replaceAll(" ", "").matches(""))
                Integer.parseInt(phonetwoLabel.getText());

            return true;

        } catch (NumberFormatException nfe) {
            window_operation.warrningWindow("Ups", "Podano błędny numer telefonu!", "Spróbuj podać poprawny numer telefonu.");
            return false;
        }
    }



}