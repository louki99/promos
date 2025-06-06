package ma.foodplus.ordering.system.product.service.domain.exception;

import ma.foodplus.ordering.system.domain.exception.DomainException;

public class ProductNotFoundException extends DomainException{

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message,Throwable cause) {
        super(message, cause);
    }
}
