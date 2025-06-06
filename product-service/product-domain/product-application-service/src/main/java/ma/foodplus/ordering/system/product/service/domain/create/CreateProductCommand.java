package ma.foodplus.ordering.system.product.service.domain.create;

import ma.foodplus.ordering.system.domain.valueobject.Money;
import ma.foodplus.ordering.system.product.service.domain.valueobject.Quantity;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateProductCommand(@NotNull UUID productId,@NotNull String name,@NotNull String description,
                                   @NotNull Boolean isActive,@NotNull Boolean featured,@NotNull Money price,
                                   @NotNull UUID unitId,@NotNull String slug,String metaTitle,String metaDescription,
                                   @NotNull UUID categoryId,@NotNull Quantity quantity,
                                   List<CreateProductAttributeValueCommand> attributes){
}
