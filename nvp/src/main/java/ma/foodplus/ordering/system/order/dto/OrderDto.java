package ma.foodplus.ordering.system.order.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDto {
    private Long id;
    private Long customerId;
    private List<OrderItemDto> items;
    private BigDecimal subtotal;
    private BigDecimal total;
    private String status;

    public OrderDto() {
    }

    public OrderDto(Long id, Long customerId, List<OrderItemDto> items, BigDecimal subtotal, BigDecimal total, String status) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.subtotal = subtotal;
        this.total = total;
        this.status = status;
    }

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

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 