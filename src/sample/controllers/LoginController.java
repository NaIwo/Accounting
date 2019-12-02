package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.SqlConnection;
import sample.WindowOperation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    @FXML
    private TextField loginLabel;
    @FXML
    private PasswordField passwordLabel;
    @FXML
    private Button closeButton;


    @FXML
    private void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void backButtonAction(ActionEvent actionEvent) throws IOException {
        WindowOperation window = new WindowOperation();
        window.goToNextWindow(actionEvent, "/resources/sample.fxml", 600, 700);
    }

    @FXML
    private void loginButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        SqlConnection sqlConnection = new SqlConnection();
        if (sqlConnection.connect()) {
            WindowOperation window = new WindowOperation();
            if (checkFields() && readData(sqlConnection.getConnection())) {
                window.goToNextWindow(actionEvent, "/resources/main_panel.fxml", 1200, 700);
            } else {
                window.warrningWindow("Niepoprawne dane", "Podano niepoprawne dane logowania", "Sprawdz poprawnosc wprowadzonych danych");
            }

        } else
            System.out.println("Nie udało się połączyć z bazą danych");
    }

    private boolean checkFields() {
        return !loginLabel.getText().equals("") && !passwordLabel.getText().equals("");
    }

    private boolean readData(Connection conn) throws SQLException {
        boolean result;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT login, haslo from Dane_logowania where login='" + loginLabel.getText() + "' and haslo='" + passwordLabel.getText() + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }
}
