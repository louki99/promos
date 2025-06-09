package ma.foodplus.ordering.system.order.service;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.model.OrderStatus;

import java.util.List;

public interface OrderService {
    // Basic CRUD operations
    OrderDto createOrder(Long customerId);
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    List<OrderDto> getOrdersByStatus(OrderStatus status);
    void deleteOrder(Long id);

    // Order items management
    OrderDto addItemToOrder(Long orderId, OrderItemDto itemDto);
    OrderDto removeItemFromOrder(Long orderId, Long itemId);
    OrderDto updateItemQuantity(Long orderId, Long itemId, Integer quantity);
    OrderDto updateItemNotes(Long orderId, Long itemId, String notes);

    // Order status transitions
    OrderDto confirmOrder(Long orderId);
    OrderDto startPreparing(Long orderId);
    OrderDto markAsReady(Long orderId);
    OrderDto startDelivery(Long orderId);
    OrderDto completeDelivery(Long orderId);
    OrderDto cancelOrder(Long orderId);
    OrderDto refundOrder(Long orderId);
} 