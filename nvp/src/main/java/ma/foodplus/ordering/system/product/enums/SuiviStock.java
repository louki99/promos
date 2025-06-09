package ma.foodplus.ordering.system.product.enums;

public enum SuiviStock{
    Aucun(0, "No tracking"),
    SERIALIZED(1, "Serialized tracking"),
    CMUP(2, "Weighted average cost"),
    FIFO(3, "First In, First Out"),
    LIFO(4, "Last In, First Out"),
    PAR_LOT(5, "Lot tracking");

    private final Integer value;
    private final String description;

    SuiviStock(Integer value,String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static SuiviStock fromValue(Integer value) {
        for (SuiviStock stock : SuiviStock.values()) {
            if (stock.value.equals(value)) {
                return stock;
            }
        }
        throw new IllegalArgumentException("Invalid SuiviStock value: " + value);
    }
}