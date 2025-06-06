package ma.foodplus.ordering.system.product.service.domain;

import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.domain.valueobject.Money;
import ma.foodplus.ordering.system.product.service.domain.entity.Product;
import ma.foodplus.ordering.system.product.service.domain.event.product.ProductCreatedEvent;
import ma.foodplus.ordering.system.product.service.domain.event.product.ProductDeletedEvent;
import ma.foodplus.ordering.system.product.service.domain.event.product.ProductUpdatedEvent;
import ma.foodplus.ordering.system.product.service.domain.exception.ProductDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static ma.foodplus.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class ProductDomainServiceImpl implements ProductDomainService {

    @Override
    public ProductCreatedEvent createProduct(Product product) {
        validateProductForCreation(product);
        log.info("Product with id: {} is initiated", product.getId().getValue());
        return new ProductCreatedEvent(product, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public ProductDeletedEvent deleteProduct(Product product) {
        validateProductForDeletion(product);
        log.info("Product with id: {} is deleted", product.getId().getValue());
        return new ProductDeletedEvent(product, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public ProductUpdatedEvent updateProduct(Product product) {
        validateProductForUpdate(product);
        log.info("Product with id: {} is updated", product.getId().getValue());
        return new ProductUpdatedEvent(product, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void validateProductForCreation(Product product) {
        if (product == null) {
            throw new ProductDomainException("Product cannot be null");
        }
        if (product.getId() == null) {
            throw new ProductDomainException("Product ID cannot be null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ProductDomainException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice().getAmount().compareTo(Money.ZERO.getAmount()) <= 0) {
            throw new ProductDomainException("Product price must be greater than zero");
        }
        if (product.getUnit() == null) {
            throw new ProductDomainException("Product unit cannot be null");
        }
        if (product.getCategory() == null) {
            throw new ProductDomainException("Product category cannot be null");
        }
        if (product.getQuantity() == null) {
            throw new ProductDomainException("Product quantity cannot be null");
        }
    }

    private void validateProductForDeletion(Product product) {
        if (product == null) {
            throw new ProductDomainException("Product cannot be null");
        }
        if (product.getId() == null) {
            throw new ProductDomainException("Product ID cannot be null");
        }
        // Add any additional business rules for deletion
        // For example, check if product is in any active orders
    }

    private void validateProductForUpdate(Product product) {
        if (product == null) {
            throw new ProductDomainException("Product cannot be null");
        }
        if (product.getId() == null) {
            throw new ProductDomainException("Product ID cannot be null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ProductDomainException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice().getAmount().compareTo(Money.ZERO.getAmount()) <= 0) {
            throw new ProductDomainException("Product price must be greater than zero");
        }
        if (product.getUnit() == null) {
            throw new ProductDomainException("Product unit cannot be null");
        }
        if (product.getCategory() == null) {
            throw new ProductDomainException("Product category cannot be null");
        }
        if (product.getQuantity() == null) {
            throw new ProductDomainException("Product quantity cannot be null");
        }
    }
}
