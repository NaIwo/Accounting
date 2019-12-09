package sample;

public class Restriction {
    private String category;
    private Double restrictionValue;
    private Double actualValue;
    private String comment;

    public Restriction(String category, Double restrictionValue, Double actualValue, String comment) {
        this.category = category;
        this.restrictionValue = restrictionValue;
        this.actualValue = actualValue;
        this.comment = comment;
    }

    public String getCategory() {
        return this.category;
    }

    public Double getRestrictionValue() {
        return this.restrictionValue;
    }

    public Double getActualValue() {
        return this.actualValue;
    }

    public String getComment() {
        return this.comment;
    }
}
