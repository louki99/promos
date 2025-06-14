package ma.foodplus.ordering.system.product.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryCommand(
    @NotBlank(message = "Category code is required")
    @Size(max = 50, message = "Category code cannot exceed 50 characters")
    String code,

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    String name,

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description,

    Long parentId
) {} 