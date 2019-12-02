package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.SqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainPanelController {

    @FXML
    private TextField store;
    private Integer id;

    public MainPanelController() {

    }


    @FXML
    private void addStore(ActionEvent actionEvent) throws SQLException {
        if (!store.getText().replaceAll(" ", "").equals("")) {
            if (!checkIfStoreExists())
                addStoreToDatabase();
        }
    }

    private boolean checkIfStoreExists() throws SQLException {
        boolean result;
        SqlConnection sqlConnection = new SqlConnection();
        Statement stmt = sqlConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from sklepy where nazwa_sklepu='" + store.getText() + "'");
        result = rs.next();
        rs.close();
        stmt.close();
        return result;
    }

    private void addStoreToDatabase() {
        System.out.println("Siema");
    }
}
