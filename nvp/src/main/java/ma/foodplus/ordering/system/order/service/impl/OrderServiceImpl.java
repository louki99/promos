package ma.foodplus.ordering.system.order.service.impl;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.exception.InvalidOrderStatusTransitionException;
import ma.foodplus.ordering.system.order.exception.OrderItemNotFoundException;
import ma.foodplus.ordering.system.order.exception.OrderNotFoundException;
import ma.foodplus.ordering.system.order.mapper.OrderMapper;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.order.model.OrderItem;
import ma.foodplus.ordering.system.order.model.OrderStatus;
import ma.foodplus.ordering.system.order.model.OrderType;
import ma.foodplus.ordering.system.order.repository.OrderRepository;
import ma.foodplus.ordering.system.order.service.OrderService;
import ma.foodplus.ordering.system.common.exception.BaseException;
import ma.foodplus.ordering.system.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private static final DateTimeFormatter ORDER_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    private String generateOrderNumber(OrderType orderType) {
        String prefix = orderType == OrderType.B2B ? "B2B" : "B2C";
        String timestamp = LocalDateTime.now().format(ORDER_NUMBER_FORMAT);
        return String.format("%s-%s", prefix, timestamp);
    }

    @Override
    public OrderDto createOrder(Long customerId, OrderType orderType) {
        Order order = new Order(customerId);
        order.setOrderType(orderType);
        order.setOrderNumber(generateOrderNumber(orderType));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderDto addItemToOrder(Long orderId, OrderItemDto itemDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderItem item = orderMapper.toOrderItem(itemDto);
        order.addItem(item);
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto removeItemFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(order::removeItem);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateItemQuantity(Long orderId, Long itemId, Integer quantity) {
        // Validate quantity
        if (quantity == null || quantity <= 0) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, 
                String.format("Invalid quantity: %d. Quantity must be greater than 0", quantity));
        }

        // First validate the order exists
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // Then validate the item exists in the order
        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFoundException(orderId, itemId));

        try {
            // Update the quantity
            order.updateItemQuantity(item, quantity);
            order.validateOrderState(); // Validate order state after update
            return orderMapper.toDto(orderRepository.save(order));
        } catch (IllegalStateException e) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, e.getMessage());
        } catch (Exception e) {
            throw new BaseException(ErrorCode.SYSTEM_ERROR, 
                String.format("Failed to update quantity for item %d in order %d: %s", 
                    itemId, orderId, e.getMessage()));
        }
    }

    @Override
    public OrderDto updateItemNotes(Long orderId, Long itemId, String notes) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> item.setNotes(notes));

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        try {
            order.validateOrderState();
            order.setStatus(OrderStatus.CONFIRMED);
            return orderMapper.toDto(orderRepository.save(order));
        } catch (IllegalStateException e) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, e.getMessage());
        } catch (InvalidOrderStatusTransitionException e) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, e.getMessage());
        }
    }

    @Override
    public OrderDto startPreparing(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.PREPARING);
    }

    @Override
    public OrderDto markAsReady(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.READY);
    }

    @Override
    public OrderDto startDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getShippingAddress() == null) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, 
                "Cannot start delivery without shipping address");
        }
        
        return updateOrderStatus(orderId, OrderStatus.DELIVERING);
    }

    @Override
    public OrderDto completeDelivery(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    @Override
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        try {
            order.cancel("Order cancelled by user");
            return orderMapper.toDto(orderRepository.save(order));
        } catch (InvalidOrderStatusTransitionException e) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, e.getMessage());
        }
    }

    @Override
    public OrderDto refundOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        try {
            order.refund("Order refunded by user");
            return orderMapper.toDto(orderRepository.save(order));
        } catch (InvalidOrderStatusTransitionException e) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, e.getMessage());
        }
    }

    @Override
    public OrderDto validateMinimumOrderValue(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (!order.meetsMinimumOrderValue()) {
            throw new RuntimeException("Order does not meet minimum order value requirement");
        }
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto calculateTotals(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.recalculateTotals();
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryFee(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Example delivery fee calculation
        BigDecimal deliveryFee = order.getSubtotal().multiply(BigDecimal.valueOf(0.1));
        order.setShippingCost(deliveryFee);
        order.recalculateTotals();
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto getOrderSummary(Long orderId) {
        return getOrderById(orderId);
    }

    @Override
    public OrderDto validateDeliveryAddress(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (order.getShippingAddress() == null || order.getShippingAddress().isEmpty()) {
            throw new RuntimeException("Delivery address is required");
        }
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto applyCustomerPreferences(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Apply customer preferences logic here
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto applyContractPricing(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Apply contract pricing logic here
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto applyBulkDiscount(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Apply bulk discount logic here
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto validateCreditLimit(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        try {
            order.validateCreditLimit();
            return orderMapper.toDto(order);
        } catch (IllegalStateException e) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, e.getMessage());
        }
    }

    @Override
    public OrderDto applyPromoCode(Long orderId, String promoCode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Apply promo code logic here
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setPaymentTerms(Long orderId, String paymentTerms) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setPaymentTerms(paymentTerms);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setDeliverySchedule(Long orderId, LocalDateTime deliveryTime) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setPreferredDeliveryDate(deliveryTime.atZone(java.time.ZoneId.systemDefault()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> searchOrders(String searchTerm) {
        return orderRepository.searchOrders(searchTerm).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto validateBulkOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Validate bulk order logic here
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto selectDeliveryTimeSlot(Long orderId, LocalDateTime timeSlot) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setPreferredDeliveryDate(timeSlot.atZone(java.time.ZoneId.systemDefault()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto getOrderHistory(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orderMapper.toDto(orders.get(orders.size() - 1)); // Return the most recent order
    }

    @Override
    public OrderDto applyLoyaltyPoints(Long orderId, Integer points) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Apply loyalty points logic here
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto validateOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Validate order logic here
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto setBulkOrderDetails(Long orderId, String reference, String frequency, LocalDateTime scheduledDate) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getOrderType() != OrderType.B2B) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, "Bulk order details can only be set for B2B orders");
        }

        order.setBulkOrder(true);
        order.setBulkOrderReference(reference);
        order.setDeliveryScheduleFrequency(frequency);
        order.setScheduledDeliveryDate(scheduledDate.atZone(java.time.ZoneId.systemDefault()));
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setContractDetails(Long orderId, String contractId, LocalDateTime startDate, LocalDateTime endDate, String terms) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getOrderType() != OrderType.B2B) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, "Contract details can only be set for B2B orders");
        }

        order.setContractId(contractId);
        order.setContractStartDate(startDate.atZone(java.time.ZoneId.systemDefault()));
        order.setContractEndDate(endDate.atZone(java.time.ZoneId.systemDefault()));
        order.setContractTerms(terms);
        order.setSpecialPricingAgreement(true);
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto applySpecialPricing(Long orderId, String pricingTerms) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getOrderType() != OrderType.B2B) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, "Special pricing can only be applied to B2B orders");
        }

        order.setSpecialPricingTerms(pricingTerms);
        order.setSpecialPricingAgreement(true);
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setDeliveryTimeSlot(Long orderId, String timeSlot) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getOrderType() != OrderType.B2C) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, "Delivery time slots are only available for B2C orders");
        }

        order.setPreferredDeliveryTimeSlot(timeSlot);
        order.setDeliveryTimeSlotConfirmed(false);
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto confirmDeliveryTimeSlot(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getOrderType() != OrderType.B2C) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, "Delivery time slots are only available for B2C orders");
        }

        if (order.getPreferredDeliveryTimeSlot() == null) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, "No delivery time slot has been set");
        }

        order.setDeliveryTimeSlotConfirmed(true);
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setContactDetails(Long orderId, String phone, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getOrderType() != OrderType.B2C) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, "Contact details are only required for B2C orders");
        }

        order.setContactPhone(phone);
        order.setContactEmail(email);
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateLoyaltyPointsEarned(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getOrderType() != OrderType.B2C) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, "Loyalty points are only available for B2C orders");
        }

        order.calculateLoyaltyPointsEarned();
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    private OrderDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        try {
            order.setStatus(newStatus);
            return orderMapper.toDto(orderRepository.save(order));
        } catch (InvalidOrderStatusTransitionException e) {
            throw new BaseException(ErrorCode.VALIDATION_ERROR, e.getMessage());
        }
    }
} 