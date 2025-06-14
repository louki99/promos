package ma.foodplus.ordering.system.product.dto.response;

import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.enums.SuiviStock;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record ProductResponse(
    // ID and Basic Information
    ProductId id,
    String reference,
    String sku,
    String title,
    String description,
    String barcode,
    String familyCode,
    List<CategoryResponse> categories,

    // Pricing
    BigDecimal salePrice,
    BigDecimal unitPrice,
    String saleUnit,
    BigDecimal priceIncludingTax,
    BigDecimal promoSkuPoints,
    BigDecimal taxRate,

    // Wholesale Pricing
    BigDecimal wholesalePrice,
    Integer wholesaleMinimumQuantity,
    BigDecimal wholesaleDiscountPercentage,
    BigDecimal wholesaleTier1Price,
    Integer wholesaleTier1Quantity,
    BigDecimal wholesaleTier2Price,
    Integer wholesaleTier2Quantity,
    BigDecimal wholesaleTier3Price,
    Integer wholesaleTier3Quantity,

    // Bulk Order Settings
    Integer minimumOrderQuantity,
    Integer maximumOrderQuantity,
    Integer bulkDiscountThreshold,
    BigDecimal bulkDiscountPercentage,
    Integer bulkPackageSize,
    String bulkPackageUnit,

    // Operational flags
    Boolean deliverable,
    Boolean vendable,
    Boolean visible,
    Boolean inactive,
    Boolean requiresApproval,
    Boolean isBulkItem,
    Boolean isPerishable,
    Boolean requiresColdStorage,
    Boolean isWholesaleOnly,
    Boolean requiresContract,

    // Stock tracking
    SuiviStock stockTracking,

    // Product attributes
    BigDecimal weight,
    String weightUnit,
    String dimensions,
    String packageSize,
    Integer packageQuantity,
    String supplierCode,
    String supplierName,
    String countryOfOrigin,
    String certification,

    // Images and media
    String photo,
    List<String> additionalPhotos,

    // B2B specific fields
    BigDecimal b2bMinimumOrderValue,
    Boolean b2bContractRequired,
    Integer b2bCreditTerms,
    String b2bPaymentMethods,
    Integer b2bDeliveryLeadTime,
    Boolean b2bCustomPricingEnabled,
    Boolean b2bVolumeDiscountEnabled,
    BigDecimal b2bContractDiscountPercentage,

    // B2C specific fields
    BigDecimal b2cRetailPrice,
    BigDecimal b2cPromoPrice,
    ZonedDateTime b2cPromoStartDate,
    ZonedDateTime b2cPromoEndDate,
    BigDecimal b2cLoyaltyPointsMultiplier,
    Boolean b2cDisplayInCatalog,
    Boolean b2cFeatured,
    BigDecimal b2cRating,
    Integer b2cReviewCount,

    // Common fields
    String targetMarket,
    String customerSegments,
    String seasonality,
    String availabilitySchedule,
    String customAttributes,

    // Timestamps
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {
    // Inner record for Category response
    public record CategoryResponse(
        Long id,
        String code,
        String name,
        String description,
        Integer level,
        Long parentId
    ) {}
}