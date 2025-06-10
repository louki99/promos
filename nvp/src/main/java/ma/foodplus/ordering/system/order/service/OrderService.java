package ma.foodplus.ordering.system.order.service;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.model.OrderStatus;
import ma.foodplus.ordering.system.order.model.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    // Basic CRUD operations
    OrderDto createOrder(Long customerId, OrderType orderType);
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

    // B2B Specific Operations
    OrderDto validateBulkOrder(Long orderId);
    OrderDto applyContractPricing(Long orderId);
    OrderDto validateCreditLimit(Long orderId);
    OrderDto setPaymentTerms(Long orderId, String terms);
    OrderDto validateMinimumOrderValue(Long orderId);
    OrderDto applyBulkDiscount(Long orderId);
    OrderDto setDeliverySchedule(Long orderId, LocalDateTime preferredDeliveryDate);

    // B2C Specific Operations
    OrderDto applyLoyaltyPoints(Long orderId, Integer points);
    OrderDto applyPromoCode(Long orderId, String promoCode);
    OrderDto selectDeliveryTimeSlot(Long orderId, LocalDateTime timeSlot);
    OrderDto applyCustomerPreferences(Long orderId);
    OrderDto calculateDeliveryFee(Long orderId);
    OrderDto validateDeliveryAddress(Long orderId);

    // Common Operations
    OrderDto calculateTotals(Long orderId);
    OrderDto validateOrder(Long orderId);
    OrderDto getOrderHistory(Long customerId);
    OrderDto getOrderSummary(Long orderId);
    List<OrderDto> searchOrders(String searchTerm);
    List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
} 