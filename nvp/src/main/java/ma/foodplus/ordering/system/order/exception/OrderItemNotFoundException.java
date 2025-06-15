package ma.foodplus.ordering.system.order.exception;

import ma.foodplus.ordering.system.common.exception.BaseException;
import ma.foodplus.ordering.system.common.exception.ErrorCode;

public class OrderItemNotFoundException extends BaseException {
    public OrderItemNotFoundException(Long orderId, Long itemId) {
        super(ErrorCode.ORDER_ITEM_NOT_FOUND, 
              String.format("Order item with id %d not found in order %d", itemId, orderId));
    }
} 