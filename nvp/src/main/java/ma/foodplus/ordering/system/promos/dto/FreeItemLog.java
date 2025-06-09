package ma.foodplus.ordering.system.promos.dto;

public class FreeItemLog {
    private int quantity;
    private final String grantingPromoCode; // Logs the first promo code that granted this item

    public FreeItemLog(int quantity, String grantingPromoCode) {
        this.quantity = quantity;
        this.grantingPromoCode = grantingPromoCode;
    }

    public void addQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getGrantingPromoCode() {
        return grantingPromoCode;
    }
}