package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String promoCode;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private ZonedDateTime startDate;

    @Column(nullable = false)
    private ZonedDateTime endDate;

    @Column(nullable = false)
    private int priority; // الأولوية

    private boolean isExclusive; // هل العرض حصري؟

    private String combinabilityGroup; // مجموعة التوافق

    private boolean isActive;

    @Column(name = "apply_first_matching_rule_only")
    private boolean applyFirstMatchingRuleOnly;

    // علاقة One-to-Many مع القواعد الترويجية
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionRule> rules;


    public Integer getId(){
        return id;
    }

    public String getPromoCode(){
        return promoCode;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public ZonedDateTime getStartDate(){
        return startDate;
    }

    public ZonedDateTime getEndDate(){
        return endDate;
    }

    public int getPriority(){
        return priority;
    }

    public boolean isExclusive(){
        return isExclusive;
    }

    public String getCombinabilityGroup(){
        return combinabilityGroup;
    }

    public boolean isActive(){
        return isActive;
    }

    public boolean isApplyFirstMatchingRuleOnly() {
        return applyFirstMatchingRuleOnly;
    }

    public List<PromotionRule> getRules(){
        return rules;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public void setPromoCode(String promoCode){
        this.promoCode=promoCode;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void setStartDate(ZonedDateTime startDate){
        this.startDate=startDate;
    }

    public void setEndDate(ZonedDateTime endDate){
        this.endDate=endDate;
    }

    public void setPriority(int priority){
        this.priority=priority;
    }

    public void setExclusive(boolean exclusive){
        isExclusive=exclusive;
    }

    public void setCombinabilityGroup(String combinabilityGroup){
        this.combinabilityGroup=combinabilityGroup;
    }

    public void setActive(boolean active){
        isActive=active;
    }

    public void setApplyFirstMatchingRuleOnly(boolean applyFirstMatchingRuleOnly) {
        this.applyFirstMatchingRuleOnly = applyFirstMatchingRuleOnly;
    }

    public void setRules(List<PromotionRule> rules){
        this.rules=rules;
    }

    /**
     * Checks if the promotion is active at the given time.
     *
     * @param now The time to check
     * @return true if the promotion is active, false otherwise
     */
    public boolean isActive(ZonedDateTime now) {
        return startDate.isBefore(now) && (endDate == null || endDate.isAfter(now));
    }
}