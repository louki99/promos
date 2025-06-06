package ma.foodplus.ordering.system.product.service.domain.exception;

import ma.foodplus.ordering.system.domain.exception.DomainException;

public class NegativeQuantityException extends DomainException{

    public NegativeQuantityException(String message) {
        super(message);
    }

    public NegativeQuantityException(String message,Throwable cause) {
        super(message, cause);
    }
}
