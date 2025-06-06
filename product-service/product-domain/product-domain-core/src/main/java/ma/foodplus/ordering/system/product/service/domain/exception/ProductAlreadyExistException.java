package ma.foodplus.ordering.system.product.service.domain.exception;

import ma.foodplus.ordering.system.domain.exception.DomainException;

public class ProductAlreadyExistException extends DomainException{

    public ProductAlreadyExistException(String message) {
        super(message);
    }

    public ProductAlreadyExistException(String message,Throwable cause) {
        super(message, cause);
    }
}
