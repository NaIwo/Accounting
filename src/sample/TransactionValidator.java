package sample;

import javafx.scene.control.*;

import java.time.LocalDate;

public class TransactionValidator {

    public boolean checkValidate(String money, ComboBox store, ComboBox category, ComboBox rate, ComboBox payment, DatePicker date) {
        WindowOperation windowOperation = new WindowOperation();
        LocalDate localDate = LocalDate.now();
        if (checkTexField(money) && !store.getSelectionModel().isEmpty() &&
                !category.getSelectionModel().isEmpty() && !rate.getSelectionModel().isEmpty()
                && !payment.getSelectionModel().isEmpty() && date.getValue() != null) {
            if(localDate.compareTo(date.getValue()) >= 0)
                return true;
            else {
                windowOperation.warrningWindow("Błąd", "Podano niepoprawne dane, wybrana data dopiero nastąpi",
                        "Popraw podaną datę", Alert.AlertType.ERROR);
                return false;
            }
        } else {
            windowOperation.warrningWindow("Błąd", "Nie wypełniono wartości wszystkich wymaganych pól lub podano niepoprawnie dane",
                    "Sprawdź poprawność podanych danych", Alert.AlertType.ERROR);
            return false;
        }
    }

    public boolean checkValidateForSubscription(ComboBox store, ComboBox category, ComboBox rate, ComboBox payment, TextField comment) {
        WindowOperation windowOperation = new WindowOperation();

        if (!store.getSelectionModel().isEmpty() || !category.getSelectionModel().isEmpty() || !rate.getSelectionModel().isEmpty()
                || !payment.getSelectionModel().isEmpty() || !comment.getText().isEmpty()) {
                return true;
        } else {
            windowOperation.warrningWindow("Błąd", "Nie wypełniono żadnego pola",
                    "Wypełnij conajmniej jedno pole", Alert.AlertType.ERROR);
            return false;
        }
    }


    public boolean checkTexField(String money) {
        if (money.matches("^[0-9]+\\.[0-9][0-9]?$") || money.matches("^[0-9]+$")) return true;
        else return false;
    }
}
