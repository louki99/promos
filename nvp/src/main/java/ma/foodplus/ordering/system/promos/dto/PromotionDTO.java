package ma.foodplus.ordering.system.promos.dto;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public class PromotionDTO {
    private Integer id;
    private String promoCode;
    private String name;
    private String description;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private int priority;
    private boolean isExclusive;
    private String combinabilityGroup;
    private boolean isActive;
    private List<PromotionRuleDTO> rules;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
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

    public List<PromotionRuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<PromotionRuleDTO> rules) {
        this.rules = rules;
    }
} 