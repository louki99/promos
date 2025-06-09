package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "promotion_lines")
public class PromotionLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "paid_family_code")
    private String paidFamilyCode;

    @Column(name = "paid_product_id")
    private Long paidProductId;

    @Column(name = "free_family_code")
    private String freeFamilyCode;

    @Column(name = "free_product_id")
    private Long freeProductId;

    public PromotionLine() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Promotion getPromotion() { return promotion; }
    public void setPromotion(Promotion promotion) { this.promotion = promotion; }

    public String getPaidFamilyCode() { return paidFamilyCode; }
    public void setPaidFamilyCode(String paidFamilyCode) { this.paidFamilyCode = paidFamilyCode; }

    public Long getPaidProductId() { return paidProductId; }
    public void setPaidProductId(Long paidProductId) { this.paidProductId = paidProductId; }

    public String getFreeFamilyCode() { return freeFamilyCode; }
    public void setFreeFamilyCode(String freeFamilyCode) { this.freeFamilyCode = freeFamilyCode; }

    public Long getFreeProductId() { return freeProductId; }
    public void setFreeProductId(Long freeProductId) { this.freeProductId = freeProductId; }
} 