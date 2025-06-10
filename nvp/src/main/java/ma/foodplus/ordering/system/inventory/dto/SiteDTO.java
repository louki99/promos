package ma.foodplus.ordering.system.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SiteDTO {
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Site code is required")
    @Pattern(regexp = "^[A-Z0-9]{1,10}$", message = "Site code must be alphanumeric and between 1-10 characters")
    private String siteCode;

    private String addressLine1;
    private String city;
    private String country;
    private String postalCode;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact phone must be exactly 10 digits")
    private String contactPhone;

    @Email(message = "Invalid email format")
    private String contactEmail;

    private boolean isActive;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
} 