package ma.foodplus.ordering.system.order.model;

import jakarta.persistence.*;
import ma.foodplus.ordering.system.promos.dto.OrdertemDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "po_number")
    private String poNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // Financial Information
    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "total", nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "total_discount", nullable = false)
    private BigDecimal totalDiscount = BigDecimal.ZERO;

    @Column(name = "total_tax", nullable = false)
    private BigDecimal totalTax = BigDecimal.ZERO;

    @Column(name = "shipping_cost", nullable = false)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    // Order Status
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    // Payment Details
    @Column(name = "payment_due_date")
    private ZonedDateTime paymentDueDate;

    @Column(name = "payment_terms")
    private String paymentTerms;

    @Column(name = "payment_notes")
    private String paymentNotes;

    @Column(name = "payment_reference")
    private String paymentReference;

    // Shipping Information
    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_city")
    private String shippingCity;

    @Column(name = "shipping_country")
    private String shippingCountry;

    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;

    @Column(name = "shipping_notes")
    private String shippingNotes;

    @Column(name = "preferred_delivery_date")
    private ZonedDateTime preferredDeliveryDate;

    @Column(name = "delivery_instructions")
    private String deliveryInstructions;

    // B2B Specific
    @Column(name = "is_wholesale", nullable = false)
    private boolean wholesale = false;

    @Column(name = "minimum_order_value")
    private BigDecimal minimumOrderValue;

    @Column(name = "bulk_discount_percentage")
    private BigDecimal bulkDiscountPercentage;

    @Column(name = "special_pricing_agreement")
    private boolean specialPricingAgreement = false;

    @Column(name = "contract_number")
    private String contractNumber;

    // Order Details
    @Column(name = "notes")
    private String notes;

    @Column(name = "internal_notes")
    private String internalNotes;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "refund_reason")
    private String refundReason;

    // Audit
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "cancelled_at")
    private ZonedDateTime cancelledAt;

    @Column(name = "refunded_at")
    private ZonedDateTime refundedAt;

    @Column(name = "delivered_at")
    private ZonedDateTime deliveredAt;

    // Constructors
    public Order() {
    }

    public Order(Long customerId) {
        this.customerId = customerId;
        this.status = OrderStatus.DRAFT;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    public Order(Long customerId, List<OrdertemDto> cartItems) {
        this.customerId = customerId;
        this.status = OrderStatus.DRAFT;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
        for (OrdertemDto item : cartItems) {
            OrderItem orderItem = new OrderItem(item);
            addItem(orderItem);
        }
        recalculateTotals();
    }

    // Business Logic Methods
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotals();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        recalculateTotals();
    }

    public void updateItemQuantity(OrderItem item, Integer newQuantity) {
        if (items.contains(item)) {
            item.setQuantity(newQuantity);
            recalculateTotals();
        }
    }

    public void recalculateTotals() {
        this.subtotal = items.stream()
                .map(OrderItem::getOriginalTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.totalDiscount = items.stream()
                .map(OrderItem::getDiscountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.totalTax = items.stream()
                .map(OrderItem::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.total = this.subtotal
                .subtract(this.totalDiscount)
                .add(this.totalTax)
                .add(this.shippingCost);
    }

    // B2B Specific Methods
    public boolean isEligibleForBulkDiscount() {
        return wholesale && items.stream()
                .anyMatch(item -> item.getQuantity() >= item.getBulkQuantityThreshold());
    }

    public void applyBulkDiscount() {
        if (isEligibleForBulkDiscount() && bulkDiscountPercentage != null) {
            items.forEach(item -> {
                if (item.getQuantity() >= item.getBulkQuantityThreshold()) {
                    BigDecimal discount = item.getOriginalTotalPrice()
                            .multiply(bulkDiscountPercentage)
                            .divide(BigDecimal.valueOf(100));
                    item.applyDiscount(discount);
                }
            });
            recalculateTotals();
        }
    }

    public boolean meetsMinimumOrderValue() {
        return minimumOrderValue == null || 
               total.compareTo(minimumOrderValue) >= 0;
    }

    // Promotion-related methods
    public List<OrderItem> findItemsByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    public List<OrderItem> findItemsByFamilyId(Long familyId) {
        return items.stream()
                .filter(item -> item.getProductFamilyId().equals(familyId))
                .collect(Collectors.toList());
    }

    public void applyPromotion(String promotionCode, String description, List<OrderItem> affectedItems, BigDecimal discountAmount) {
        affectedItems.forEach(item -> {
            item.setAppliedPromotionCode(promotionCode);
            item.applyDiscount(discountAmount.divide(BigDecimal.valueOf(affectedItems.size()), 2, BigDecimal.ROUND_HALF_UP));
        });
        recalculateTotals();
    }

    // Lifecycle Methods
    public void cancel(String reason) {
        this.status = OrderStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancelledAt = ZonedDateTime.now();
    }

    public void refund(String reason) {
        this.status = OrderStatus.REFUNDED;
        this.refundReason = reason;
        this.refundedAt = ZonedDateTime.now();
    }

    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
        this.deliveredAt = ZonedDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        recalculateTotals();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getFinalTotalPrice() {
        return total;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
        recalculateTotals();
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public void setPreferredDeliveryDate(ZonedDateTime preferredDeliveryDate) {
        this.preferredDeliveryDate = preferredDeliveryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerId=" + customerId +
                ", status=" + status +
                ", total=" + total +
                '}';
    }
}
