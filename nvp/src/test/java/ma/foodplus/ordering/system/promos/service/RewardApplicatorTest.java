package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.promos.dto.PromotionRuleDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionTierDTO;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.model.PromotionTier;
import ma.foodplus.ordering.system.promos.model.Reward;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RewardApplicatorTest {

    @Autowired
    private RewardApplicator rewardApplicator;
    private Order order;
    private PromotionContext context;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        order = createOrder();
        context = new PromotionContext(order);
        promotion = createPromotion();
    }

    @Test
    void applyBracketCalculation_FixedAmountDiscount() {
        // Arrange
        PromotionRule rule = createPromotionRule(PromotionRule.CalculationMethod.BRACKET);
        PromotionTier tier = createPromotionTier(new BigDecimal("100.00"), new BigDecimal("10.00"));
        rule.setTiers(Collections.singletonList(tier));
        rule.setPromotion(promotion);

        // Act
        rewardApplicator.apply(context, rule);

        // Assert
        assertEquals(new BigDecimal("10.00"), context.getTotalDiscountApplied());
    }

    @Test
    void applyBracketCalculation_PercentageDiscount() {
        // Arrange
        PromotionRule rule = createPromotionRule(PromotionRule.CalculationMethod.BRACKET);
        PromotionTier tier = createPromotionTier(new BigDecimal("100.00"), new BigDecimal("0.10"));
        tier.getReward().setType(Reward.RewardType.PERCENTAGE);
        rule.setTiers(Collections.singletonList(tier));
        rule.setPromotion(promotion);

        // Act
        rewardApplicator.apply(context, rule);

        // Assert
        assertEquals(new BigDecimal("20.00"), context.getTotalDiscountApplied());
    }

    @Test
    void applyCumulativeCalculation_MultipleTiers() {
        // Arrange
        PromotionRule rule = createPromotionRule(PromotionRule.CalculationMethod.CUMULATIVE);
        PromotionTier tier1 = createPromotionTier(new BigDecimal("50.00"), new BigDecimal("5.00"));
        PromotionTier tier2 = createPromotionTier(new BigDecimal("100.00"), new BigDecimal("10.00"));
        rule.setTiers(Arrays.asList(tier1, tier2));
        rule.setPromotion(promotion);

        // Act
        rewardApplicator.apply(context, rule);

        // Assert
        assertEquals(new BigDecimal("15.00"), context.getTotalDiscountApplied());
    }

    @Test
    void applyFreeProductReward() {
        // Arrange
        PromotionRule rule = createPromotionRule(PromotionRule.CalculationMethod.BRACKET);
        PromotionTier tier = createPromotionTier(new BigDecimal("100.00"), BigDecimal.ONE);
        tier.getReward().setType(Reward.RewardType.FREE_PRODUCT);
        rule.setTiers(Collections.singletonList(tier));
        rule.setPromotion(promotion);

        // Act
        rewardApplicator.apply(context, rule);

        // Assert
        assertTrue(context.getFreeItemsLog().containsKey(1L));
        assertEquals(1, context.getFreeItemsLog().get(1L).getQuantity());
    }

    @Test
    void applyWithRepetitionLimit() {
        // Arrange
        PromotionRule rule = createPromotionRule(PromotionRule.CalculationMethod.BRACKET);
        PromotionTier tier = createPromotionTier(new BigDecimal("50.00"), new BigDecimal("5.00"));
        rule.setRepetition(2);
        rule.setTiers(Collections.singletonList(tier));
        rule.setPromotion(promotion);

        // Act
        rewardApplicator.apply(context, rule);

        // Assert
        assertEquals(new BigDecimal("10.00"), context.getTotalDiscountApplied());
    }

    @Test
    void applyWithSkuPoints() {
        // Arrange
        PromotionRule rule = createPromotionRule(PromotionRule.CalculationMethod.BRACKET);
        PromotionTier tier = createPromotionTier(new BigDecimal("5"), new BigDecimal("5.00"));
        rule.setBreakpointType(PromotionRule.BreakpointType.SKU_POINTS);
        rule.setTiers(Collections.singletonList(tier));
        rule.setPromotion(promotion);

        // Act
        rewardApplicator.apply(context, rule);

        // Assert
        assertEquals(new BigDecimal("5.00"), context.getTotalDiscountApplied());
    }

    @Test
    void applyWithExcludedProducts() {
        // Arrange
        PromotionRule rule = createPromotionRule(PromotionRule.CalculationMethod.BRACKET);
        PromotionTier tier = createPromotionTier(new BigDecimal("100.00"), new BigDecimal("10.00"));
        rule.setTiers(Collections.singletonList(tier));
        rule.setPromotion(promotion);
        promotion.setExcludedProductIds(Collections.singletonList(2L));

        // Act
        rewardApplicator.apply(context, rule);

        // Assert
        assertEquals(new BigDecimal("10.00"), context.getTotalDiscountApplied());
    }

    // Helper methods
    private Order createOrder() {
        Order order = new Order();
        order.setItems(Arrays.asList(
            createOrderItem(1L, "Product 1", 2, new BigDecimal("10.00")),
            createOrderItem(2L, "Product 2", 3, new BigDecimal("15.00"))
        ));
        return order;
    }

    private ma.foodplus.ordering.system.order.model.OrderItem createOrderItem(
            Long productId, String name, int quantity, BigDecimal unitPrice) {
        ma.foodplus.ordering.system.order.model.OrderItem item = 
            new ma.foodplus.ordering.system.order.model.OrderItem();
        item.setProductId(productId);
        item.setProductName(name);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        return item;
    }

    private PromotionRule createPromotionRule(PromotionRule.CalculationMethod method) {
        PromotionRule rule = new PromotionRule();
        rule.setCalculationMethod(method);
        rule.setBreakpointType(PromotionRule.BreakpointType.AMOUNT);
        return rule;
    }

    private PromotionTier createPromotionTier(BigDecimal threshold, BigDecimal discountAmount) {
        PromotionTier tier = new PromotionTier();
        tier.setMinimumThreshold(threshold);
        
        Reward reward = new Reward();
        reward.setType(Reward.RewardType.FIXED_AMOUNT);
        reward.setDiscountAmount(discountAmount);
        tier.setReward(reward);
        
        return tier;
    }

    private Promotion createPromotion() {
        Promotion promotion = new Promotion();
        promotion.setPromoCode("TEST_PROMO");
        promotion.setName("Test Promotion");
        promotion.setActive(true);
        return promotion;
    }
}