package ma.foodplus.ordering.system.product.dto.category;

import jakarta.validation.constraints.Size;

public record UpdateCategoryCommand(
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    String name,

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description,

    Integer level,

    Long parentId
) {}