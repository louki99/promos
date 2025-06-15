package ma.foodplus.ordering.system.order.exception;

import ma.foodplus.ordering.system.order.model.OrderStatus;

public class InvalidOrderStatusTransitionException extends RuntimeException {
    public InvalidOrderStatusTransitionException(OrderStatus currentStatus, OrderStatus newStatus) {
        super(String.format("Cannot transition order from %s to %s", currentStatus, newStatus));
    }
} 