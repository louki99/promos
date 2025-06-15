package ma.foodplus.ordering.system.promos.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class PromotionValidationResponse {
    private boolean isValid;
} 