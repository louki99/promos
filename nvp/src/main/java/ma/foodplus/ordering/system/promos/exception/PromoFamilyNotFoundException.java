package ma.foodplus.ordering.system.promos.exception;

public class PromoFamilyNotFoundException extends RuntimeException {
    
    public PromoFamilyNotFoundException(String message) {
        super(message);
    }
    
    public PromoFamilyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 