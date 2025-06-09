package ma.foodplus.ordering.system.promos.dto;

// Inside ApplyPromotionResponse.java

import ma.foodplus.ordering.system.promos.component.AppliedPromotion;

import java.math.BigDecimal;
import java.util.List;

public class ApplyPromotionResponse {
    private BigDecimal originalTotal;
    private BigDecimal discountTotal;
    private BigDecimal finalTotal;
    private List<LineItemResultDto> lineItems;
    private List<FreeItemDto> freeItems;

    // Here is the list of applied promotions
    private List<AppliedPromotion> appliedPromotions;

    public BigDecimal getOriginalTotal(){
        return originalTotal;
    }

    public BigDecimal getDiscountTotal(){
        return discountTotal;
    }

    public BigDecimal getFinalTotal(){
        return finalTotal;
    }

    public List<LineItemResultDto> getLineItems(){
        return lineItems;
    }

    public List<FreeItemDto> getFreeItems(){
        return freeItems;
    }

    public List<AppliedPromotion> getAppliedPromotions(){
        return appliedPromotions;
    }

    public void setOriginalTotal(BigDecimal originalTotal){
        this.originalTotal=originalTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal){
        this.discountTotal=discountTotal;
    }

    public void setFinalTotal(BigDecimal finalTotal){
        this.finalTotal=finalTotal;
    }

    public void setLineItems(List<LineItemResultDto> lineItems){
        this.lineItems=lineItems;
    }

    public void setFreeItems(List<FreeItemDto> freeItems){
        this.freeItems=freeItems;
    }

    public void setAppliedPromotions(List<AppliedPromotion> appliedPromotions){
        this.appliedPromotions=appliedPromotions;
    }
}
