package ma.foodplus.ordering.system.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCustomerDTO {
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private String category;
    private BigDecimal coef;
    private BigDecimal prixTTC;
    private Integer qteMont;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private BigDecimal remise;
    private BigDecimal prixVenNouv;
    private BigDecimal remiseNouv;
} 