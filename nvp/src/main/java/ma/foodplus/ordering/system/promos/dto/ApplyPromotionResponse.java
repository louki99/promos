package ma.foodplus.ordering.system.promos.dto;

import ma.foodplus.ordering.system.promos.component.AppliedPromotion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ApplyPromotionResponse {
    private BigDecimal originalTotal;
    private BigDecimal discountTotal;
    private BigDecimal finalTotal;
    private List<LineItemResultDto> lineItems;
    private List<FreeItemDto> freeItems;
    private List<AppliedPromotion> appliedPromotions;
    private Map<String, BigDecimal> promotionDiscounts;

    // Private constructor for the builder
    private ApplyPromotionResponse(Builder builder) {
        this.originalTotal = builder.originalTotal;
        this.discountTotal = builder.discountTotal;
        this.finalTotal = builder.finalTotal;
        this.lineItems = builder.lineItems;
        this.freeItems = builder.freeItems;
        this.appliedPromotions = builder.appliedPromotions;
        this.promotionDiscounts = builder.promotionDiscounts;
    }

    // Getters...
    public Map<String, BigDecimal> getPromotionDiscounts() {
        return promotionDiscounts;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal originalTotal;
        private BigDecimal discountTotal;
        private BigDecimal finalTotal;
        private List<LineItemResultDto> lineItems;
        private List<FreeItemDto> freeItems;
        private List<AppliedPromotion> appliedPromotions;
        private Map<String, BigDecimal> promotionDiscounts;

        public Builder originalTotal(BigDecimal val) { originalTotal = val; return this; }
        public Builder discountTotal(BigDecimal val) { discountTotal = val; return this; }
        public Builder finalTotal(BigDecimal val) { finalTotal = val; return this; }
        public Builder lineItems(List<LineItemResultDto> val) { lineItems = val; return this; }
        public Builder freeItems(List<FreeItemDto> val) { freeItems = val; return this; }
        public Builder appliedPromotions(List<AppliedPromotion> val) { appliedPromotions = val; return this; }
        public Builder promotionDiscounts(Map<String, BigDecimal> val) { promotionDiscounts = val; return this; }

        public ApplyPromotionResponse build() {
            return new ApplyPromotionResponse(this);
        }
    }
}
