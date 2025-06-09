package ma.foodplus.ordering.system.order.dto;

import java.math.BigDecimal;

public class OrderItemDto {
    private Long productId;
    private BigDecimal unitPrice;
    private int quantity;

    public OrderItemDto() {
    }

    public OrderItemDto(Long productId, BigDecimal unitPrice, int quantity) {
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
} 