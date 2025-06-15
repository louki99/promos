package ma.foodplus.ordering.system.promos.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ConditionDTO {
    private Integer id;
    private ConditionType conditionType;
    private ConditionLogic logic;
    private String entityId;
    private BigDecimal threshold;
    private String operator;

    public enum ConditionType {
        CART_SUBTOTAL,
        MINIMUM_AMOUNT,
        MINIMUM_QUANTITY,
        PRODUCT_IN_CART,
        CATEGORY_IN_CART,
        CUSTOMER_GROUP,
        TIME_OF_DAY,
        DAY_OF_WEEK,
        CUSTOMER_LOYALTY_LEVEL,
        PAYMENT_METHOD
    }

    public enum ConditionLogic {
        AND,
        OR
    }
} 