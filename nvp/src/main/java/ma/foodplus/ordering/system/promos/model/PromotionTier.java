package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "promotion_tiers")
public class PromotionTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    private BigDecimal minimumThreshold; // الحد الأدنى لتفعيل هذه الشريحة

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private PromotionRule rule;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reward_id", referencedColumnName = "id")
    private Reward reward; // كل شريحة لها مكافأتها الخاصة

    // Constructors
    public PromotionTier() {
    }

    public PromotionTier(BigDecimal minimumThreshold, Reward reward) {
        this.minimumThreshold = minimumThreshold;
        this.reward = reward;
        validateThreshold();
    }

    // Business Logic Methods
    private void validateThreshold() {
        if (minimumThreshold == null || minimumThreshold.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum threshold must be non-negative");
        }
    }

    public boolean isApplicable(BigDecimal value) {
        if (value == null) {
            return false;
        }
        return value.compareTo(minimumThreshold) >= 0;
    }

    public BigDecimal calculateReward(BigDecimal value) {
        if (!isApplicable(value)) {
            return BigDecimal.ZERO;
        }
        return reward.calculateDiscount(value);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMinimumThreshold() {
        return minimumThreshold;
    }

    public void setMinimumThreshold(BigDecimal minimumThreshold) {
        this.minimumThreshold = minimumThreshold;
        validateThreshold();
    }

    public PromotionRule getRule() {
        return rule;
    }

    public void setRule(PromotionRule rule) {
        this.rule = rule;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromotionTier that = (PromotionTier) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PromotionTier{" +
                "id=" + id +
                ", minimumThreshold=" + minimumThreshold +
                ", reward=" + reward +
                '}';
    }
}
