package ma.foodplus.ordering.system.pos.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ma.foodplus.ordering.system.pos.enums.PartnerType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String sku;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @DecimalMin("0.0")
    @Column(precision = 10, scale = 2)
    private BigDecimal costPrice;

    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @DecimalMin("0.0")
    @Column(precision = 10, scale = 2)
    private BigDecimal wholesalePrice;

    @ManyToOne
    @JoinColumn(name = "tax_id")
    private Tax tax;

    @NotBlank
    @Size(max = 20)
    private String unit; // e.g., "pcs", "kg", "liter"

    @Min(0)
    private Integer minStockLevel;

    @Min(0)
    private Integer maxStockLevel;

    private boolean trackInventory = true;
    private boolean active = true;

    @Size(max = 255)
    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<SaleItem> saleItems;

    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories;

    // Constructors
    public Product() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String name, String sku, String barcode, BigDecimal sellingPrice) {
        this();
        this.name = name;
        this.sku = sku;
        this.barcode = barcode;
        this.sellingPrice = sellingPrice;
    }

    // Business methods
    public BigDecimal getPriceForCustomerType(PartnerType partnerType) {
        return switch (partnerType) {
            case WHOLESALE -> wholesalePrice != null ? wholesalePrice : sellingPrice;
            case VIP -> sellingPrice.multiply(BigDecimal.valueOf(0.95)); // 5% discount
            default -> sellingPrice;
        };
    }

    public BigDecimal getMargin() {
        if (costPrice != null && costPrice.compareTo(BigDecimal.ZERO) > 0) {
            return sellingPrice.subtract(costPrice);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getMarginPercentage() {
        if (costPrice != null && costPrice.compareTo(BigDecimal.ZERO) > 0) {
            return getMargin().divide(costPrice, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public BigDecimal getWholesalePrice() { return wholesalePrice; }
    public void setWholesalePrice(BigDecimal wholesalePrice) { this.wholesalePrice = wholesalePrice; }

    public Tax getTax() { return tax; }
    public void setTax(Tax tax) { this.tax = tax; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Integer getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(Integer minStockLevel) { this.minStockLevel = minStockLevel; }

    public Integer getMaxStockLevel() { return maxStockLevel; }
    public void setMaxStockLevel(Integer maxStockLevel) { this.maxStockLevel = maxStockLevel; }

    public boolean isTrackInventory() { return trackInventory; }
    public void setTrackInventory(boolean trackInventory) { this.trackInventory = trackInventory; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<SaleItem> getSaleItems() { return saleItems; }
    public void setSaleItems(List<SaleItem> saleItems) { this.saleItems = saleItems; }

    public List<Inventory> getInventories() { return inventories; }
    public void setInventories(List<Inventory> inventories) { this.inventories = inventories; }
}
