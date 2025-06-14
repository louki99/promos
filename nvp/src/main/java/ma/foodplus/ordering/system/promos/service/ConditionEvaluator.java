package ma.foodplus.ordering.system.promos.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.order.model.OrderItem;
import ma.foodplus.ordering.system.order.model.OrderItemContext;
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
import java.util.*;
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
     * @param order       The current shopping order.
     * @param conditions The list of conditions to evaluate.
     * @param logic      The logic to apply (ALL conditions must be true, or ANY one condition must be true).
     * @return True if the conditions are met according to the logic, false otherwise.
     */
    public boolean evaluate(Order order,List<Condition> conditions,PromotionRule.ConditionLogic logic) {
        if (conditions == null || conditions.isEmpty()) {
            return true; // A rule with no conditions is always considered met.
        }

        if (logic == PromotionRule.ConditionLogic.ALL) {
            // ALL: Every single condition in the list must return true.
            return conditions.stream().allMatch(condition -> isSingleConditionMet(order, condition));
        } else {
            // ANY: At least one condition in the list must return true.
            return conditions.stream().anyMatch(condition -> isSingleConditionMet(order, condition));
        }
    }

    /**
     * Evaluates a single condition against the order's state.
     * This method acts as a router to the specific evaluation logic based on the condition type.
     *
     * @param order      The current shopping order.
     * @param condition The condition to evaluate.
     * @return True if the condition is met.
     */
    private boolean isSingleConditionMet(Order order, Condition condition) {
        // This switch is the key to extensibility. To add a new condition type,
        // you just add a new case here and a corresponding private method.
        switch (condition.getConditionType()) {
            case CART_SUBTOTAL:
                return evaluateCartSubtotal(order, condition);

            case PRODUCT_IN_CART:
                return evaluateProductInCart(order, condition);

            case CUSTOMER_IN_GROUP:
                return evaluateCustomerGroup(order, condition);

            case TIME_OF_DAY:
                return evaluateTimeOfDay(condition);

            case DAY_OF_WEEK:
                return evaluateDayOfWeek(condition);

            case CUSTOMER_LOYALTY_LEVEL:
                return evaluateCustomerLoyaltyLevel(order, condition);

            case PAYMENT_METHOD:
                return evaluatePaymentMethod(order, condition);

            default:
                // It's good practice to log a warning for unhandled condition types.
                // log.warn("Unhandled condition type: {}", condition.getConditionType());
                return false;
        }
    }

    // --- Private Helper Methods for Specific Condition Types ---

    private boolean evaluateCartSubtotal(Order order, Condition condition) {
        BigDecimal cartTotal = order.getTotal();
        BigDecimal expectedValue = new BigDecimal(condition.getValue());
        return checkValue(cartTotal, condition.getOperator(), expectedValue);
    }

    private boolean evaluateProductInCart(Order order, Condition condition) {
        List<OrderItemContext> matchingItems;

        // Find items based on whether the condition is for a single product or a family of products.
        if ("PRODUCT".equalsIgnoreCase(condition.getEntityType())) {
            matchingItems = order.findItemsByProductId(Long.valueOf(condition.getEntityId()))
                .stream().map(OrderItemContext::new).collect(Collectors.toList());
        } else if ("PRODUCT_FAMILY".equalsIgnoreCase(condition.getEntityType())) {
            matchingItems = order.findItemsByFamilyId(Long.valueOf(condition.getEntityId()))
                .stream().map(OrderItemContext::new).collect(Collectors.toList());
        } else {
            return false; // Invalid entity type for this condition
        }

        if (matchingItems.isEmpty()) {
            return false;
        }

        // The condition's value represents the required quantity.
        int totalQuantity = matchingItems.stream()
                .mapToInt(item -> item.getOriginalItem().getQuantity())
                .sum();
        int requiredQuantity = Integer.parseInt(condition.getValue());

        return checkValue(new BigDecimal(totalQuantity), condition.getOperator(), new BigDecimal(requiredQuantity));
    }

    private boolean evaluateCustomerGroup(Order order, Condition condition) {
        if (order.getCustomerId() == null || condition.getCustomerGroupId() == null) {
            return false;
        }
        return customerService.isCustomerInGroup(order.getCustomerId(), condition.getCustomerGroupId());
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

    private boolean evaluateCustomerLoyaltyLevel(Order order, Condition condition) {
        if (order.getCustomerId() == null) {
            return false;
        }
        int customerLevel = customerService.getCustomerLoyaltyLevel(order.getCustomerId());
        return customerLevel >= condition.getRequiredLoyaltyLevel();
    }

    private boolean evaluatePaymentMethod(Order order, Condition condition) {
        if (order.getPaymentMethod() == null || condition.getPaymentMethod() == null) {
            return false;
        }
        return order.getPaymentMethod().name().equals(condition.getPaymentMethod());
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
    public boolean evaluateDynamicCondition(DynamicCondition condition, Order order) {
        if (!condition.isActive() || order == null) {
            return false;
        }

        switch (condition.getConditionType().toUpperCase()) {
            case "CUSTOMER_LOYALTY":
            case "CUSTOMER_GROUP":
            case "PURCHASE_HISTORY":
                return evaluateCustomerBasedCondition(condition, order);
            case "ORDER_TOTAL":
                return condition.evaluateNumeric(order.getFinalTotalPrice().doubleValue());
            case "PAYMENT_METHOD":
                return condition.evaluate(order.getPaymentMethod().name());
            default:
                return false;
        }
    }

    private boolean evaluateCustomerBasedCondition(DynamicCondition condition, Order order) {
        if (order.getCustomerId() == null) {
            return false;
        }

        switch (condition.getConditionType().toUpperCase()) {
            case "CUSTOMER_LOYALTY":
                int customerLevel = customerService.getCustomerLoyaltyLevel(order.getCustomerId());
                return condition.evaluateNumeric((double) customerLevel);
            case "CUSTOMER_GROUP":
                return evaluatePurchaseHistory(condition, order.getCustomerId());
            case "PURCHASE_HISTORY":
                return evaluatePurchaseHistory(condition, order.getCustomerId());
            default:
                return false;
        }
    }

    private boolean evaluatePurchaseHistory(DynamicCondition condition, Long customerId) {
        BigDecimal totalSpent = customerService.getCustomerTotalSpent(customerId);
        return condition.evaluateNumeric(totalSpent.doubleValue());
    }

    private boolean evaluateProductBasedCondition(DynamicCondition condition, Order order) {
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
            List<String> productCategories = productService.getProductCategory(condition.getEntityId());
            return productCategories.contains(condition.getConditionValue());
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
            int currentStock = inventoryService.getProductStockLevel(Long.valueOf(condition.getEntityId()));
            return currentStock >= minStock;
        } catch (Exception e) {
            log.error("Error evaluating product stock level: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateOrderBasedCondition(DynamicCondition condition, Order order) {
        switch (condition.getOperator()) {
            case "TOTAL_ITEMS":
                return evaluateOrderTotalItems(condition);
            case "TOTAL_AMOUNT":
                return evaluateCartTotalAmount(condition);
            case "ITEM_CATEGORIES":
                return evaluateOrderItemCategories(condition);
            default:
                log.warn("Unknown cart condition operator: {}", condition.getOperator());
                return false;
        }
    }

    private boolean evaluateOrderTotalItems(DynamicCondition condition) {
        try {
            int requiredItems = Integer.parseInt(condition.getConditionValue());
            int totalItems = condition.getOrder().getItems().stream()
                    .map(OrderItemContext::new)
                    .mapToInt(item -> item.getOriginalItem().getQuantity())
                    .sum();
            return totalItems >= requiredItems;
        } catch (Exception e) {
            log.error("Error evaluating order total items: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateCartTotalAmount(DynamicCondition condition) {
        try {
            double requiredAmount = Double.parseDouble(condition.getConditionValue());
            double orderTotal = condition.getOrder().getFinalTotalPrice().doubleValue();
            return orderTotal >= requiredAmount;
        } catch (Exception e) {
            log.error("Error evaluating order total amount: {}", e.getMessage());
            return false;
        }
    }

    private boolean evaluateOrderItemCategories(DynamicCondition condition) {
        try {
            Set<String> requiredCategories = Arrays.stream(condition.getConditionValue().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            Set<String> orderCategories = condition.getOrder().getItems().stream()
                    .map(OrderItemContext::new)
                    .map(item -> {
                        try {
                            List<String> path = productService.getProductCategory(
                                item.getOriginalItem().getProductId().toString());
                            return path.get(0); // Get the first category in the path
                        } catch (Exception e) {
                            log.warn("Failed to get product category for item {}: {}", 
                                item.getOriginalItem().getId(), e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            return requiredCategories.stream().allMatch(orderCategories::contains);
        } catch (Exception e) {
            log.error("Error evaluating order item categories: {}", e.getMessage());
            return false;
        }
    }
}