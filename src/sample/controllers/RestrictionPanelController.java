package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

public class RestrictionPanelController {

    @FXML
    private TextField payment;
    @FXML
    private TextField comment;
    @FXML
    private ComboBox categoryController;


    private WindowOperation windowOperation;
    private SqlOperation sqlOperation;
    private SqlConnection sqlConnection;
    private TransactionValidator validator;

    public RestrictionPanelController()
    {
        windowOperation = new WindowOperation();
        sqlOperation = new SqlOperation();
        sqlConnection = new SqlConnection();
        validator = new TransactionValidator();
    }


    public void initialize() throws SQLException {
        sqlOperation.addCategoriesToMenu(categoryController, sqlConnection.getConnection());
    }

    @FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        windowOperation.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
    }

    @FXML
    public void saveRestriction(ActionEvent actionEvent) throws SQLException {

        if(checkCorrectness())
        {

            if(sqlOperation.checkCurrentRestriction(sqlConnection.getConnection(), id, categoryController))
                sqlOperation.updateRestrictionDatabase(sqlConnection.getConnection(), windowOperation, payment, comment, categoryController, id);
            else
                sqlOperation.insertRestrictionToDatabase(sqlConnection.getConnection(), windowOperation, payment, comment, categoryController, id);

            sqlOperation.checkIfExceeded(sqlConnection.getConnection(), windowOperation, id, categoryController);
        }
    }

    private Boolean checkCorrectness()
    {
        if(categoryController.getSelectionModel().isEmpty())
        {
            windowOperation.warrningWindow("Błąd", "Nie podano kategorii",
                    "Wypełnij wymagane pole", Alert.AlertType.ERROR);
            return false;
        }
        else if (!validator.checkTexField(payment.getText().replaceAll(",", ".")))
        {
            windowOperation.warrningWindow("Błąd", "Nie podano poprawnej kwoty",
                    "Wypełnij wymagane pole", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    public void deleteResrtrictionButton(ActionEvent actionEvent) throws SQLException {
        if(!sqlOperation.checkCurrentRestriction(sqlConnection.getConnection(), id, categoryController))
            windowOperation.warrningWindow("Błąd", "Ograniczenie na daną kategorię nie isinieje",
                    "Upewnij się że podajesz poprawną kategorię.", Alert.AlertType.ERROR);
        else if(categoryController.getSelectionModel().isEmpty()) {
            windowOperation.warrningWindow("Błąd", "Nie podano kategorii",
                    "Uzupełnij wymagane pole.", Alert.AlertType.ERROR);
        }
        else
            sqlOperation.deleteRestrictionFromDatabase(sqlConnection.getConnection(), windowOperation, id, categoryController);

    }
}
