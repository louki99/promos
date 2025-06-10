package ma.foodplus.ordering.system.customer.model;

import ma.foodplus.ordering.system.product.model.Product;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products_customer")
public class ProductCustomer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_product")
    private Product product;

    @Column(length = 50)
    private String category;

    @Positive(message = "Coefficient must be positive")
    @Digits(integer = 10, fraction = 2, message = "Coefficient must have at most 2 decimal places")
    private BigDecimal coef;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 6, message = "Price must have at most 6 decimal places")
    @Column(name = "prix_ttc", precision = 24, scale = 6)
    private BigDecimal prixTTC;
    
    @Positive(message = "Quantity must be positive")
    @Column(name = "qte_mont")
    private Integer qteMont;

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_customer")
    private Customer customer;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @Digits(integer = 10, fraction = 6, message = "Discount must have at most 6 decimal places")
    @Column(precision = 24, scale = 6)
    private BigDecimal remise;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "New price must be greater than 0")
    @Digits(integer = 10, fraction = 6, message = "New price must have at most 6 decimal places")
    @Column(name = "prix_ven_nouv", precision = 24, scale = 6)
    private BigDecimal prixVenNouv;
    
    @DecimalMin(value = "0.0", message = "New discount cannot be negative")
    @Digits(integer = 10, fraction = 6, message = "New discount must have at most 6 decimal places")
    @Column(name = "remise_nouv", precision = 24, scale = 6)
    private BigDecimal remiseNouv;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "is_active")
    private boolean active;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
        active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    public boolean isValid() {
        return active && 
               product != null && 
               customer != null && 
               prixTTC != null && 
               prixTTC.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal calculateDiscountedPrice() {
        if (remise == null || remise.compareTo(BigDecimal.ZERO) == 0) {
            return prixTTC;
        }
        return prixTTC.multiply(BigDecimal.ONE.subtract(remise.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)));
    }

    public BigDecimal calculateNewDiscountedPrice() {
        if (prixVenNouv == null) {
            return calculateDiscountedPrice();
        }
        if (remiseNouv == null || remiseNouv.compareTo(BigDecimal.ZERO) == 0) {
            return prixVenNouv;
        }
        return prixVenNouv.multiply(BigDecimal.ONE.subtract(remiseNouv.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)));
    }

    public boolean hasPriceChange() {
        return prixVenNouv != null && prixVenNouv.compareTo(prixTTC) != 0;
    }

    public boolean hasDiscountChange() {
        return remiseNouv != null && remiseNouv.compareTo(remise) != 0;
    }
} 