package ma.foodplus.ordering.system.order.dto;

import java.math.BigDecimal;

public class OrderItemDto {
    private Long productId;
    private Long productFamilyId;
    private String productName;
    private String sku;
    private BigDecimal skuPoints;
    private BigDecimal unitPrice;
    private Integer quantity;
    private String notes;

    public OrderItemDto() {
    }

    public OrderItemDto(Long productId, BigDecimal unitPrice, Integer quantity) {
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductFamilyId() {
        return productFamilyId;
    }

    public void setProductFamilyId(Long productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getSkuPoints() {
        return skuPoints;
    }

    public void setSkuPoints(BigDecimal skuPoints) {
        this.skuPoints = skuPoints;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
} 