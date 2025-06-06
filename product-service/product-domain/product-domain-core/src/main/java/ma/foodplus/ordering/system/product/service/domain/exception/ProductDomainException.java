package ma.foodplus.ordering.system.product.service.domain.exception;


import ma.foodplus.ordering.system.domain.exception.DomainException;

public class ProductDomainException extends DomainException{

    public ProductDomainException(String message) {
        super(message);
    }

    public ProductDomainException(String message,Throwable cause) {
        super(message, cause);
    }
}
