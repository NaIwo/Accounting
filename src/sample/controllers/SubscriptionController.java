package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.TransactionValidator;
import sample.WindowOperation;
import sample.sqloperation.SqlConnection;
import sample.sqloperation.SqlOperation;

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
    private int id;


    public void initialize() throws SQLException {
        MainPanelController operation = new MainPanelController();
        SqlOperation SQLoperation = new SqlOperation();

        this.id = operation.getID();
        operation.addCategoriesToMenu(CategoryPanel);
        operation.addStoresToMenu(ShopPanel);
        operation.addRateToMenu(MarkPanel);
        operation.addPaymentToMenu(PaymentPanel);
        if (SQLoperation.idInSubscription(id)) {
            SQLoperation.setSubscribedValude(id, ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, CommentPanel);
        }
    }

    public void saveChanges(ActionEvent actionEvent) throws IOException, SQLException {
        WindowOperation window = new WindowOperation();
        TransactionValidator validator = new TransactionValidator();
        SqlOperation SQLoperation = new SqlOperation();
        SqlConnection connection = new SqlConnection();

        if (validator.checkValidateForSubscription(ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, CommentPanel)) {

            if (SQLoperation.idInSubscription(id)) {
                SQLoperation.updateSubscriptionToDatabase(connection.getConnection(), window, id, ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, CommentPanel.getText().toUpperCase());
            } else
            {
                if(!CommentPanel.getText().isEmpty())
                    SQLoperation.insertSubscriptionToDatabase(connection.getConnection(), window, id, ShopPanel, CategoryPanel, MarkPanel, PaymentPanel, CommentPanel.getText().toUpperCase());
                else
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
