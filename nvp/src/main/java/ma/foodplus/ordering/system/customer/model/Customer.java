package ma.foodplus.ordering.system.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.foodplus.ordering.system.common.model.CategoryTarif;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ct_num", nullable = false, unique = true)
    private String ctNum;

    @Column(name = "ice", nullable = false)
    private String ice;

    @Column(nullable = false)
    private String description;

    private String telephone;
    private String telecopie;
    private String email;
    private String address;
    
    @Column(name = "code_postal")
    private String codePostal;
    
    private String ville;
    private String country;
    
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_tarif_id")
    private CategoryTarif cateTarif;

    @Type(type = "jsonb")
    @Column(name = "num_payeur", columnDefinition = "jsonb")
    private String numPayeur;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
} 