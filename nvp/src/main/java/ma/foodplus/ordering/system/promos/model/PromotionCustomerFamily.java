package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "promotion_customer_families")
public class PromotionCustomerFamily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "customer_family_code", nullable = false)
    private String customerFamilyCode;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    public PromotionCustomerFamily() {}

    public PromotionCustomerFamily(Promotion promotion, String customerFamilyCode, ZonedDateTime startDate, ZonedDateTime endDate) {
        this.promotion = promotion;
        this.customerFamilyCode = customerFamilyCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Promotion getPromotion() { return promotion; }
    public void setPromotion(Promotion promotion) { this.promotion = promotion; }

    public String getCustomerFamilyCode() { return customerFamilyCode; }
    public void setCustomerFamilyCode(String customerFamilyCode) { this.customerFamilyCode = customerFamilyCode; }

    public ZonedDateTime getStartDate() { return startDate; }
    public void setStartDate(ZonedDateTime startDate) { this.startDate = startDate; }

    public ZonedDateTime getEndDate() { return endDate; }
    public void setEndDate(ZonedDateTime endDate) { this.endDate = endDate; }
} 