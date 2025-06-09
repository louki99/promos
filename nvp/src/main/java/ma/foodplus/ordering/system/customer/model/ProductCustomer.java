package ma.foodplus.ordering.system.customer.model;

import ma.foodplus.ordering.system.product.model.Product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products_customer")
public class ProductCustomer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_product")
    private Product product;

    private String category;
    private BigDecimal coef;
    
    @Column(name = "prix_ttc", precision = 24, scale = 6)
    private BigDecimal prixTTC;
    
    @Column(name = "qte_mont")
    private Integer qteMont;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_customer")
    private Customer customer;

    @Column(precision = 24, scale = 6)
    private BigDecimal remise;
    
    @Column(name = "prix_ven_nouv", precision = 24, scale = 6)
    private BigDecimal prixVenNouv;
    
    @Column(name = "remise_nouv", precision = 24, scale = 6)
    private BigDecimal remiseNouv;
} 