package ma.foodplus.ordering.system.product.service.domain.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateCategoryResponse{
    @NotNull
    private final UUID categoryId;
    @NotNull
    private final String message;
}
