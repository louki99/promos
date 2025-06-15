package ma.foodplus.ordering.system.promos.exception;

public class PromoFamilyValidationException extends RuntimeException {
    
    public PromoFamilyValidationException(String message) {
        super(message);
    }
    
    public PromoFamilyValidationException(String message, Throwable cause) {
        super(message, cause);
    }
} 