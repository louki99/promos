package ma.foodplus.ordering.system.product.service.domain.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateProductResponse{
    @NotNull
    private final UUID productId;
    @NotNull
    private final String message;
}
