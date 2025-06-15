package ma.foodplus.ordering.system.promos.dto;

import lombok.Data;
import java.util.List;

@Data
public class PromotionRuleDTO {
    private Integer id;
    private Integer promotionId;
    private String name;
    private String description;
    private BreakpointType breakpointType;
    private CalculationMethod calculationMethod;
    private List<PromotionTierDTO> tiers;
    private List<ConditionDTO> conditions;

    public enum BreakpointType {
        AMOUNT,
        QUANTITY,
        SKU_POINTS
    }

    public enum CalculationMethod {
        BRACKET,
        CUMULATIVE
    }
} 