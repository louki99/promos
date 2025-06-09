package ma.foodplus.ordering.system.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTarifDTO {
    private Long id;
    private String description;
    private BigDecimal priceTTC;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
} 