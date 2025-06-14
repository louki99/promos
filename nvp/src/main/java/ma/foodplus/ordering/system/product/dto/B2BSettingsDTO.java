package ma.foodplus.ordering.system.product.dto;

import java.math.BigDecimal;

public record B2BSettingsDTO(
    BigDecimal b2bMinimumOrderValue,
    Boolean b2bContractRequired,
    Integer b2bCreditTerms,
    String b2bPaymentMethods,
    Integer b2bDeliveryLeadTime,
    Boolean b2bCustomPricingEnabled,
    Boolean b2bVolumeDiscountEnabled,
    BigDecimal b2bContractDiscountPercentage
) {} 