package ma.foodplus.ordering.system.order.service;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.model.OrderStatus;
import ma.foodplus.ordering.system.order.model.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    // Basic Order Operations
    OrderDto createOrder(Long customerId, OrderType orderType);
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    List<OrderDto> getOrdersByStatus(OrderStatus status);
    void deleteOrder(Long id);

    // Item Management
    OrderDto addItemToOrder(Long orderId, OrderItemDto itemDto);
    OrderDto removeItemFromOrder(Long orderId, Long itemId);
    OrderDto updateItemQuantity(Long orderId, Long itemId, Integer quantity);
    OrderDto updateItemNotes(Long orderId, Long itemId, String notes);

    // Order Status Management
    OrderDto confirmOrder(Long orderId);
    OrderDto startPreparing(Long orderId);
    OrderDto markAsReady(Long orderId);
    OrderDto startDelivery(Long orderId);
    OrderDto completeDelivery(Long orderId);
    OrderDto cancelOrder(Long orderId);
    OrderDto refundOrder(Long orderId);

    // B2B Specific Operations
    OrderDto validateCreditLimit(Long orderId);
    OrderDto applyContractPricing(Long orderId);
    OrderDto setBulkOrderDetails(Long orderId, String reference, String frequency, LocalDateTime scheduledDate);
    OrderDto validateBulkOrder(Long orderId);
    OrderDto setContractDetails(Long orderId, String contractId, LocalDateTime startDate, LocalDateTime endDate, String terms);
    OrderDto applySpecialPricing(Long orderId, String pricingTerms);

    // B2C Specific Operations
    OrderDto setDeliveryTimeSlot(Long orderId, String timeSlot);
    OrderDto confirmDeliveryTimeSlot(Long orderId);
    OrderDto applyLoyaltyPoints(Long orderId, Integer points);
    OrderDto calculateLoyaltyPointsEarned(Long orderId);
    OrderDto setContactDetails(Long orderId, String phone, String email);

    // Common Operations
    OrderDto validateMinimumOrderValue(Long orderId);
    OrderDto calculateTotals(Long orderId);
    OrderDto calculateDeliveryFee(Long orderId);
    OrderDto getOrderSummary(Long orderId);
    OrderDto validateDeliveryAddress(Long orderId);
    List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    OrderDto applyCustomerPreferences(Long orderId);
    OrderDto applyBulkDiscount(Long orderId);
    OrderDto applyPromoCode(Long orderId, String promoCode);
    OrderDto setPaymentTerms(Long orderId, String paymentTerms);
    OrderDto setDeliverySchedule(Long orderId, LocalDateTime deliveryTime);
    List<OrderDto> searchOrders(String searchTerm);
    OrderDto selectDeliveryTimeSlot(Long orderId, LocalDateTime timeSlot);
    OrderDto getOrderHistory(Long customerId);
    OrderDto validateOrder(Long orderId);
} 