package ma.foodplus.ordering.system.product.service.dataaccess.product.entity;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Entity
public class ProductEntity{

    @Id
    private UUID id;
    private String name;
    private String description;
    private Boolean isActive;
    private Boolean featured;
    private BigDecimal price;
    private UUID unit;
    private String slug;
    private String metaTitle;
    private String metaDescription;
    private UUID category;
    private int quantity;
}
