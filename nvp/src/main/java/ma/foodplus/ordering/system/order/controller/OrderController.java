package ma.foodplus.ordering.system.order.controller;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.model.OrderStatus;
import ma.foodplus.ordering.system.order.model.OrderType;
import ma.foodplus.ordering.system.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import ma.foodplus.ordering.system.common.dto.ErrorResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Endpoints for managing orders, order items, and order status transitions in the FoodPlus ordering system.")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new order for the given customer.")
    @ApiResponse(responseCode = "200", description = "Order created successfully", content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid customer ID", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<OrderDto> createOrder(
            @Parameter(description = "ID of the customer placing the order", required = true) @RequestParam Long customerId,
            @Parameter(description = "Type of order (B2B or B2C)", required = true) @RequestParam OrderType orderType) {
        return ResponseEntity.ok(orderService.createOrder(customerId, orderType));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves an order by its unique ID.")
    @ApiResponse(responseCode = "200", description = "Order found", content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<OrderDto> getOrderById(@Parameter(description = "Order ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get orders by customer ID", description = "Retrieves all orders for a given customer.")
    @ApiResponse(responseCode = "200", description = "Orders found", content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @ApiResponse(responseCode = "404", description = "No orders found for customer", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@Parameter(description = "Customer ID", required = true) @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieves all orders with a specific status (e.g., PENDING, COMPLETED, CANCELLED).")
    @ApiResponse(responseCode = "200", description = "Orders found", content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @ApiResponse(responseCode = "404", description = "No orders found for status", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@Parameter(description = "Order status", required = true, example = "PENDING") @PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Deletes an order by its ID.")
    @ApiResponse(responseCode = "204", description = "Order deleted successfully")
    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "Order ID", required = true) @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // Order items management endpoints
    @PostMapping("/{orderId}/items")
    @Operation(summary = "Add item to order", description = "Adds an item to an existing order.")
    @ApiResponse(responseCode = "200", description = "Item added to order", content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<OrderDto> addItemToOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable Long orderId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Order item to add. Required fields: productId, unitPrice, quantity",
                required = true,
                content = @Content(schema = @Schema(implementation = OrderItemDto.class))
            ) @RequestBody OrderItemDto itemDto) {
        if (itemDto.getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required");
        }
        if (itemDto.getUnitPrice() == null) {
            throw new IllegalArgumentException("Unit price is required");
        }
        if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        return ResponseEntity.ok(orderService.addItemToOrder(orderId, itemDto));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Remove item from order", description = "Removes an item from an existing order.")
    @ApiResponse(responseCode = "200", description = "Item removed from order", content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @ApiResponse(responseCode = "404", description = "Order or item not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<OrderDto> removeItemFromOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable Long orderId,
            @Parameter(description = "Order item ID", required = true) @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.removeItemFromOrder(orderId, itemId));
    }

    @PatchMapping("/{orderId}/items/{itemId}/quantity")
    @Operation(summary = "Update item quantity", description = "Updates the quantity of a specific item in an order.")
    @ApiResponse(responseCode = "200", description = "Item quantity updated", content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @ApiResponse(responseCode = "404", description = "Order or item not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<OrderDto> updateItemQuantity(
            @Parameter(description = "Order ID", required = true) @PathVariable Long orderId,
            @Parameter(description = "Order item ID", required = true) @PathVariable Long itemId,
            @Parameter(description = "New quantity", required = true) @RequestParam Integer quantity) {
        return ResponseEntity.ok(orderService.updateItemQuantity(orderId, itemId, quantity));
    }

    @PatchMapping("/{orderId}/items/{itemId}/notes")
    @Operation(summary = "Update item notes", description = "Updates the notes for a specific item in an order.")
    @ApiResponse(responseCode = "200", description = "Item notes updated", content = @Content(schema = @Schema(implementation = OrderDto.class)))
    @ApiResponse(responseCode = "404", description = "Order or item not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<OrderDto> updateItemNotes(
            @Parameter(description = "Order ID", required = true) @PathVariable Long orderId,
            @Parameter(description = "Order item ID", required = true) @PathVariable Long itemId,
            @Parameter(description = "Notes for the item", required = true) @RequestParam String notes) {
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

    // B2B Specific Endpoints
    @PostMapping("/{orderId}/validate-bulk")
    @Operation(summary = "Validate bulk order", description = "Validates a bulk order for B2B customers.")
    public ResponseEntity<OrderDto> validateBulkOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.validateBulkOrder(orderId));
    }

    @PostMapping("/{orderId}/apply-contract-pricing")
    @Operation(summary = "Apply contract pricing", description = "Applies contract-based pricing for B2B customers.")
    public ResponseEntity<OrderDto> applyContractPricing(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.applyContractPricing(orderId));
    }

    @PostMapping("/{orderId}/validate-credit")
    @Operation(summary = "Validate credit limit", description = "Validates the order against customer's credit limit.")
    public ResponseEntity<OrderDto> validateCreditLimit(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.validateCreditLimit(orderId));
    }

    @PostMapping("/{orderId}/payment-terms")
    @Operation(summary = "Set payment terms", description = "Sets payment terms for B2B orders.")
    public ResponseEntity<OrderDto> setPaymentTerms(
            @PathVariable Long orderId,
            @RequestParam String terms) {
        return ResponseEntity.ok(orderService.setPaymentTerms(orderId, terms));
    }

    @PostMapping("/{orderId}/validate-minimum-value")
    @Operation(summary = "Validate minimum order value", description = "Validates if the order meets minimum value requirements.")
    public ResponseEntity<OrderDto> validateMinimumOrderValue(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.validateMinimumOrderValue(orderId));
    }

    @PostMapping("/{orderId}/apply-bulk-discount")
    @Operation(summary = "Apply bulk discount", description = "Applies bulk discount to eligible items.")
    public ResponseEntity<OrderDto> applyBulkDiscount(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.applyBulkDiscount(orderId));
    }

    @PostMapping("/{orderId}/delivery-schedule")
    @Operation(summary = "Set delivery schedule", description = "Sets preferred delivery date for B2B orders.")
    public ResponseEntity<OrderDto> setDeliverySchedule(
            @PathVariable Long orderId,
            @RequestParam LocalDateTime preferredDeliveryDate) {
        return ResponseEntity.ok(orderService.setDeliverySchedule(orderId, preferredDeliveryDate));
    }

    @PutMapping("/{orderId}/bulk-order")
    public ResponseEntity<OrderDto> setBulkOrderDetails(
            @PathVariable Long orderId,
            @RequestBody BulkOrderRequest request) {
        return ResponseEntity.ok(orderService.setBulkOrderDetails(
            orderId, 
            request.getReference(), 
            request.getFrequency(), 
            request.getScheduledDate()
        ));
    }

    @PutMapping("/{orderId}/contract")
    public ResponseEntity<OrderDto> setContractDetails(
            @PathVariable Long orderId,
            @RequestBody ContractRequest request) {
        return ResponseEntity.ok(orderService.setContractDetails(
            orderId,
            request.getContractId(),
            request.getStartDate(),
            request.getEndDate(),
            request.getTerms()
        ));
    }

    @PutMapping("/{orderId}/special-pricing")
    public ResponseEntity<OrderDto> applySpecialPricing(
            @PathVariable Long orderId,
            @RequestBody SpecialPricingRequest request) {
        return ResponseEntity.ok(orderService.applySpecialPricing(
            orderId,
            request.getPricingTerms()
        ));
    }

    // B2C Specific Endpoints
    @PostMapping("/{orderId}/apply-loyalty-points")
    @Operation(summary = "Apply loyalty points", description = "Applies loyalty points to the order.")
    public ResponseEntity<OrderDto> applyLoyaltyPoints(
            @PathVariable Long orderId,
            @RequestParam Integer points) {
        return ResponseEntity.ok(orderService.applyLoyaltyPoints(orderId, points));
    }

    @PostMapping("/{orderId}/apply-promo")
    @Operation(summary = "Apply promotional code", description = "Applies a promotional code to the order.")
    public ResponseEntity<OrderDto> applyPromoCode(
            @PathVariable Long orderId,
            @RequestParam String promoCode) {
        return ResponseEntity.ok(orderService.applyPromoCode(orderId, promoCode));
    }

    @PostMapping("/{orderId}/delivery-time-slot")
    @Operation(summary = "Select delivery time slot", description = "Selects a delivery time slot for B2C orders.")
    public ResponseEntity<OrderDto> selectDeliveryTimeSlot(
            @PathVariable Long orderId,
            @RequestParam LocalDateTime timeSlot) {
        return ResponseEntity.ok(orderService.selectDeliveryTimeSlot(orderId, timeSlot));
    }

    @PostMapping("/{orderId}/apply-preferences")
    @Operation(summary = "Apply customer preferences", description = "Applies customer preferences to the order.")
    public ResponseEntity<OrderDto> applyCustomerPreferences(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.applyCustomerPreferences(orderId));
    }

    @PostMapping("/{orderId}/calculate-delivery-fee")
    @Operation(summary = "Calculate delivery fee", description = "Calculates delivery fee for B2C orders.")
    public ResponseEntity<OrderDto> calculateDeliveryFee(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.calculateDeliveryFee(orderId));
    }

    @PostMapping("/{orderId}/validate-address")
    @Operation(summary = "Validate delivery address", description = "Validates the delivery address for B2C orders.")
    public ResponseEntity<OrderDto> validateDeliveryAddress(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.validateDeliveryAddress(orderId));
    }

    @PutMapping("/{orderId}/delivery-time-slot")
    public ResponseEntity<OrderDto> setDeliveryTimeSlot(
            @PathVariable Long orderId,
            @RequestBody DeliveryTimeSlotRequest request) {
        return ResponseEntity.ok(orderService.setDeliveryTimeSlot(
            orderId,
            request.getTimeSlot()
        ));
    }

    @PutMapping("/{orderId}/confirm-time-slot")
    public ResponseEntity<OrderDto> confirmDeliveryTimeSlot(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.confirmDeliveryTimeSlot(orderId));
    }

    @PutMapping("/{orderId}/contact-details")
    public ResponseEntity<OrderDto> setContactDetails(
            @PathVariable Long orderId,
            @RequestBody ContactDetailsRequest request) {
        return ResponseEntity.ok(orderService.setContactDetails(
            orderId,
            request.getPhone(),
            request.getEmail()
        ));
    }

    @PutMapping("/{orderId}/loyalty-points")
    public ResponseEntity<OrderDto> applyLoyaltyPoints(
            @PathVariable Long orderId,
            @RequestBody LoyaltyPointsRequest request) {
        return ResponseEntity.ok(orderService.applyLoyaltyPoints(
            orderId,
            request.getPoints()
        ));
    }

    @GetMapping("/{orderId}/loyalty-points")
    public ResponseEntity<OrderDto> calculateLoyaltyPointsEarned(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.calculateLoyaltyPointsEarned(orderId));
    }

    // Common Operations
    @PostMapping("/{orderId}/calculate-totals")
    @Operation(summary = "Calculate order totals", description = "Recalculates all totals for the order.")
    public ResponseEntity<OrderDto> calculateTotals(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.calculateTotals(orderId));
    }

    @PostMapping("/{orderId}/validate")
    @Operation(summary = "Validate order", description = "Validates the entire order.")
    public ResponseEntity<OrderDto> validateOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.validateOrder(orderId));
    }

    @GetMapping("/customer/{customerId}/history")
    @Operation(summary = "Get order history", description = "Retrieves order history for a customer.")
    public ResponseEntity<OrderDto> getOrderHistory(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrderHistory(customerId));
    }

    @GetMapping("/{orderId}/summary")
    @Operation(summary = "Get order summary", description = "Retrieves a summary of the order.")
    public ResponseEntity<OrderDto> getOrderSummary(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderSummary(orderId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search orders", description = "Searches orders based on a search term.")
    public ResponseEntity<List<OrderDto>> searchOrders(@RequestParam String searchTerm) {
        return ResponseEntity.ok(orderService.searchOrders(searchTerm));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get orders by date range", description = "Retrieves orders within a date range.")
    public ResponseEntity<List<OrderDto>> getOrdersByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(orderService.getOrdersByDateRange(startDate, endDate));
    }

    // Request DTOs
    @Data
    public static class BulkOrderRequest {
        private String reference;
        private String frequency;
        private LocalDateTime scheduledDate;
    }

    @Data
    public static class ContractRequest {
        private String contractId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String terms;
    }

    @Data
    public static class SpecialPricingRequest {
        private String pricingTerms;
    }

    @Data
    public static class DeliveryTimeSlotRequest {
        private String timeSlot;
    }

    @Data
    public static class ContactDetailsRequest {
        private String phone;
        private String email;
    }

    @Data
    public static class LoyaltyPointsRequest {
        private Integer points;
    }
} 