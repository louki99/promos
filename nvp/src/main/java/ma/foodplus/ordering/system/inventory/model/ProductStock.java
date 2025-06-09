package ma.foodplus.ordering.system.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.foodplus.ordering.system.product.model.Product;

import jakarta.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_stock")
public class ProductStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_product")
    private Product product;

    @Column(name = "de_no")
    private String deNo;

    @Column(name = "qte_mini")
    private Integer qteMini;

    @Column(name = "qte_max")
    private Integer qteMax;

    @Column(name = "qte_sto")
    private Integer qteSto;

    private boolean principal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_id")
    private Depot depot;
} 