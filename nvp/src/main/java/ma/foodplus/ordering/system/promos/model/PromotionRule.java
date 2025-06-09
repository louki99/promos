package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.concurrent.locks.Condition;

@Entity
@Table (name = "promotion_rules")
public class PromotionRule {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionLogic conditionLogic; // ALL or ANY

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalculationMethod calculationMethod; // BRACKET or CUMULATIVE

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BreakpointType breakpointType; // AMOUNT, QUANTITY, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Condition> conditions;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionTier> tiers;

    // Enums for clarity
    public enum ConditionLogic { ALL, ANY }
    public enum CalculationMethod { BRACKET, CUMULATIVE }
    public enum BreakpointType { AMOUNT, QUANTITY, SKU_POINTS }


    public Integer getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ConditionLogic getConditionLogic(){
        return conditionLogic;
    }

    public CalculationMethod getCalculationMethod(){
        return calculationMethod;
    }

    public BreakpointType getBreakpointType(){
        return breakpointType;
    }

    public Promotion getPromotion(){
        return promotion;
    }

    public List<Condition> getConditions(){
        return conditions;
    }

    public List<PromotionTier> getTiers(){
        return tiers;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setConditionLogic(ConditionLogic conditionLogic){
        this.conditionLogic=conditionLogic;
    }

    public void setCalculationMethod(CalculationMethod calculationMethod){
        this.calculationMethod=calculationMethod;
    }

    public void setBreakpointType(BreakpointType breakpointType){
        this.breakpointType=breakpointType;
    }

    public void setPromotion(Promotion promotion){
        this.promotion=promotion;
    }

    public void setConditions(List<Condition> conditions){
        this.conditions=conditions;
    }

    public void setTiers(List<PromotionTier> tiers){
        this.tiers=tiers;
    }
}