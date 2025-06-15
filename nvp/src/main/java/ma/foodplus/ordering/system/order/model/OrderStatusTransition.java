package ma.foodplus.ordering.system.order.model;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatusTransition {
    DRAFT(EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED)),
    CONFIRMED(EnumSet.of(OrderStatus.PREPARING, OrderStatus.CANCELLED)),
    PREPARING(EnumSet.of(OrderStatus.READY, OrderStatus.CANCELLED)),
    READY(EnumSet.of(OrderStatus.DELIVERING, OrderStatus.CANCELLED)),
    DELIVERING(EnumSet.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED)),
    DELIVERED(EnumSet.of(OrderStatus.REFUNDED)),
    CANCELLED(EnumSet.noneOf(OrderStatus.class)),
    REFUNDED(EnumSet.noneOf(OrderStatus.class));

    private final Set<OrderStatus> allowedTransitions;

    OrderStatusTransition(Set<OrderStatus> allowedTransitions) {
        this.allowedTransitions = allowedTransitions;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        return allowedTransitions.contains(newStatus);
    }

    public static boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        return currentStatus.getTransition().canTransitionTo(newStatus);
    }
} 