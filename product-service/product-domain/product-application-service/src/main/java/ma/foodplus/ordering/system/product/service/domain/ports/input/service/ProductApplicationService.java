package ma.foodplus.ordering.system.product.service.domain.ports.input.service;

import jakarta.validation.Valid;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductResponse;
/**
 * Application service interface for product management.
 * This interface defines the use cases for product operations.
 */
public interface ProductApplicationService {
    /**
     * Creates a new product.
     *
     * @param createProductCommand The command containing product creation details
     * @return Response containing the created product ID and status message
     */
    CreateProductResponse createProduct(@Valid CreateProductCommand createProductCommand);

}
