package ma.foodplus.ordering.system.category.tariff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTarifDTO {
    private Long id;
    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must be at most 50 characters")
    private String code;
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;
    private Boolean active;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
} 