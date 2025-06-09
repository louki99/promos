package ma.foodplus.ordering.system.promos.dto;

import ma.foodplus.ordering.system.promos.model.PromotionRule;
import java.util.List;

public class PromotionRuleDTO {
    private Integer id;
    private String name;
    private PromotionRule.ConditionLogic conditionLogic;
    private PromotionRule.CalculationMethod calculationMethod;
    private PromotionRule.BreakpointType breakpointType;
    private List<ConditionDTO> conditions;
    private List<PromotionTierDTO> tiers;

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

    public PromotionRule.ConditionLogic getConditionLogic() {
        return conditionLogic;
    }

    public void setConditionLogic(PromotionRule.ConditionLogic conditionLogic) {
        this.conditionLogic = conditionLogic;
    }

    public PromotionRule.CalculationMethod getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(PromotionRule.CalculationMethod calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public PromotionRule.BreakpointType getBreakpointType() {
        return breakpointType;
    }

    public void setBreakpointType(PromotionRule.BreakpointType breakpointType) {
        this.breakpointType = breakpointType;
    }

    public List<ConditionDTO> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionDTO> conditions) {
        this.conditions = conditions;
    }

    public List<PromotionTierDTO> getTiers() {
        return tiers;
    }

    public void setTiers(List<PromotionTierDTO> tiers) {
        this.tiers = tiers;
    }
} 