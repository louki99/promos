package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table (name = "rewards")
public class Reward {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType;

    @Column(nullable = false)
    private BigDecimal value;

    private Integer targetEntityId; // ID للمنتج أو العائلة المستهدفة
    private String targetEntityType;

    public enum RewardType { PERCENT_DISCOUNT_ON_ITEM, FIXED_DISCOUNT_ON_CART, FREE_PRODUCT }

    public Integer getId(){
        return id;
    }

    public RewardType getRewardType(){
        return rewardType;
    }

    public BigDecimal getValue(){
        return value;
    }

    public Integer getTargetEntityId(){
        return targetEntityId;
    }

    public String getTargetEntityType(){
        return targetEntityType;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public void setRewardType(RewardType rewardType){
        this.rewardType=rewardType;
    }

    public void setValue(BigDecimal value){
        this.value=value;
    }

    public void setTargetEntityId(Integer targetEntityId){
        this.targetEntityId=targetEntityId;
    }

    public void setTargetEntityType(String targetEntityType){
        this.targetEntityType=targetEntityType;
    }
}