package ma.foodplus.ordering.system.pos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @NotNull
    @Min(0)
    private Integer quantity;

    @Min(0)
    private Integer reservedQuantity = 0;

    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;

    // Constructors
    public Inventory() {
        this.lastUpdated = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public Inventory(Product product, Store store, Integer quantity) {
        this();
        this.product = product;
        this.store = store;
        this.quantity = quantity;
    }

    // Business methods
    public Integer getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public boolean isLowStock() {
        return product.getMinStockLevel() != null &&
                quantity <= product.getMinStockLevel();
    }

    public boolean isOverStock() {
        return product.getMaxStockLevel() != null &&
                quantity >= product.getMaxStockLevel();
    }

    public void adjustQuantity(Integer adjustment) {
        this.quantity += adjustment;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}