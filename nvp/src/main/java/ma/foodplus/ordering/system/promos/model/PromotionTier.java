package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table (name = "promotion_tiers")
public class PromotionTier {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private BigDecimal minimumThreshold; // الحد الأدنى لتفعيل هذه الشريحة

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private PromotionRule rule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reward_id", referencedColumnName = "id")
    private Reward reward; // كل شريحة لها مكافأتها الخاصة

    public Integer getId(){
        return id;
    }

    public BigDecimal getMinimumThreshold(){
        return minimumThreshold;
    }

    public PromotionRule getRule(){
        return rule;
    }

    public Reward getReward(){
        return reward;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public void setMinimumThreshold(BigDecimal minimumThreshold){
        this.minimumThreshold=minimumThreshold;
    }

    public void setRule(PromotionRule rule){
        this.rule=rule;
    }

    public void setReward(Reward reward){
        this.reward=reward;
    }
}
