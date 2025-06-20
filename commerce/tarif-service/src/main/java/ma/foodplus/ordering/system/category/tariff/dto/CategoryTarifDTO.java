package ma.foodplus.ordering.system.category.tariff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTarifDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean active;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
} 