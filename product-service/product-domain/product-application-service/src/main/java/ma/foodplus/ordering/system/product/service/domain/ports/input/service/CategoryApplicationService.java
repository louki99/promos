package ma.foodplus.ordering.system.product.service.domain.ports.input.service;

import jakarta.validation.Valid;
import ma.foodplus.ordering.system.product.service.domain.create.CreateCategoryCommand;
import ma.foodplus.ordering.system.product.service.domain.create.CreateCategoryResponse;

/**
 * Application service interface for category management.
 * This interface defines the use cases for category operations.
 */
public interface CategoryApplicationService{
    /**
     * Creates a new category.
     *
     * @param createCategoryCommand The command containing category creation details
     * @return Response containing the created category ID and status message
     */
    CreateCategoryResponse createCategory(@Valid CreateCategoryCommand createCategoryCommand);

}
