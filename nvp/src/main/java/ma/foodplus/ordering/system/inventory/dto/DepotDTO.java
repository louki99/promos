package ma.foodplus.ordering.system.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ma.foodplus.ordering.system.inventory.model.Depot.DepotType;

import java.time.ZonedDateTime;

@Data
public class DepotDTO {
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Depot code is required")
    @Pattern(regexp = "^[A-Z0-9]{1,10}$", message = "Depot code must be alphanumeric and between 1-10 characters")
    private String depotCode;

    @NotNull(message = "Site ID is required")
    private Long siteId;

    @NotNull(message = "Depot type is required")
    private DepotType depotType;

    private Double capacityCubicMeters;
    private String temperatureRange;
    private boolean isRefrigerated;
    private boolean isActive;
    private Integer securityLevel;
    private String accessRestrictions;
    private String handlingEquipment;
    private String specialRequirements;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
} 