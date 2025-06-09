package ma.foodplus.ordering.system.order.controller;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.model.OrderStatus;
import ma.foodplus.ordering.system.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestParam Long customerId) {
        return ResponseEntity.ok(orderService.createOrder(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // Order items management endpoints
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderDto> addItemToOrder(
            @PathVariable Long orderId,
            @RequestBody OrderItemDto itemDto) {
        return ResponseEntity.ok(orderService.addItemToOrder(orderId, itemDto));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderDto> removeItemFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.removeItemFromOrder(orderId, itemId));
    }

    @PatchMapping("/{orderId}/items/{itemId}/quantity")
    public ResponseEntity<OrderDto> updateItemQuantity(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(orderService.updateItemQuantity(orderId, itemId, quantity));
    }

    @PatchMapping("/{orderId}/items/{itemId}/notes")
    public ResponseEntity<OrderDto> updateItemNotes(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestParam String notes) {
        return ResponseEntity.ok(orderService.updateItemNotes(orderId, itemId, notes));
    }

    // Order status transition endpoints
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderDto> confirmOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    @PostMapping("/{orderId}/start-preparing")
    public ResponseEntity<OrderDto> startPreparing(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.startPreparing(orderId));
    }

    @PostMapping("/{orderId}/mark-ready")
    public ResponseEntity<OrderDto> markAsReady(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.markAsReady(orderId));
    }

    @PostMapping("/{orderId}/start-delivery")
    public ResponseEntity<OrderDto> startDelivery(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.startDelivery(orderId));
    }

    @PostMapping("/{orderId}/complete-delivery")
    public ResponseEntity<OrderDto> completeDelivery(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.completeDelivery(orderId));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<OrderDto> refundOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.refundOrder(orderId));
    }
} 