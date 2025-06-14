package ma.foodplus.ordering.system.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.foodplus.ordering.system.product.enums.SuiviStock;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(unique = true, nullable = false)
    private String sku; // Stock Keeping Unit

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, length = 36)
    private String barcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_code", referencedColumnName = "code")
    private ProductFamily productFamily;

    @ManyToMany
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    // Pricing
    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "sale_unit", length = 50)
    private String saleUnit;

    @Column(name = "price_including_tax", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceIncludingTax;

    @Column(name = "promo_sku_points", nullable = false, precision = 10, scale = 2)
    private BigDecimal promoSkuPoints = BigDecimal.ZERO;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    // Wholesale Pricing
    @Column(name = "wholesale_price", precision = 10, scale = 2)
    private BigDecimal wholesalePrice;

    @Column(name = "wholesale_minimum_quantity")
    private Integer wholesaleMinimumQuantity = 1;

    @Column(name = "wholesale_discount_percentage", precision = 5, scale = 2)
    private BigDecimal wholesaleDiscountPercentage;

    @Column(name = "wholesale_tier_1_price", precision = 10, scale = 2)
    private BigDecimal wholesaleTier1Price;

    @Column(name = "wholesale_tier_1_quantity")
    private Integer wholesaleTier1Quantity;

    @Column(name = "wholesale_tier_2_price", precision = 10, scale = 2)
    private BigDecimal wholesaleTier2Price;

    @Column(name = "wholesale_tier_2_quantity")
    private Integer wholesaleTier2Quantity;

    @Column(name = "wholesale_tier_3_price", precision = 10, scale = 2)
    private BigDecimal wholesaleTier3Price;

    @Column(name = "wholesale_tier_3_quantity")
    private Integer wholesaleTier3Quantity;

    // Bulk Order Settings
    @Column(name = "minimum_order_quantity")
    private Integer minimumOrderQuantity = 1;

    @Column(name = "maximum_order_quantity")
    private Integer maximumOrderQuantity;

    @Column(name = "bulk_discount_threshold")
    private Integer bulkDiscountThreshold;

    @Column(name = "bulk_discount_percentage", precision = 5, scale = 2)
    private BigDecimal bulkDiscountPercentage;

    @Column(name = "bulk_package_size")
    private Integer bulkPackageSize;

    @Column(name = "bulk_package_unit", length = 20)
    private String bulkPackageUnit;

    // Operational flags
    @Column(nullable = false)
    private Boolean deliverable = false;

    @Column(nullable = false)
    private Boolean vendable = true;

    @Column(nullable = false)
    private Boolean visible = true;

    @Column(nullable = false)
    private Boolean inactive = false;

    @Column(name = "requires_approval", nullable = false)
    private Boolean requiresApproval = false;

    @Column(name = "is_bulk_item", nullable = false)
    private Boolean isBulkItem = false;

    @Column(name = "is_perishable", nullable = false)
    private Boolean isPerishable = false;

    @Column(name = "requires_cold_storage", nullable = false)
    private Boolean requiresColdStorage = false;

    @Column(name = "is_wholesale_only", nullable = false)
    private Boolean isWholesaleOnly = false;

    @Column(name = "requires_contract", nullable = false)
    private Boolean requiresContract = false;

    // Stock tracking
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_tracking", nullable = false, length = 20)
    private SuiviStock stockTracking = SuiviStock.Aucun;

    // Product attributes
    @Column(precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(name = "weight_unit", length = 10)
    private String weightUnit;

    @Column(length = 50)
    private String dimensions;

    @Column(name = "package_size", length = 50)
    private String packageSize;

    @Column(name = "package_quantity")
    private Integer packageQuantity;

    @Column(name = "supplier_code", length = 50)
    private String supplierCode;

    @Column(name = "supplier_name", length = 100)
    private String supplierName;

    @Column(name = "country_of_origin", length = 100)
    private String countryOfOrigin;

    @Column(length = 200)
    private String certification;

    // Images and media
    @Column(length = 500)
    private String photo;

    @Column(name = "additional_photos", columnDefinition = "TEXT[]")
    private List<String> additionalPhotos = new ArrayList<>();

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // B2B specific fields
    @Column(name = "b2b_minimum_order_value", precision = 19, scale = 2)
    private BigDecimal b2bMinimumOrderValue;

    @Column(name = "b2b_contract_required")
    private Boolean b2bContractRequired = false;

    @Column(name = "b2b_credit_terms")
    private Integer b2bCreditTerms;

    @Column(name = "b2b_payment_methods", length = 255)
    private String b2bPaymentMethods;

    @Column(name = "b2b_delivery_lead_time")
    private Integer b2bDeliveryLeadTime;

    @Column(name = "b2b_custom_pricing_enabled")
    private Boolean b2bCustomPricingEnabled = false;

    @Column(name = "b2b_volume_discount_enabled")
    private Boolean b2bVolumeDiscountEnabled = false;

    @Column(name = "b2b_contract_discount_percentage", precision = 5, scale = 2)
    private BigDecimal b2bContractDiscountPercentage;

    // B2C specific fields
    @Column(name = "b2c_retail_price", precision = 19, scale = 2)
    private BigDecimal b2cRetailPrice;

    @Column(name = "b2c_promo_price", precision = 19, scale = 2)
    private BigDecimal b2cPromoPrice;

    @Column(name = "b2c_promo_start_date")
    private ZonedDateTime b2cPromoStartDate;

    @Column(name = "b2c_promo_end_date")
    private ZonedDateTime b2cPromoEndDate;

    @Column(name = "b2c_loyalty_points_multiplier", precision = 5, scale = 2)
    private BigDecimal b2cLoyaltyPointsMultiplier = BigDecimal.ONE;

    @Column(name = "b2c_display_in_catalog")
    private Boolean b2cDisplayInCatalog = true;

    @Column(name = "b2c_featured")
    private Boolean b2cFeatured = false;

    @Column(name = "b2c_rating", precision = 3, scale = 2)
    private BigDecimal b2cRating;

    @Column(name = "b2c_review_count")
    private Integer b2cReviewCount = 0;

    // Common fields
    @Column(name = "target_market", length = 50)
    private String targetMarket = "BOTH";

    @Column(name = "customer_segments", length = 255)
    private String customerSegments;

    @Column(name = "seasonality", length = 50)
    private String seasonality;

    @Column(name = "availability_schedule", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private JsonNode availabilitySchedule;

    @Column(name = "custom_attributes", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private JsonNode customAttributes;

    // Business Logic Methods

    /**
     * Calculate the wholesale price based on quantity and tier pricing
     * @param quantity The quantity being ordered
     * @return The calculated wholesale price per unit
     */
    public BigDecimal calculateWholesalePrice(Integer quantity) {
        if (quantity == null || quantity < wholesaleMinimumQuantity) {
            return wholesalePrice;
        }

        if (wholesaleTier3Quantity != null && quantity >= wholesaleTier3Quantity) {
            return wholesaleTier3Price;
        }
        if (wholesaleTier2Quantity != null && quantity >= wholesaleTier2Quantity) {
            return wholesaleTier2Price;
        }
        if (wholesaleTier1Quantity != null && quantity >= wholesaleTier1Quantity) {
            return wholesaleTier1Price;
        }

        return wholesalePrice;
    }

    /**
     * Get the name of the applied wholesale tier
     * @param quantity The quantity being ordered
     * @return The name of the applied tier or null if no tier applies
     */
    public String getAppliedWholesaleTier(Integer quantity) {
        if (quantity == null || quantity < wholesaleMinimumQuantity) {
            return null;
        }

        if (wholesaleTier3Quantity != null && quantity >= wholesaleTier3Quantity) {
            return "TIER_3";
        }
        if (wholesaleTier2Quantity != null && quantity >= wholesaleTier2Quantity) {
            return "TIER_2";
        }
        if (wholesaleTier1Quantity != null && quantity >= wholesaleTier1Quantity) {
            return "TIER_1";
        }

        return "BASE_WHOLESALE";
    }

    /**
     * Calculate the bulk discount percentage based on quantity
     * @param quantity The quantity being ordered
     * @return The applicable discount percentage
     */
    public BigDecimal calculateBulkDiscount(Integer quantity) {
        if (quantity == null || bulkDiscountThreshold == null || 
            quantity < bulkDiscountThreshold || bulkDiscountPercentage == null) {
            return BigDecimal.ZERO;
        }
        return bulkDiscountPercentage;
    }

    /**
     * Check if the product is eligible for wholesale pricing
     * @param quantity The quantity being ordered
     * @return true if the product is eligible for wholesale pricing
     */
    public boolean isEligibleForWholesale(Integer quantity) {
        return !isWholesaleOnly && quantity >= wholesaleMinimumQuantity;
    }

    /**
     * Check if the product is eligible for bulk discount
     * @param quantity The quantity being ordered
     * @return true if the product is eligible for bulk discount
     */
    public boolean isEligibleForBulkDiscount(Integer quantity) {
        return isBulkItem && quantity >= bulkDiscountThreshold;
    }

    /**
     * Calculate the final price including all applicable discounts
     * @param quantity The quantity being ordered
     * @param isWholesale Whether this is a wholesale order
     * @return The final price per unit
     */
    public BigDecimal calculateFinalPrice(Integer quantity, boolean isWholesale) {
        BigDecimal basePrice = isWholesale ? calculateWholesalePrice(quantity) : unitPrice;
        
        if (isWholesale && wholesaleDiscountPercentage != null) {
            basePrice = basePrice.multiply(BigDecimal.ONE.subtract(wholesaleDiscountPercentage.divide(BigDecimal.valueOf(100))));
        }
        
        if (isEligibleForBulkDiscount(quantity)) {
            basePrice = basePrice.multiply(BigDecimal.ONE.subtract(bulkDiscountPercentage.divide(BigDecimal.valueOf(100))));
        }
        
        return basePrice;
    }

    /**
     * Validate if the quantity meets all requirements for a bulk order
     * @param quantity The quantity to validate
     * @return A validation result containing any errors
     */
    public BulkOrderValidationResult validateBulkOrderQuantity(Integer quantity) {
        BulkOrderValidationResult result = new BulkOrderValidationResult();
        
        if (quantity == null) {
            result.addError("Quantity cannot be null");
            return result;
        }

        if (quantity < minimumOrderQuantity) {
            result.addError(String.format("Quantity must be at least %d", minimumOrderQuantity));
        }

        if (maximumOrderQuantity != null && quantity > maximumOrderQuantity) {
            result.addError(String.format("Quantity cannot exceed %d", maximumOrderQuantity));
        }

        if (isBulkItem && bulkPackageSize != null && quantity % bulkPackageSize != 0) {
            result.addError(String.format("Quantity must be a multiple of %d %s", 
                bulkPackageSize, bulkPackageUnit != null ? bulkPackageUnit : "units"));
        }

        return result;
    }

    /**
     * Calculate the number of packages needed for a bulk order
     * @param quantity The total quantity ordered
     * @return The number of packages needed
     */
    public Integer calculateRequiredPackages(Integer quantity) {
        if (quantity == null || bulkPackageSize == null || bulkPackageSize <= 0) {
            return quantity;
        }
        return (int) Math.ceil((double) quantity / bulkPackageSize);
    }

    /**
     * Check if the product requires special handling
     * @return true if the product requires special handling
     */
    public boolean requiresSpecialHandling() {
        return isPerishable || requiresColdStorage || requiresContract;
    }

    /**
     * Get the storage requirements for the product
     * @return A string describing the storage requirements
     */
    public String getStorageRequirements() {
        StringBuilder requirements = new StringBuilder();
        
        if (isPerishable) {
            requirements.append("Perishable");
        }
        if (requiresColdStorage) {
            if (requirements.length() > 0) {
                requirements.append(", ");
            }
            requirements.append("Cold Storage Required");
        }
        
        return requirements.toString();
    }

    /**
     * Calculate the total weight for a given quantity
     * @param quantity The quantity to calculate weight for
     * @return The total weight in the product's weight unit
     */
    public BigDecimal calculateTotalWeight(Integer quantity) {
        if (weight == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return weight.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Check if the product is available for wholesale
     * @param quantity The quantity being ordered
     * @param hasContract Whether the customer has a valid contract
     * @return true if the product is available for wholesale
     */
    public boolean isAvailableForWholesale(Integer quantity, boolean hasContract) {
        if (isWholesaleOnly && !hasContract) {
            return false;
        }
        return isEligibleForWholesale(quantity);
    }

    /**
     * Get pricing notes for wholesale orders
     * @param quantity The quantity being ordered
     * @return A string containing pricing notes
     */
    public String getWholesalePricingNotes(Integer quantity) {
        StringBuilder notes = new StringBuilder();
        
        if (isWholesaleOnly) {
            notes.append("Wholesale only product. ");
        }
        
        if (requiresContract) {
            notes.append("Contract required. ");
        }
        
        String tier = getAppliedWholesaleTier(quantity);
        if (tier != null) {
            notes.append(String.format("Applied tier: %s. ", tier));
        }
        
        if (isEligibleForBulkDiscount(quantity)) {
            notes.append(String.format("Bulk discount of %s%% applied. ", 
                bulkDiscountPercentage.toString()));
        }
        
        return notes.toString().trim();
    }

    /**
     * Inner class for bulk order validation results
     */
    public static class BulkOrderValidationResult {
        private final List<String> errors = new ArrayList<>();
        private boolean valid = true;

        public void addError(String error) {
            errors.add(error);
            valid = false;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return Collections.unmodifiableList(errors);
        }

        public String getErrorMessage() {
            return String.join(", ", errors);
        }
    }

    public Boolean getIsBulkItem() {
        return isBulkItem;
    }

    public Boolean getIsPerishable() {
        return isPerishable;
    }

    public Boolean getRequiresColdStorage() {
        return requiresColdStorage;
    }

    public Boolean getRequiresContract() {
        return requiresContract;
    }

    /**
     * Check if the product is available for B2B customers
     */
    public boolean isAvailableForB2B() {
        return "B2B".equals(targetMarket) || "BOTH".equals(targetMarket);
    }

    /**
     * Check if the product is available for B2C customers
     */
    public boolean isAvailableForB2C() {
        return "B2C".equals(targetMarket) || "BOTH".equals(targetMarket);
    }

    /**
     * Get the current B2C price considering promotions
     */
    public BigDecimal getCurrentB2CPrice() {
        if (b2cPromoPrice != null && b2cPromoStartDate != null && b2cPromoEndDate != null) {
            ZonedDateTime now = ZonedDateTime.now();
            if (now.isAfter(b2cPromoStartDate) && now.isBefore(b2cPromoEndDate)) {
                return b2cPromoPrice;
            }
        }
        return b2cRetailPrice != null ? b2cRetailPrice : salePrice;
    }

    /**
     * Calculate loyalty points for B2C purchase
     */
    public int calculateLoyaltyPoints(BigDecimal purchaseAmount) {
        return purchaseAmount.multiply(b2cLoyaltyPointsMultiplier).intValue();
    }

    /**
     * Check if product is in season
     */
    public boolean isInSeason() {
        if (seasonality == null) return true;
        // Implementation would depend on your seasonality logic
        return true;
    }

    /**
     * Get B2B pricing information
     */
    public B2BPricingInfo getB2BPricingInfo() {
        return new B2BPricingInfo(
            b2bMinimumOrderValue,
            b2bContractRequired,
            b2bCreditTerms,
            b2bPaymentMethods,
            b2bDeliveryLeadTime,
            b2bCustomPricingEnabled,
            b2bVolumeDiscountEnabled,
            b2bContractDiscountPercentage
        );
    }

    /**
     * Get B2C pricing information
     */
    public B2CPricingInfo getB2CPricingInfo() {
        return new B2CPricingInfo(
            getCurrentB2CPrice(),
            b2cPromoPrice,
            b2cPromoStartDate,
            b2cPromoEndDate,
            b2cLoyaltyPointsMultiplier,
            b2cDisplayInCatalog,
            b2cFeatured,
            b2cRating,
            b2cReviewCount
        );
    }

    // Inner classes for structured data
    @Data
    @AllArgsConstructor
    public static class B2BPricingInfo {
        private final BigDecimal minimumOrderValue;
        private final Boolean contractRequired;
        private final Integer creditTerms;
        private final String paymentMethods;
        private final Integer deliveryLeadTime;
        private final Boolean customPricingEnabled;
        private final Boolean volumeDiscountEnabled;
        private final BigDecimal contractDiscountPercentage;
    }

    @Data
    @AllArgsConstructor
    public static class B2CPricingInfo {
        private final BigDecimal currentPrice;
        private final BigDecimal promoPrice;
        private final ZonedDateTime promoStartDate;
        private final ZonedDateTime promoEndDate;
        private final BigDecimal loyaltyPointsMultiplier;
        private final Boolean displayInCatalog;
        private final Boolean featured;
        private final BigDecimal rating;
        private final Integer reviewCount;
    }
} 