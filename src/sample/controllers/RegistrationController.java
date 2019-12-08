package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Validator;
import sample.WindowOperation;

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

    private static String name;
    private static String surname;
    private static String street;
    private static Integer house;
    private static Integer home;
    private static Integer postalcode;
    private static String city;
    private static String email;
    private static Integer phoneone;
    private static Integer phonetwo;

    public void initialize()
    {
        if(name != null)
        {
            nameLabel.setText(name);
            surnameLabel.setText(surname);
            streetLabel.setText(street);
            houseLabel.setText(house.toString());
            if(home != null) homeLabel.setText(home.toString());
            postcodeLabel.setText(postalcode.toString().substring(0,2) + "-" + postalcode.toString().substring(2,postalcode.toString().length()));
            cityLabel.setText(city);
            emailLabel.setText(email);
            if(phoneone != null) phoneoneLabel.setText(phoneone.toString());
            if(phonetwo != null) phonetwoLabel.setText(phonetwo.toString());
        }
    }

    public void clearVariable()
    {
        name = null;
        surname = null;
        street = null;
        house = null;
        home = null;
        postalcode = null;
        city = null;
        email = null;
        phoneone = null;
        phonetwo = null;
    }

    @FXML
    private void backButtonAction(ActionEvent actionEvent) throws IOException {
        clearVariable();
        LoginController window = new LoginController();
        window.backButtonAction(actionEvent);
    }

    @FXML
    private void registrationButtonAction(ActionEvent actionEvent) throws IOException {
        WindowOperation window_operation = new WindowOperation();
        TextField[] allTextFields = new TextField[]{nameLabel, surnameLabel, streetLabel, houseLabel, homeLabel, postcodeLabel,
                cityLabel, emailLabel, phoneoneLabel, phonetwoLabel};
        Validator validate = new Validator(allTextFields);
        if (validate.checkAllValidation(window_operation)) {
            assignVariables();
            window_operation.goToNextWindow(actionEvent, "/resources/nickname_panel.fxml", 600, 700);
        }
    }

    private void assignVariables() {
        name = nameLabel.getText();
        surname = surnameLabel.getText();
        street = streetLabel.getText();
        house = Integer.parseInt(houseLabel.getText());
        home = homeLabel.getText().replaceAll(" ", "").matches("") ? null : Integer.parseInt(homeLabel.getText());
        postalcode = Integer.parseInt(postcodeLabel.getText().replaceAll("-", "").replaceAll(" ", ""));
        city = cityLabel.getText();
        email = emailLabel.getText();
        phoneone = phoneoneLabel.getText().replaceAll(" ", "").matches("") ? null : Integer.parseInt(phoneoneLabel.getText());
        phonetwo = phonetwoLabel.getText().replaceAll(" ", "").matches("") ? null : Integer.parseInt(phonetwoLabel.getText());
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getStreet() {
        return street;
    }

    public Integer getHouse() {
        return house;
    }

    public Integer getHome() {
        return home;
    }

    public Integer getPostalcode() {
        return postalcode;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPhoneone() {
        return phoneone;
    }

    public Integer getPhonetwo() {
        return phonetwo;
    }
}
