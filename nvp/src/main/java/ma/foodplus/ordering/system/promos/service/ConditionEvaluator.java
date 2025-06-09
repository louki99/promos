package ma.foodplus.ordering.system.promos.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.promos.component.Cart;
import ma.foodplus.ordering.system.promos.component.CartItemContext;
import ma.foodplus.ordering.system.promos.model.Condition;
import ma.foodplus.ordering.system.promos.model.DynamicCondition;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.customer.service.CustomerService;
import ma.foodplus.ordering.system.product.service.ProductService;
import ma.foodplus.ordering.system.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service responsible for evaluating if a set of promotional conditions are met for a given cart.
 * This service is decoupled from reward application, focusing solely on condition checking.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConditionEvaluator {

    private final CustomerService customerService;
    private final ProductService productService;
    private final InventoryService inventoryService;

    /**
     * Evaluates a list of conditions based on the provided logic (ALL or ANY).
     *
     * @param cart       The current shopping cart.
     * @param conditions The list of conditions to evaluate.
     * @param logic      The logic to apply (ALL conditions must be true, or ANY one condition must be true).
     * @return True if the conditions are met according to the logic, false otherwise.
     */
    public boolean evaluate(Cart cart, List<Condition> conditions, PromotionRule.ConditionLogic logic) {
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
                return evaluateCustomerGroup(cart, condition);

            case TIME_OF_DAY:
                return evaluateTimeOfDay(condition);

            case DAY_OF_WEEK:
                return evaluateDayOfWeek(condition);

            case CUSTOMER_LOYALTY_LEVEL:
                return evaluateCustomerLoyaltyLevel(cart, condition);

            case PAYMENT_METHOD:
                return evaluatePaymentMethod(cart, condition);

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

    private boolean evaluateCustomerGroup(Cart cart, Condition condition) {
        // This would typically involve a service call to check customer group membership
        // For now, we'll return true as a placeholder
        return true;
    }

    private boolean evaluateTimeOfDay(Condition condition) {
        LocalTime currentTime = ZonedDateTime.now().toLocalTime();
        LocalTime targetTime = LocalTime.parse(condition.getValue());
        
        switch (condition.getOperator()) {
            case GREATER_THAN:
                return currentTime.isAfter(targetTime);
            case LESS_THAN:
                return currentTime.isBefore(targetTime);
            case EQUAL:
                return currentTime.equals(targetTime);
            default:
                return false;
        }
    }

    private boolean evaluateDayOfWeek(Condition condition) {
        int currentDay = ZonedDateTime.now().getDayOfWeek().getValue();
        int targetDay = Integer.parseInt(condition.getValue());
        
        switch (condition.getOperator()) {
            case EQUAL:
                return currentDay == targetDay;
            case NOT_EQUAL:
                return currentDay != targetDay;
            default:
                return false;
        }
    }

    private boolean evaluateCustomerLoyaltyLevel(Cart cart, Condition condition) {
        // This would typically involve a service call to check customer loyalty level
        // For now, we'll return true as a placeholder
        return true;
    }

    private boolean evaluatePaymentMethod(Cart cart, Condition condition) {
        // This would typically involve checking the selected payment method
        // For now, we'll return true as a placeholder
        return true;
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
            case EQUAL:
                return comparisonResult == 0;
            case NOT_EQUAL:
                return comparisonResult != 0;
            case GREATER_THAN:
                return comparisonResult > 0;
            case GREATER_THAN_OR_EQUAL:
                return comparisonResult >= 0;
            case LESS_THAN:
                return comparisonResult < 0;
            case LESS_THAN_OR_EQUAL:
                return comparisonResult <= 0;
            default:
                return false;
        }
    }

    /**
     * Evaluates a dynamic condition based on its type and value.
     * @param condition The dynamic condition to evaluate
     * @return true if the condition is met, false otherwise
     */
    public boolean evaluateDynamicCondition(DynamicCondition condition) {
        if (!condition.isActive()) {
            return false;
        }

        switch (condition.getConditionType()) {
            case "TIME_BASED":
                return evaluateTimeBasedCondition(condition);
            case "CUSTOMER_BASED":
                return evaluateCustomerBasedCondition(condition);
            case "PRODUCT_BASED":
                return evaluateProductBasedCondition(condition);
            case "CART_BASED":
                return evaluateCartBasedCondition(condition);
            default:
                log.warn("Unknown dynamic condition type: {}", condition.getConditionType());
                return false;
        }
    }

    private boolean evaluateTimeBasedCondition(DynamicCondition condition) {
        ZonedDateTime now = ZonedDateTime.now();
        String[] timeRange = condition.getConditionValue().split("-");
        
        if (timeRange.length != 2) {
            log.warn("Invalid time range format in condition: {}", condition.getConditionValue());
            return false;
        }

        try {
            ZonedDateTime startTime = ZonedDateTime.parse(timeRange[0]);
            ZonedDateTime endTime = ZonedDateTime.parse(timeRange[1]);
            
            return now.isAfter(startTime) && now.isBefore(endTime);
        } catch (Exception e) {
            log.error("Error parsing time range: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateCustomerBasedCondition(DynamicCondition condition) {
        switch (condition.getOperator()) {
            case "LOYALTY_LEVEL":
                return evaluateCustomerLoyaltyLevel(condition);
            case "PURCHASE_HISTORY":
                return evaluatePurchaseHistory(condition);
            case "CUSTOMER_GROUP":
                return evaluateCustomerGroup(condition);
            default:
                log.warn("Unknown customer condition operator: {}", condition.getOperator());
                return false;
        }
    }

    private boolean evaluateCustomerLoyaltyLevel(DynamicCondition condition) {
        try {
            int requiredLevel = Integer.parseInt(condition.getConditionValue());
            int customerLevel = customerService.getCustomerLoyaltyLevel(condition.getEntityId());
            return customerLevel >= requiredLevel;
        } catch (Exception e) {
            log.error("Error evaluating customer loyalty level: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluatePurchaseHistory(DynamicCondition condition) {
        try {
            String[] params = condition.getConditionValue().split(",");
            if (params.length != 2) {
                return false;
            }
            int minPurchases = Integer.parseInt(params[0]);
            double minAmount = Double.parseDouble(params[1]);

            Map<String, Object> purchaseHistory = customerService.getCustomerPurchaseHistory(condition.getEntityId());
            int totalPurchases = (int) purchaseHistory.get("totalPurchases");
            double totalAmount = (double) purchaseHistory.get("totalAmount");

            return totalPurchases >= minPurchases && totalAmount >= minAmount;
        } catch (Exception e) {
            log.error("Error evaluating purchase history: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateCustomerGroup(DynamicCondition condition) {
        try {
            Set<String> customerGroups = customerService.getCustomerGroups(condition.getEntityId());
            return customerGroups.contains(condition.getConditionValue());
        } catch (Exception e) {
            log.error("Error evaluating customer group: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateProductBasedCondition(DynamicCondition condition) {
        switch (condition.getOperator()) {
            case "CATEGORY":
                return evaluateProductCategory(condition);
            case "PRICE_RANGE":
                return evaluateProductPriceRange(condition);
            case "STOCK_LEVEL":
                return evaluateProductStockLevel(condition);
            default:
                log.warn("Unknown product condition operator: {}", condition.getOperator());
                return false;
        }
    }

    private boolean evaluateProductCategory(DynamicCondition condition) {
        try {
            String productCategory = productService.getProductCategory(condition.getEntityId());
            return productCategory.equals(condition.getConditionValue());
        } catch (Exception e) {
            log.error("Error evaluating product category: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateProductPriceRange(DynamicCondition condition) {
        try {
            String[] range = condition.getConditionValue().split("-");
            if (range.length != 2) {
                return false;
            }
            double minPrice = Double.parseDouble(range[0]);
            double maxPrice = Double.parseDouble(range[1]);
            double productPrice = productService.getProductPrice(condition.getEntityId());

            return productPrice >= minPrice && productPrice <= maxPrice;
        } catch (Exception e) {
            log.error("Error evaluating product price range: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateProductStockLevel(DynamicCondition condition) {
        try {
            int minStock = Integer.parseInt(condition.getConditionValue());
            int currentStock = inventoryService.getProductStockLevel(condition.getEntityId());
            return currentStock >= minStock;
        } catch (Exception e) {
            log.error("Error evaluating product stock level: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateCartBasedCondition(DynamicCondition condition) {
        switch (condition.getOperator()) {
            case "TOTAL_ITEMS":
                return evaluateCartTotalItems(condition);
            case "TOTAL_AMOUNT":
                return evaluateCartTotalAmount(condition);
            case "ITEM_CATEGORIES":
                return evaluateCartItemCategories(condition);
            default:
                log.warn("Unknown cart condition operator: {}", condition.getOperator());
                return false;
        }
    }

    private boolean evaluateCartTotalItems(DynamicCondition condition) {
        try {
            int requiredItems = Integer.parseInt(condition.getConditionValue());
            int totalItems = condition.getCart().getItems().stream()
                    .mapToInt(item -> item.getOriginalItem().getQuantity())
                    .sum();
            return totalItems >= requiredItems;
        } catch (Exception e) {
            log.error("Error evaluating cart total items: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateCartTotalAmount(DynamicCondition condition) {
        try {
            double requiredAmount = Double.parseDouble(condition.getConditionValue());
            double cartTotal = condition.getCart().getFinalTotalPrice().doubleValue();
            return cartTotal >= requiredAmount;
        } catch (Exception e) {
            log.error("Error evaluating cart total amount: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateCartItemCategories(DynamicCondition condition) {
        try {
            String[] requiredCategories = condition.getConditionValue().split(",");
            Set<String> cartCategories = condition.getCart().getItems().stream()
                    .map(item -> productService.getProductCategory(item.getOriginalItem().getProductId()))
                    .collect(Collectors.toSet());
            
            return Set.of(requiredCategories).stream()
                    .allMatch(cartCategories::contains);
        } catch (Exception e) {
            log.error("Error evaluating cart item categories: {}", e.getMessage());
            return false;
        }
    }
}