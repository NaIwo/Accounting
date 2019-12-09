package sample;

public class Transaction {
    private String store;
    private String category;
    private String date;
    private Double money;
    private Integer rate;
    private String comment;


    public Transaction(String store, String category, String date, Double money, Integer rate, String comment) {
        this.store = store;
        this.category = category;
        this.date = date;
        this.money = money;
        this.rate = rate;
        this.comment = comment;
    }
    /*public void set(String store, String category, String date, Double  money, Integer  rate) {
        this.store = store;
        this.category = category;
        this.date = date;
        this.money = money;
        this.rate = rate;
    }*/

    public String getStore() {
        return this.store;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDate() {
        return this.date;
    }

    public Double getMoney() {
        return this.money;
    }

    public Integer getRate() {
        return this.rate;
    }

    public String getComment() {
        return this.comment;
    }
}
