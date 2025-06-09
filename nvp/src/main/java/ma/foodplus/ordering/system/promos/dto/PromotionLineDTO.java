package ma.foodplus.ordering.system.promos.dto;

import lombok.Data;

@Data
public class PromotionLineDTO {
    private Long id;
    private Long promotionId;
    private String paidFamilyCode;
    private Long paidProductId;
    private String freeFamilyCode;
    private Long freeProductId;
} 