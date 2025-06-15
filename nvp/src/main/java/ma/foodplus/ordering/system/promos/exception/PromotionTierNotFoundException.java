package ma.foodplus.ordering.system.promos.exception;

public class PromotionTierNotFoundException extends RuntimeException {
    public PromotionTierNotFoundException(String message) {
        super(message);
    }

    public PromotionTierNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 