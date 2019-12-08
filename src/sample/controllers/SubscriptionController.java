package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.TransactionValidator;
import sample.WindowOperation;
import sample.sqloperation.SqlConnection;
import sample.sqloperation.SqlOperation;

import static sample.controllers.MainPanelController.id;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class SubscriptionController {


    @FXML
    private ComboBox ShopPanel;
    @FXML
    private ComboBox CategoryPanel;
    @FXML
    private ComboBox PaymentPanel;
    @FXML
    private ComboBox MarkPanel;
    @FXML
    private TextField CommentPanel;


    public void initialize() throws SQLException {
        SqlOperation SQLoperation = new SqlOperation();
        SqlConnection connection = new SqlConnection();
        SQLoperation.addCategoriesToMenu(CategoryPanel, connection.getConnection());
        SQLoperation.addStoresToMenu(ShopPanel, connection.getConnection());
        addRateToMenu(MarkPanel);
        SQLoperation.addPaymentToMenu(PaymentPanel, connection.getConnection());
        if (SQLoperation.idInSubscription(id)) {
            SQLoperation.setSubscribedValude(id, ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, CommentPanel);
        }
        MarkPanel.getItems().add(0);
    }

    private void addRateToMenu(ComboBox comboBox) {
        //comboBox.getItems().removeAll(comboBox.getItems());
        for (int i = 1; i < 6; i++) {
            comboBox.getItems().add(i);
        }
    }

    public void saveChanges(ActionEvent actionEvent) throws IOException, SQLException {
        WindowOperation window = new WindowOperation();
        TransactionValidator validator = new TransactionValidator();
        SqlOperation SQLoperation = new SqlOperation();
        SqlConnection connection = new SqlConnection();

        if (validator.checkValidateForSubscription(ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, CommentPanel)) {

            if (SQLoperation.idInSubscription(id)) {
                if (!CommentPanel.getText().isEmpty()) {
                    if (CommentPanel.getText().length() > 50)
                        CommentPanel.setText(CommentPanel.getText().substring(0, 50));
                    SQLoperation.updateSubscriptionToDatabase(connection.getConnection(), window, id, ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, CommentPanel.getText().toUpperCase());
                } else
                    SQLoperation.updateSubscriptionToDatabase(connection.getConnection(), window, id, ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, null);
            } else {
                if (!CommentPanel.getText().isEmpty()) {
                    if (CommentPanel.getText().length() > 50) CommentPanel.setText(CommentPanel.getText().substring(0, 50));
                    SQLoperation.insertSubscriptionToDatabase(connection.getConnection(), window, id, ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, CommentPanel.getText().toUpperCase());
                } else
                    SQLoperation.insertSubscriptionToDatabase(connection.getConnection(), window, id, ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, null);
            }

            window.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
        }
    }

    public void backWithoutSaving(ActionEvent actionEvent) throws IOException {
        WindowOperation window = new WindowOperation();
        window.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
    }
}
