package ma.foodplus.ordering.system.product.service.domain.create;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCategoryCommand(@NotNull UUID categoryId,@NotNull String name,String description){
}
