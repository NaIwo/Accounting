package sample;

public class TransactionValidator {

    public boolean checkValidate(String money) {
        checkTexField(money);
        return true;
    }

    private boolean checkTexField(String money) {
        if(!money.replaceAll(" ", "").equals("") && money.contains(",")) {
            System.out.println("Tutaj");
            return true;
        }
        else
            return false;
    }
}
