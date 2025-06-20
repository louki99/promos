package ma.foodplus.ordering.system.category.tariff.exception;

public enum ErrorCode {
    // Order related errors (1000-1999)
    ORDER_NOT_FOUND(1000, "Order not found"),
    ORDER_ITEM_NOT_FOUND(1001, "Order item not found"),
    ORDER_INVALID_STATUS(1002, "Invalid order status"),
    ORDER_MINIMUM_VALUE_NOT_MET(1003, "Order does not meet minimum value requirement"),
    ORDER_DELIVERY_ADDRESS_REQUIRED(1004, "Delivery address is required"),
    ORDER_CREDIT_LIMIT_EXCEEDED(1005, "Credit limit exceeded"),
    ORDER_INVALID_PROMO_CODE(1006, "Invalid promo code"),
    ORDER_INVALID_DELIVERY_TIME(1007, "Invalid delivery time"),
    ORDER_INVALID_BULK_ORDER(1008, "Invalid bulk order"),
    ORDER_INVALID_LOYALTY_POINTS(1009, "Invalid loyalty points"),

    // Product related errors (2000-2999)
    PRODUCT_NOT_FOUND(2000, "Product not found"),
    PRODUCT_INVALID_PRICE(2001, "Invalid product price"),
    PRODUCT_INVALID_QUANTITY(2002, "Invalid product quantity"),
    PRODUCT_OUT_OF_STOCK(2003, "Product out of stock"),
    PRODUCT_INVALID_CATEGORY(2004, "Invalid product category"),

    // Customer related errors (3000-3999)
    CUSTOMER_NOT_FOUND(3000, "Customer not found"),
    CUSTOMER_INVALID_CREDENTIALS(3001, "Invalid customer credentials"),
    CUSTOMER_INVALID_ADDRESS(3002, "Invalid customer address"),
    CUSTOMER_INVALID_PHONE(3003, "Invalid customer phone number"),
    CUSTOMER_INVALID_EMAIL(3004, "Invalid customer email"),

    // System errors (9000-9999)
    SYSTEM_ERROR(9000, "System error"),
    VALIDATION_ERROR(9001, "Validation error"),
    DATABASE_ERROR(9002, "Database error"),
    CACHE_ERROR(9003, "Cache error"),
    EXTERNAL_SERVICE_ERROR(9004, "External service error");

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