package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.WindowOperation;

import java.io.IOException;

public class PersonalInformationPanel {

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


    public PersonalInformationPanel()
    {

    }

    public void initialize()
    {

    }

    @FXML
    public void changeButtonAction()
    {

    }

    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        WindowOperation windowOperation = new WindowOperation();
        windowOperation.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
    }
}
