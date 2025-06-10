package ma.foodplus.ordering.system.order.service.impl;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.mapper.OrderMapper;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.order.model.OrderItem;
import ma.foodplus.ordering.system.order.model.OrderStatus;
import ma.foodplus.ordering.system.order.model.OrderType;
import ma.foodplus.ordering.system.order.repository.OrderRepository;
import ma.foodplus.ordering.system.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDto createOrder(Long customerId, OrderType orderType) {
        Order order = new Order(customerId);
        order.setOrderType(orderType);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
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
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        OrderItem item = orderMapper.toOrderItem(itemDto);
        order.addItem(item);
        
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto removeItemFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(order::removeItem);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateItemQuantity(Long orderId, Long itemId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> order.updateItemQuantity(item, quantity));

        return orderMapper.toDto(orderRepository.save(order));
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
        return updateOrderStatus(orderId, OrderStatus.CONFIRMED);
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
        return updateOrderStatus(orderId, OrderStatus.DELIVERING);
    }

    @Override
    public OrderDto completeDelivery(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    @Override
    public OrderDto cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Override
    public OrderDto refundOrder(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.REFUNDED);
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
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Validate credit limit logic here
        return orderMapper.toDto(order);
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

    private OrderDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }
} 