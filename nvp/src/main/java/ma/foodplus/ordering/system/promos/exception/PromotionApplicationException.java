package ma.foodplus.ordering.system.promos.exception;

/**
 * Exception thrown when there is an error during the promotion application process.
 */
public class PromotionApplicationException extends RuntimeException {
    
    public PromotionApplicationException(String message) {
        super(message);
    }

    public PromotionApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
} 