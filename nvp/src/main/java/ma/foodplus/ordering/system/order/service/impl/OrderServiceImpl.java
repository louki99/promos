package ma.foodplus.ordering.system.order.service.impl;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.mapper.OrderMapper;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.order.model.OrderItem;
import ma.foodplus.ordering.system.order.model.OrderStatus;
import ma.foodplus.ordering.system.order.repository.OrderRepository;
import ma.foodplus.ordering.system.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderDto createOrder(Long customerId) {
        Order order = new Order(customerId);
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

    private OrderDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }
} 