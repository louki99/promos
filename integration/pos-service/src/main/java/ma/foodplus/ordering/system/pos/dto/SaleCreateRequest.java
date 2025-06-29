package ma.foodplus.ordering.system.pos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ma.foodplus.ordering.system.pos.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public class SaleCreateRequest {
    
    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Terminal ID is required")
    private Long terminalId;

    @NotNull(message = "Partner ID is required")
    private Long partnerId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @DecimalMin(value = "0.0", message = "Paid amount cannot be negative")
    private BigDecimal paidAmount;

    @DecimalMin(value = "0.0", message = "Discount amount cannot be negative")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @Valid
    @NotEmpty(message = "Sale items are required")
    private List<SaleItemRequest> saleItems;

    // Getters and Setters
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public Long getTerminalId() { return terminalId; }
    public void setTerminalId(Long terminalId) { this.terminalId = terminalId; }

    public Long getPartnerId() { return partnerId; }
    public void setPartnerId(Long partnerId) { this.partnerId = partnerId; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<SaleItemRequest> getSaleItems() { return saleItems; }
    public void setSaleItems(List<SaleItemRequest> saleItems) { this.saleItems = saleItems; }

    // Inner class for sale items
    public static class SaleItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @DecimalMin(value = "0.0", message = "Unit price cannot be negative")
        private BigDecimal unitPrice;

        @DecimalMin(value = "0.0", message = "Discount cannot be negative")
        private BigDecimal discount = BigDecimal.ZERO;

        // Getters and Setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

        public BigDecimal getDiscount() { return discount; }
        public void setDiscount(BigDecimal discount) { this.discount = discount; }
    }
} 