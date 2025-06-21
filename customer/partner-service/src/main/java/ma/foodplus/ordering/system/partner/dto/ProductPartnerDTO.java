package ma.foodplus.ordering.system.partner.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductPartnerDTO{
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private String category;
    private BigDecimal coef;
    private BigDecimal prixTTC;
    private Integer qteMont;

    @NotNull(message = "Partner ID is required")
    private Long partnerId;

    private BigDecimal remise;
    private BigDecimal prixVenNouv;
    private BigDecimal remiseNouv;
} 