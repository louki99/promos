package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.math.BigDecimal;
import java.util.HashMap;
import java.time.LocalTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String promoCode;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "customer_group")
    private String customerGroup;

    @Min(0)
    @Column(nullable = false)
    private int priority;

    @Column(nullable = false)
    private boolean isExclusive;

    private String combinabilityGroup;

    @Column(nullable = false)
    private boolean isActive;

    @Column(name = "apply_first_matching_rule_only")
    private boolean applyFirstMatchingRuleOnly;

    @Column(name = "max_usage_count")
    private Integer maxUsageCount;

    @Column(name = "current_usage_count")
    private Integer currentUsageCount;

    @Column(name = "max_usage_per_customer")
    private Integer maxUsagePerCustomer;

    @Column(name = "min_purchase_amount")
    private BigDecimal minPurchaseAmount;

    @ElementCollection
    @CollectionTable(name = "promotion_excluded_products", 
        joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "product_id")
    private List<Long> excludedProductIds;

    @ElementCollection
    @CollectionTable(name = "promotion_excluded_categories", 
        joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "category_id")
    private List<Long> excludedCategoryIds;

    @ElementCollection
    @CollectionTable(name = "promotion_customer_usage", 
        joinColumns = @JoinColumn(name = "promotion_id"))
    @MapKeyColumn(name = "customer_id")
    @Column(name = "usage_count")
    private Map<Long, Integer> customerUsageCounts;

    // علاقة One-to-Many مع القواعد الترويجية
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionRule> rules;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DynamicCondition> dynamicConditions;

    @Column(name = "skip_to_sequence")
    private Integer skipToSequence; // Aller à

    @Column(name = "index_discount")
    private Integer indexDiscount; // Index Discount

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionCustomerFamily> customerFamilies;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionLine> promotionLines;

    @Column(name = "is_nested_promotion")
    private boolean isNestedPromotion;

    @Column(name = "parent_promotion_id")
    private Long parentPromotionId;

    @Column(name = "nested_level")
    private Integer nestedLevel;

    @ElementCollection
    @CollectionTable(name = "promotion_product_points", 
        joinColumns = @JoinColumn(name = "promotion_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "points")
    private Map<Long, Integer> productPoints;

    @Column(name = "time_restricted")
    private boolean timeRestricted;

    @Column(name = "start_time")
    private String startTime; // Format: "HH:mm"

    @Column(name = "end_time")
    private String endTime; // Format: "HH:mm"

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reward> rewards = new HashSet<>();

    // Constructors
    public Promotion() {
    }

    public Promotion(String promoCode, String name, ZonedDateTime startDate, ZonedDateTime endDate) {
        this.promoCode = promoCode;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = true;
        validateDates();
    }

    // Business Logic Methods
    private void validateDates() {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    public boolean isActive(ZonedDateTime now) {
        return isActive && startDate.isBefore(now) && endDate.isAfter(now);
    }

    public boolean canBeCombinedWith(Promotion other) {
        if (this.isExclusive || other.isExclusive) {
            return false;
        }
        if (this.combinabilityGroup != null && other.combinabilityGroup != null) {
            return this.combinabilityGroup.equals(other.combinabilityGroup);
        }
        return true;
    }

    public boolean hasValidRules() {
        return rules != null && !rules.isEmpty();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
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

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        validateDates();
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        validateDates();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isExclusive() {
        return isExclusive;
    }

    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }

    public String getCombinabilityGroup() {
        return combinabilityGroup;
    }

    public void setCombinabilityGroup(String combinabilityGroup) {
        this.combinabilityGroup = combinabilityGroup;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isApplyFirstMatchingRuleOnly() {
        return applyFirstMatchingRuleOnly;
    }

    public void setApplyFirstMatchingRuleOnly(boolean applyFirstMatchingRuleOnly) {
        this.applyFirstMatchingRuleOnly = applyFirstMatchingRuleOnly;
    }

    public List<PromotionRule> getRules() {
        return rules;
    }

    public void setRules(List<PromotionRule> rules) {
        this.rules = rules;
    }

    public List<DynamicCondition> getDynamicConditions() {
        return dynamicConditions;
    }

    public void setDynamicConditions(List<DynamicCondition> dynamicConditions) {
        this.dynamicConditions = dynamicConditions;
    }

    public Integer getMaxUsageCount() {
        return maxUsageCount;
    }

    public void setMaxUsageCount(Integer maxUsageCount) {
        this.maxUsageCount = maxUsageCount;
    }

    public Integer getCurrentUsageCount() {
        return currentUsageCount;
    }

    public void setCurrentUsageCount(Integer currentUsageCount) {
        this.currentUsageCount = currentUsageCount;
    }

    public Integer getMaxUsagePerCustomer() {
        return maxUsagePerCustomer;
    }

    public void setMaxUsagePerCustomer(Integer maxUsagePerCustomer) {
        this.maxUsagePerCustomer = maxUsagePerCustomer;
    }

    public BigDecimal getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(BigDecimal minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public List<Long> getExcludedProductIds() {
        return excludedProductIds;
    }

    public void setExcludedProductIds(List<Long> excludedProductIds) {
        this.excludedProductIds = excludedProductIds;
    }

    public List<Long> getExcludedCategoryIds() {
        return excludedCategoryIds;
    }

    public void setExcludedCategoryIds(List<Long> excludedCategoryIds) {
        this.excludedCategoryIds = excludedCategoryIds;
    }

    public Map<Long, Integer> getCustomerUsageCounts() {
        return customerUsageCounts;
    }

    public void setCustomerUsageCounts(Map<Long, Integer> customerUsageCounts) {
        this.customerUsageCounts = customerUsageCounts;
    }

    public int getCustomerUsageCount(Long customerId) {
        return customerUsageCounts != null ? 
            customerUsageCounts.getOrDefault(customerId, 0) : 0;
    }

    public void incrementCustomerUsage(Long customerId) {
        if (customerUsageCounts == null) {
            customerUsageCounts = new HashMap<>();
        }
        customerUsageCounts.merge(customerId, 1, Integer::sum);
        if (currentUsageCount != null) {
            currentUsageCount++;
        }
    }

    public Integer getSkipToSequence() {
        return skipToSequence;
    }

    public void setSkipToSequence(Integer skipToSequence) {
        this.skipToSequence = skipToSequence;
    }

    public Integer getIndexDiscount() {
        return indexDiscount;
    }

    public void setIndexDiscount(Integer indexDiscount) {
        this.indexDiscount = indexDiscount;
    }

    public List<PromotionCustomerFamily> getCustomerFamilies() {
        return customerFamilies;
    }

    public void setCustomerFamilies(List<PromotionCustomerFamily> customerFamilies) {
        this.customerFamilies = customerFamilies;
    }

    public List<PromotionLine> getPromotionLines() {
        return promotionLines;
    }

    public void setPromotionLines(List<PromotionLine> promotionLines) {
        this.promotionLines = promotionLines;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public boolean isNestedPromotion() {
        return isNestedPromotion;
    }

    public void setNestedPromotion(boolean nestedPromotion) {
        isNestedPromotion = nestedPromotion;
    }

    public Long getParentPromotionId() {
        return parentPromotionId;
    }

    public void setParentPromotionId(Long parentPromotionId) {
        this.parentPromotionId = parentPromotionId;
    }

    public Integer getNestedLevel() {
        return nestedLevel;
    }

    public void setNestedLevel(Integer nestedLevel) {
        this.nestedLevel = nestedLevel;
    }

    public Map<Long, Integer> getProductPoints() {
        return productPoints;
    }

    public void setProductPoints(Map<Long, Integer> productPoints) {
        this.productPoints = productPoints;
    }

    public boolean isTimeRestricted() {
        return timeRestricted;
    }

    public void setTimeRestricted(boolean timeRestricted) {
        this.timeRestricted = timeRestricted;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isWithinTimeRestriction() {
        if (!timeRestricted || startTime == null || endTime == null) {
            return true;
        }
        
        LocalTime now = LocalTime.now();
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        
        return !now.isBefore(start) && !now.isAfter(end);
    }

    public Set<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(Set<Reward> rewards) {
        this.rewards = rewards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promotion promotion = (Promotion) o;
        return Objects.equals(id, promotion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", promoCode='" + promoCode + '\'' +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}