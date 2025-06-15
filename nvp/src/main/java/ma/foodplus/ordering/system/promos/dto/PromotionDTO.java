package ma.foodplus.ordering.system.promos.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class PromotionDTO {
    private Integer id;
    private String name;
    private String description;
    private String promoCode;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean active;
    private boolean exclusive;
    private String combinabilityGroup;
    private Integer priority;
    private BigDecimal minPurchaseAmount;
    private Set<Long> excludedProductIds;
    private Set<Long> excludedCategoryIds;
    private String customerGroup;
    private Integer maxUsagePerCustomer;
    private boolean nestedPromotion;
    private Map<Long, Integer> productPoints;
    private List<PromotionRuleDTO> rules;
    private List<PromotionLineDTO> promotionLines;
    private List<PromotionCustomerFamilyDTO> customerFamilies;
    private Set<String> excludedFamilyCodes;
    private Integer parentPromotionId;

    public boolean isActive(ZonedDateTime date) {
        return active && date.isAfter(startDate) && date.isBefore(endDate);
    }

    public boolean isWithinTimeRestriction() {
        ZonedDateTime now = ZonedDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public String getCombinabilityGroup() {
        return combinabilityGroup;
    }

    public void setCombinabilityGroup(String combinabilityGroup) {
        this.combinabilityGroup = combinabilityGroup;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public BigDecimal getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(BigDecimal minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public Set<Long> getExcludedProductIds() {
        return excludedProductIds;
    }

    public void setExcludedProductIds(Set<Long> excludedProductIds) {
        this.excludedProductIds = excludedProductIds;
    }

    public Set<Long> getExcludedCategoryIds() {
        return excludedCategoryIds;
    }

    public void setExcludedCategoryIds(Set<Long> excludedCategoryIds) {
        this.excludedCategoryIds = excludedCategoryIds;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public Integer getMaxUsagePerCustomer() {
        return maxUsagePerCustomer;
    }

    public void setMaxUsagePerCustomer(Integer maxUsagePerCustomer) {
        this.maxUsagePerCustomer = maxUsagePerCustomer;
    }

    public boolean isNestedPromotion() {
        return nestedPromotion;
    }

    public void setNestedPromotion(boolean nestedPromotion) {
        this.nestedPromotion = nestedPromotion;
    }

    public Map<Long, Integer> getProductPoints() {
        return productPoints;
    }

    public void setProductPoints(Map<Long, Integer> productPoints) {
        this.productPoints = productPoints;
    }

    public List<PromotionRuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<PromotionRuleDTO> rules) {
        this.rules = rules;
    }

    public List<PromotionLineDTO> getPromotionLines() {
        return promotionLines;
    }

    public void setPromotionLines(List<PromotionLineDTO> promotionLines) {
        this.promotionLines = promotionLines;
    }

    public List<PromotionCustomerFamilyDTO> getCustomerFamilies() {
        return customerFamilies;
    }

    public void setCustomerFamilies(List<PromotionCustomerFamilyDTO> customerFamilies) {
        this.customerFamilies = customerFamilies;
    }

    public Set<String> getExcludedFamilyCodes() {
        return excludedFamilyCodes;
    }

    public void setExcludedFamilyCodes(Set<String> excludedFamilyCodes) {
        this.excludedFamilyCodes = excludedFamilyCodes;
    }

    public Integer getParentPromotionId() {
        return parentPromotionId;
    }

    public void setParentPromotionId(Integer parentPromotionId) {
        this.parentPromotionId = parentPromotionId;
    }
} 