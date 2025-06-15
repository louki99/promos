package ma.foodplus.ordering.system.order.model;

import jakarta.persistence.*;
import ma.foodplus.ordering.system.order.exception.InvalidOrderStatusTransitionException;
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
    private Boolean specialPricingAgreement = false;

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

    // B2B Credit Management
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "available_credit")
    private BigDecimal availableCredit;

    @Column(name = "payment_due_days")
    private Integer paymentDueDays;

    @Column(name = "credit_terms")
    private String creditTerms;

    // Bulk Order Management
    @Column(name = "is_bulk_order")
    private Boolean isBulkOrder = false;

    @Column(name = "bulk_order_reference")
    private String bulkOrderReference;

    @Column(name = "bulk_order_notes")
    private String bulkOrderNotes;

    @Column(name = "scheduled_delivery_date")
    private ZonedDateTime scheduledDeliveryDate;

    @Column(name = "delivery_schedule_frequency")
    private String deliveryScheduleFrequency; // DAILY, WEEKLY, MONTHLY

    // Contract Management
    @Column(name = "contract_id")
    private String contractId;

    @Column(name = "contract_start_date")
    private ZonedDateTime contractStartDate;

    @Column(name = "contract_end_date")
    private ZonedDateTime contractEndDate;

    @Column(name = "contract_terms")
    private String contractTerms;

    @Column(name = "special_pricing_terms")
    private String specialPricingTerms;

    // Delivery Time Slot Management
    @Column(name = "preferred_delivery_time_slot")
    private String preferredDeliveryTimeSlot;

    @Column(name = "delivery_time_slot_confirmed")
    private Boolean deliveryTimeSlotConfirmed = false;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    // Loyalty Program
    @Column(name = "loyalty_points_earned")
    private Integer loyaltyPointsEarned = 0;

    @Column(name = "loyalty_points_redeemed")
    private Integer loyaltyPointsRedeemed = 0;

    @Column(name = "loyalty_discount_applied")
    private BigDecimal loyaltyDiscountApplied = BigDecimal.ZERO;

    @Column(name = "loyalty_member_id")
    private String loyaltyMemberId;

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
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Cannot add items to an order that is not in DRAFT status");
        }
        items.add(item);
        item.setOrder(this);
        recalculateTotals();
    }

    public void removeItem(OrderItem item) {
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Cannot remove items from an order that is not in DRAFT status");
        }
        items.remove(item);
        item.setOrder(null);
        recalculateTotals();
    }

    public void updateItemQuantity(OrderItem item, Integer newQuantity) {
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Cannot update item quantity in an order that is not in DRAFT status");
        }
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
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(loyaltyDiscountApplied);
        
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
        if (status == OrderStatus.DELIVERED || status == OrderStatus.REFUNDED) {
            throw new InvalidOrderStatusTransitionException(status, OrderStatus.CANCELLED);
        }
        this.cancellationReason = reason;
        setStatus(OrderStatus.CANCELLED);
    }

    public void refund(String reason) {
        if (status != OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusTransitionException(status, OrderStatus.REFUNDED);
        }
        this.refundReason = reason;
        setStatus(OrderStatus.REFUNDED);
    }

    public void markAsDelivered() {
        if (shippingAddress == null) {
            throw new IllegalStateException("Cannot mark order as delivered without shipping address");
        }
        setStatus(OrderStatus.DELIVERED);
    }

    // Status Management
    public void setStatus(OrderStatus newStatus) {
        if (this.status != null && !OrderStatusTransition.isValidTransition(this.status, newStatus)) {
            throw new InvalidOrderStatusTransitionException(this.status, newStatus);
        }
        this.status = newStatus;
        this.updatedAt = ZonedDateTime.now();
        
        // Update relevant timestamps based on status
        switch (newStatus) {
            case CANCELLED -> this.cancelledAt = ZonedDateTime.now();
            case REFUNDED -> this.refundedAt = ZonedDateTime.now();
            case DELIVERED -> this.deliveredAt = ZonedDateTime.now();
        }
    }

    // Business Rules Validation
    public void validateOrderState() {
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }

        if (status == OrderStatus.DELIVERED && shippingAddress == null) {
            throw new IllegalStateException("Delivery address is required for delivered orders");
        }

        if (status == OrderStatus.CANCELLED && cancellationReason == null) {
            throw new IllegalStateException("Cancellation reason is required for cancelled orders");
        }

        if (status == OrderStatus.REFUNDED && refundReason == null) {
            throw new IllegalStateException("Refund reason is required for refunded orders");
        }

        if (orderType == OrderType.B2B) {
            if (!meetsMinimumOrderValue()) {
                throw new IllegalStateException("B2B order does not meet minimum order value requirement");
            }
            validateCreditLimit();
            validateBulkOrder();
            validateContract();
        } else {
            validateDeliveryTimeSlot();
        }
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

    public boolean isWithinCreditLimit() {
        if (orderType != OrderType.B2B) {
            return true;
        }
        return availableCredit != null && 
               total.compareTo(availableCredit) <= 0;
    }

    public void validateCreditLimit() {
        if (orderType == OrderType.B2B && !isWithinCreditLimit()) {
            throw new IllegalStateException(
                String.format("Order total %.2f exceeds available credit limit %.2f", 
                    total, availableCredit));
        }
    }

    public void validateBulkOrder() {
        if (isBulkOrder) {
            if (scheduledDeliveryDate == null) {
                throw new IllegalStateException("Scheduled delivery date is required for bulk orders");
            }
            if (deliveryScheduleFrequency == null) {
                throw new IllegalStateException("Delivery schedule frequency is required for bulk orders");
            }
            if (bulkOrderReference == null) {
                throw new IllegalStateException("Bulk order reference is required for bulk orders");
            }
        }
    }

    public void validateContract() {
        if (orderType == OrderType.B2B && specialPricingAgreement) {
            if (contractId == null) {
                throw new IllegalStateException("Contract ID is required for special pricing agreements");
            }
            if (contractStartDate == null || contractEndDate == null) {
                throw new IllegalStateException("Contract dates are required for special pricing agreements");
            }
            if (contractEndDate.isBefore(contractStartDate)) {
                throw new IllegalStateException("Contract end date must be after start date");
            }
            if (ZonedDateTime.now().isAfter(contractEndDate)) {
                throw new IllegalStateException("Contract has expired");
            }
        }
    }

    public void validateDeliveryTimeSlot() {
        if (orderType == OrderType.B2C) {
            if (preferredDeliveryTimeSlot == null) {
                throw new IllegalStateException("Delivery time slot is required for B2C orders");
            }
            if (contactPhone == null) {
                throw new IllegalStateException("Contact phone is required for B2C orders");
            }
        }
    }

    public void applyLoyaltyPoints(Integer pointsToRedeem) {
        if (orderType != OrderType.B2C) {
            throw new IllegalStateException("Loyalty points can only be applied to B2C orders");
        }
        if (pointsToRedeem < 0) {
            throw new IllegalArgumentException("Points to redeem cannot be negative");
        }
        this.loyaltyPointsRedeemed = pointsToRedeem;
        // Calculate discount based on points (example: 100 points = $1)
        this.loyaltyDiscountApplied = BigDecimal.valueOf(pointsToRedeem)
            .divide(BigDecimal.valueOf(100))
            .setScale(2, BigDecimal.ROUND_HALF_UP);
        recalculateTotals();
    }

    public void calculateLoyaltyPointsEarned() {
        if (orderType != OrderType.B2C) {
            return;
        }
        // Example: 1 point per $1 spent
        this.loyaltyPointsEarned = total.intValue();
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

    // Getters and Setters for new fields
    public void setBulkOrder(Boolean bulkOrder) {
        this.isBulkOrder = bulkOrder;
    }

    public void setBulkOrderReference(String bulkOrderReference) {
        this.bulkOrderReference = bulkOrderReference;
    }

    public void setDeliveryScheduleFrequency(String deliveryScheduleFrequency) {
        this.deliveryScheduleFrequency = deliveryScheduleFrequency;
    }

    public void setScheduledDeliveryDate(ZonedDateTime scheduledDeliveryDate) {
        this.scheduledDeliveryDate = scheduledDeliveryDate;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public void setContractStartDate(ZonedDateTime contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public void setContractEndDate(ZonedDateTime contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public void setContractTerms(String contractTerms) {
        this.contractTerms = contractTerms;
    }

    public void setSpecialPricingAgreement(Boolean specialPricingAgreement) {
        this.specialPricingAgreement = specialPricingAgreement;
    }

    public void setSpecialPricingTerms(String specialPricingTerms) {
        this.specialPricingTerms = specialPricingTerms;
    }

    public void setPreferredDeliveryTimeSlot(String preferredDeliveryTimeSlot) {
        this.preferredDeliveryTimeSlot = preferredDeliveryTimeSlot;
    }

    public String getPreferredDeliveryTimeSlot() {
        return preferredDeliveryTimeSlot;
    }

    public void setDeliveryTimeSlotConfirmed(Boolean deliveryTimeSlotConfirmed) {
        this.deliveryTimeSlotConfirmed = deliveryTimeSlotConfirmed;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
