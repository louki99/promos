package ma.foodplus.ordering.system.category.tariff.exception;

public enum ErrorCode {
    CATEGORY_TARIF_NOT_FOUND(4000, "CategoryTarif not found"),
    CATEGORY_TARIF_DUPLICATE_CODE(4001, "CategoryTarif code already exists"),
    CATEGORY_TARIF_INVALID_INPUT(4002, "Invalid input for CategoryTarif");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 