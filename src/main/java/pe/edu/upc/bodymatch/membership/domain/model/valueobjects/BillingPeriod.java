package pe.edu.upc.bodymatch.membership.domain.model.valueobjects;

public enum BillingPeriod {
    MONTHLY(30),
    QUARTERLY(90),
    YEARLY(365);

    private final int days;

    BillingPeriod(int days) {
        this.days = days;
    }

    public int days() {
        return days;
    }
}
