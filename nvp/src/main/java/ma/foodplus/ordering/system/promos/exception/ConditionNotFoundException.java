package ma.foodplus.ordering.system.promos.exception;

public class ConditionNotFoundException extends RuntimeException {
    public ConditionNotFoundException(String message) {
        super(message);
    }

    public ConditionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 