package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "promotion_rules")
public class PromotionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionLogic conditionLogic;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalculationMethod calculationMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BreakpointType breakpointType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BreakpointCalculationBasis breakpointCalculationBasis = BreakpointCalculationBasis.ORIGINAL_PRICE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Condition> conditions;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionTier> tiers;

    @Column(name = "repetition")
    private Integer repetition; // Répétition

    // Enums for clarity
    public enum ConditionLogic { ALL, ANY,AND }
    public enum CalculationMethod { BRACKET, CUMULATIVE }
    public enum BreakpointType { AMOUNT, QUANTITY, SKU_POINTS }
    public enum BreakpointCalculationBasis {
        ORIGINAL_PRICE,  // Use the original price before any discounts
        CURRENT_PRICE    // Use the current price after higher priority discounts
    }

    // Constructors
    public PromotionRule() {
    }

    public PromotionRule(String name, ConditionLogic conditionLogic, CalculationMethod calculationMethod, BreakpointType breakpointType) {
        this.name = name;
        this.conditionLogic = conditionLogic;
        this.calculationMethod = calculationMethod;
        this.breakpointType = breakpointType;
    }

    // Business Logic Methods
    public boolean evaluateConditions(List<Condition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return true;
        }

        if (conditionLogic == ConditionLogic.ALL) {
            return conditions.stream().allMatch(Condition::evaluate);
        } else {
            return conditions.stream().anyMatch(Condition::evaluate);
        }
    }

    public PromotionTier findApplicableTier(BigDecimal value) {
        if (tiers == null || tiers.isEmpty()) {
            return null;
        }

        return tiers.stream()
                .filter(tier -> tier.getMinimumThreshold().compareTo(value) <= 0)
                .max((t1, t2) -> t1.getMinimumThreshold().compareTo(t2.getMinimumThreshold()))
                .orElse(null);
    }

    public boolean hasValidTiers() {
        return tiers != null && !tiers.isEmpty();
    }

    public boolean hasValidConditions() {
        return conditions != null && !conditions.isEmpty();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConditionLogic getConditionLogic() {
        return conditionLogic;
    }

    public void setConditionLogic(ConditionLogic conditionLogic) {
        this.conditionLogic = conditionLogic;
    }

    public CalculationMethod getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(CalculationMethod calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public BreakpointType getBreakpointType() {
        return breakpointType;
    }

    public void setBreakpointType(BreakpointType breakpointType) {
        this.breakpointType = breakpointType;
    }

    public BreakpointCalculationBasis getBreakpointCalculationBasis() {
        return breakpointCalculationBasis;
    }

    public void setBreakpointCalculationBasis(BreakpointCalculationBasis breakpointCalculationBasis) {
        this.breakpointCalculationBasis = breakpointCalculationBasis;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<PromotionTier> getTiers() {
        return tiers;
    }

    public void setTiers(List<PromotionTier> tiers) {
        this.tiers = tiers;
    }

    public Integer getRepetition() {
        return repetition;
    }

    public void setRepetition(Integer repetition) {
        this.repetition = repetition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromotionRule that = (PromotionRule) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PromotionRule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", conditionLogic=" + conditionLogic +
                ", calculationMethod=" + calculationMethod +
                '}';
    }
}