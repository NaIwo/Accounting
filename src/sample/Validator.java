package sample;

import javafx.scene.control.TextField;
import sample.controllers.Controller;

public class Validator {

    private TextField nameLabel;
    private TextField surnameLabel;
    private TextField streetLabel;
    private TextField houseLabel;
    private TextField homeLabel;
    private TextField postcodeLabel;
    private TextField cityLabel;
    private TextField emailLabel;
    private TextField phoneoneLabel;
    private TextField phonetwoLabel;

    public Validator(TextField[] allTextFields) {
        this.nameLabel = allTextFields[0];
        this.surnameLabel = allTextFields[1];
        this.streetLabel = allTextFields[2];
        this.houseLabel = allTextFields[3];
        this.homeLabel = allTextFields[4];
        this.postcodeLabel = allTextFields[5];
        this.cityLabel = allTextFields[6];
        this.emailLabel = allTextFields[7];
        this.phoneoneLabel = allTextFields[8];
        this.phonetwoLabel = allTextFields[9];
    }

    public boolean checkAllValidation(WindowOperation window_operation)
    {
        if (checkFieldFulfillment(window_operation) &&
                checkCorrectStringArguments(window_operation) &&
                checkHouseLabel(window_operation) &&
                checkEmail(window_operation) &&
                checkPostCode(window_operation) &&
                checkPhoneNumber(window_operation))
            return true;
        else
            return false;
    }

    private boolean checkPostCode(WindowOperation window_operation) {
        try {
            String text_value = postcodeLabel.getText().replaceAll("-", "").replaceAll(" ", "");
            Integer.parseInt(text_value);
            if(text_value.length() == 5)
                return true;
            else
            {
                window_operation.warrningWindow("Ups", "Podano niepoprawny kod pocztowy.", "Sprobuj podac ponownie.");
                return false;
            }

        } catch (NumberFormatException nfe) {
            window_operation.warrningWindow("Ups", "Podano niepoprawny kod pocztowy.", "Sprobuj podac ponownie.");
            return false;
        }
    }


    private boolean checkEmail(WindowOperation window_operation) {
        if (emailLabel.getText().matches("^.+@{1}.{1,9}\\.{1}.+$"))
            return true;
        else {
            window_operation.warrningWindow("Ups", "Podano niepoprawny adres email.", "Sprawdz poprawnosc i sprobuj podac ponownie.");
            return false;
        }
    }

    private boolean checkHouseLabel(WindowOperation window_operation) {
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

    private boolean checkCorrectStringArguments(WindowOperation window_operation) {
        if (!nameLabel.getText().replaceAll("[0-9]", "").equals(nameLabel.getText()) ||
                !surnameLabel.getText().replaceAll("[0-9]", "").equals(surnameLabel.getText()) ||
                !streetLabel.getText().replaceAll("[0-9]", "").equals(streetLabel.getText()) ||
                !cityLabel.getText().replaceAll("[0-9]", "").equals(cityLabel.getText())) {

            window_operation.warrningWindow("Ups", "Podano błędne wartości pól tekstowych", "Spróbuj podać ponownie.");
            return false;
        } else
            return true;
    }

    private boolean checkFieldFulfillment(WindowOperation window_operation) {
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

    private boolean checkPhoneNumber(WindowOperation window_operation) {
        try {
            if (validatePhoneNumber(window_operation, phoneoneLabel)) return false;
            if (validatePhoneNumber(window_operation, phonetwoLabel)) return false;

            return true;

        } catch (NumberFormatException nfe) {
            window_operation.warrningWindow("Ups", "Podano błędny numer telefonu!", "Spróbuj podać poprawny numer telefonu.");
            return false;
        }
    }

    private boolean validatePhoneNumber(WindowOperation window_operation, TextField phoneoneLabel) {
        if (!phoneoneLabel.getText().replaceAll(" ", "").matches(""))
        {
            Integer.parseInt(phoneoneLabel.getText());
            if(phoneoneLabel.getText().length() != 9)
            {
                window_operation.warrningWindow("Ups", "Podano błędny numer telefonu!", "Spróbuj podać poprawny numer telefonu.");
                return true;
            }
        }
        return false;
    }
}
