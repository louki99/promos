package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.component.Cart;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.stream.Collectors;

/**
 * Service responsible for evaluating if a set of promotional conditions are met for a given cart.
 * This service is decoupled from reward application, focusing solely on condition checking.
 */
@Service
public class ConditionEvaluator {

    /**
     * Evaluates a list of conditions based on the provided logic (ALL or ANY).
     *
     * @param cart       The current shopping cart.
     * @param conditions The list of conditions to evaluate.
     * @param logic      The logic to apply (ALL conditions must be true, or ANY one condition must be true).
     * @return True if the conditions are met according to the logic, false otherwise.
     */
    public boolean evaluate(Cart cart,List<Condition> conditions,PromotionRule.ConditionLogic logic) {
        if (conditions == null || conditions.isEmpty()) {
            return true; // A rule with no conditions is always considered met.
        }

        if (logic == PromotionRule.ConditionLogic.ALL) {
            // ALL: Every single condition in the list must return true.
            return conditions.stream().allMatch(condition -> isSingleConditionMet(cart, condition));
        } else {
            // ANY: At least one condition in the list must return true.
            return conditions.stream().anyMatch(condition -> isSingleConditionMet(cart, condition));
        }
    }

    /**
     * Evaluates a single condition against the cart's state.
     * This method acts as a router to the specific evaluation logic based on the condition type.
     *
     * @param cart      The current shopping cart.
     * @param condition The condition to evaluate.
     * @return True if the condition is met.
     */
    private boolean isSingleConditionMet(Cart cart, Condition condition) {
        // This switch is the key to extensibility. To add a new condition type,
        // you just add a new case here and a corresponding private method.
        switch (condition.getConditionType()) {
            case CART_SUBTOTAL:
                return evaluateCartSubtotal(cart, condition);

            case PRODUCT_IN_CART:
                return evaluateProductInCart(cart, condition);

            case CUSTOMER_IN_GROUP:
                // This would require fetching customer data.
                // return evaluateCustomerGroup(cart.getCustomerId(), condition);
                return true; // Placeholder for now

            default:
                // It's good practice to log a warning for unhandled condition types.
                // log.warn("Unhandled condition type: {}", condition.getConditionType());
                return false;
        }
    }

    // --- Private Helper Methods for Specific Condition Types ---

    private boolean evaluateCartSubtotal(Cart cart, Condition condition) {
        BigDecimal cartTotal = cart.getFinalTotalPrice();
        BigDecimal expectedValue = new BigDecimal(condition.getValue());
        return checkValue(cartTotal, condition.getOperator(), expectedValue);
    }

    private boolean evaluateProductInCart(Cart cart, Condition condition) {
        List<CartItemContext> matchingItems;

        // Find items based on whether the condition is for a single product or a family of products.
        if ("PRODUCT".equalsIgnoreCase(condition.getEntityType())) {
            matchingItems = cart.findItemsByProductId(Long.valueOf(condition.getEntityId()));
        } else if ("PRODUCT_FAMILY".equalsIgnoreCase(condition.getEntityType())) {
            matchingItems = cart.findItemsByFamilyId(Long.valueOf(condition.getEntityId()));
        } else {
            return false; // Invalid entity type for this condition
        }

        if (matchingItems.isEmpty()) {
            return false;
        }

        // The condition's value represents the required quantity.
        int totalQuantity = matchingItems.stream().mapToInt(item -> item.getOriginalItem().getQuantity()).sum();
        int requiredQuantity = Integer.parseInt(condition.getValue());

        // Use a BigDecimal for comparison to be consistent, even with integers.
        return checkValue(new BigDecimal(totalQuantity), condition.getOperator(), new BigDecimal(requiredQuantity));
    }

    /**
     * A generic comparison utility for BigDecimal values.
     *
     * @param actualValue    The actual value from the cart.
     * @param operator       The comparison operator (e.g., GREATER_THAN_OR_EQUAL).
     * @param expectedValue  The value defined in the condition.
     * @return True if the comparison is successful.
     */
    private boolean checkValue(BigDecimal actualValue, Condition.Operator operator, BigDecimal expectedValue) {
        int comparisonResult = actualValue.compareTo(expectedValue);
        switch (operator) {
            case GREATER_THAN_OR_EQUAL:
                return comparisonResult >= 0;
            case EQUAL:
                return comparisonResult == 0;
            case LESS_THAN:
                return comparisonResult < 0;
            // Add other operators like GREATER_THAN, LESS_THAN_OR_EQUAL as needed.
            default:
                return false;
        }
    }
}