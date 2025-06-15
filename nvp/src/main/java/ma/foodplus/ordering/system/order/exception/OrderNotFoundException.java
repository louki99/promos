package ma.foodplus.ordering.system.order.exception;

import ma.foodplus.ordering.system.common.exception.BaseException;
import ma.foodplus.ordering.system.common.exception.ErrorCode;

public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException(Long orderId) {
        super(ErrorCode.ORDER_NOT_FOUND, String.format("Order not found with id: %d", orderId));
    }
} 