package ma.foodplus.ordering.system.promos.service;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.component.CartItemContext;
import ma.foodplus.ordering.system.promos.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardApplicator {

    @Qualifier("databaseProductSkuResolver")
    private final ProductSkuResolver productSkuResolver;

    public void apply(PromotionContext context, PromotionRule rule) {
        List<CartItemContext> eligibleItems = findEligibleItemsForRule(context, rule);
        if (eligibleItems.isEmpty()) return;

        BigDecimal breakpointValue = calculateBreakpointValue(eligibleItems, rule.getBreakpointType());

        List<PromotionTier> tiers = rule.getTiers().stream()
                .sorted(Comparator.comparing(PromotionTier::getMinimumThreshold))
                .collect(Collectors.toList());

        if (rule.getCalculationMethod() == PromotionRule.CalculationMethod.BRACKET) {
            applyBracket(context, rule, eligibleItems, breakpointValue, tiers);
        } else {
            applyCumulative(context, rule, eligibleItems, breakpointValue, tiers);
        }
    }

    private BigDecimal calculateBreakpointValue(List<CartItemContext> eligibleItems, PromotionRule.BreakpointType breakpointType) {
        switch (breakpointType) {
            case AMOUNT:
                return eligibleItems.stream()
                        .map(item -> item.getOriginalItem().getPrice().multiply(new BigDecimal(item.getOriginalItem().getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            case QUANTITY:
                int totalQuantity = eligibleItems.stream()
                        .mapToInt(item -> item.getOriginalItem().getQuantity())
                        .sum();
                return new BigDecimal(totalQuantity);
            case SKU_POINTS:
                return eligibleItems.stream()
                        .map(item -> {
                            BigDecimal pointsPerUnit = productSkuResolver.getSkuPointsForProduct(item.getOriginalItem().getProductId());
                            return pointsPerUnit.multiply(new BigDecimal(item.getOriginalItem().getQuantity()));
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            default:
                throw new IllegalArgumentException("Unsupported breakpoint type: " + breakpointType);
        }
    }

    private void applyBracket(PromotionContext context, PromotionRule rule, List<CartItemContext> eligibleItems,
                            BigDecimal breakpointValue, List<PromotionTier> sortedTiers) {
        PromotionTier bestTier = sortedTiers.stream()
                .sorted(Comparator.comparing(PromotionTier::getMinimumThreshold).reversed())
                .filter(tier -> breakpointValue.compareTo(tier.getMinimumThreshold()) >= 0)
                .findFirst()
                .orElse(null);

        if (bestTier == null) return;

        applyRewardForTier(context, bestTier, eligibleItems, rule.getPromotion());
        updateContextAfterApplication(context, rule.getPromotion());
    }

    private void updateContextAfterApplication(PromotionContext context, Promotion promotion) {
        context.markPromotionAsApplied(promotion);
    }

    private void applyRewardForTier(PromotionContext context, PromotionTier tier, List<CartItemContext> itemsToReward, Promotion promotion) {
        Reward reward = tier.getReward();
        BigDecimal rewardValue = reward.getValue();
        BigDecimal totalDiscount = BigDecimal.ZERO;

        switch (reward.getRewardType()) {
            case PERCENT_DISCOUNT_ON_ITEM:
                BigDecimal basePrice = itemsToReward.stream()
                        .map(CartItemContext::getRemainingPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                totalDiscount = basePrice.multiply(rewardValue.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                distributeDiscountProportionally(itemsToReward, totalDiscount);
                break;
            case FIXED_DISCOUNT_ON_CART:
                BigDecimal availablePrice = itemsToReward.stream()
                        .map(CartItemContext::getRemainingPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                totalDiscount = rewardValue.min(availablePrice);
                distributeDiscountProportionally(itemsToReward, totalDiscount);
                break;
            case FREE_PRODUCT:
                context.logFreeItem(Long.valueOf(reward.getTargetEntityId()), rewardValue.intValue(), promotion.getPromoCode());
                break;
        }

        if (totalDiscount.compareTo(BigDecimal.ZERO) > 0) {
            context.logAppliedDiscount(promotion.getPromoCode(), "Bracket Discount: " + reward.getRewardType(), totalDiscount);
        }
    }

    private void applyCumulative(PromotionContext context, PromotionRule rule, List<CartItemContext> eligibleItems,
                               BigDecimal totalBreakpointValue, List<PromotionTier> sortedTiers) {
        BigDecimal lastThreshold = BigDecimal.ZERO;

        for (PromotionTier tier : sortedTiers) {
            if (totalBreakpointValue.compareTo(lastThreshold) <= 0) break;

            BigDecimal currentThreshold = tier.getMinimumThreshold();
            BigDecimal bracketSize = currentThreshold.subtract(lastThreshold);
            BigDecimal valueInThisBracket = totalBreakpointValue.subtract(lastThreshold).min(bracketSize);

            applyRewardForSlice(context, tier, eligibleItems, valueInThisBracket, rule.getBreakpointType(), rule.getPromotion());

            lastThreshold = currentThreshold;
        }

        if (totalBreakpointValue.compareTo(lastThreshold) > 0) {
            BigDecimal finalRemainingValue = totalBreakpointValue.subtract(lastThreshold);
            PromotionTier highestTier = sortedTiers.get(sortedTiers.size() - 1);
            applyRewardForSlice(context, highestTier, eligibleItems, finalRemainingValue, rule.getBreakpointType(), rule.getPromotion());
        }

        updateContextAfterApplication(context, rule.getPromotion());
    }

    private void applyRewardForSlice(PromotionContext context, PromotionTier tier, List<CartItemContext> allEligibleItems,
                                   BigDecimal valueSlice, PromotionRule.BreakpointType breakpointType, Promotion promotion) {
        Reward reward = tier.getReward();
        BigDecimal totalDiscountForSlice = BigDecimal.ZERO;

        if (reward.getRewardType() == Reward.RewardType.PERCENT_DISCOUNT_ON_ITEM) {
            if (breakpointType == PromotionRule.BreakpointType.AMOUNT) {
                totalDiscountForSlice = valueSlice.multiply(reward.getValue().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                distributeDiscountProportionally(allEligibleItems, totalDiscountForSlice);
            } else if (breakpointType == PromotionRule.BreakpointType.QUANTITY) {
                BigDecimal priceOfSlice = calculatePriceForQuantitySlice(allEligibleItems, valueSlice.intValue());
                totalDiscountForSlice = priceOfSlice.multiply(reward.getValue().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                distributeDiscountProportionally(allEligibleItems, totalDiscountForSlice);
            }
        }

        if (totalDiscountForSlice.compareTo(BigDecimal.ZERO) > 0) {
            context.logAppliedDiscount(promotion.getPromoCode(), "Cumulative Slice Discount", totalDiscountForSlice);
        }
    }

    private void distributeDiscountProportionally(List<CartItemContext> items, BigDecimal totalDiscount) {
        BigDecimal totalRemainingPrice = items.stream()
                .map(CartItemContext::getRemainingPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (totalRemainingPrice.compareTo(BigDecimal.ZERO) <= 0) return;

        BigDecimal allocatedDiscount = BigDecimal.ZERO;
        for (int i = 0; i < items.size() - 1; i++) {
            CartItemContext item = items.get(i);
            BigDecimal proportion = item.getRemainingPrice().divide(totalRemainingPrice, 4, RoundingMode.HALF_UP);
            BigDecimal discountForThisItem = totalDiscount.multiply(proportion).setScale(2, RoundingMode.HALF_UP);
            item.applyDiscount(discountForThisItem);
            allocatedDiscount = allocatedDiscount.add(discountForThisItem);
        }
        
        if (!items.isEmpty()) {
            CartItemContext lastItem = items.get(items.size() - 1);
            BigDecimal remainingDiscount = totalDiscount.subtract(allocatedDiscount);
            lastItem.applyDiscount(remainingDiscount);
        }
    }

    private BigDecimal calculatePriceForQuantitySlice(List<CartItemContext> items, int quantitySlice) {
        BigDecimal price = BigDecimal.ZERO;
        int quantityToAccountFor = quantitySlice;
        for (CartItemContext item : items) {
            if (quantityToAccountFor <= 0) break;
            int quantityFromThisItem = Math.min(quantityToAccountFor, item.getRemainingQuantityForRewards());
            BigDecimal pricePerUnit = item.getOriginalItem().getPrice();
            price = price.add(pricePerUnit.multiply(new BigDecimal(quantityFromThisItem)));
            item.consumeQuantity(quantityFromThisItem);
            quantityToAccountFor -= quantityFromThisItem;
        }
        return price;
    }

    private List<CartItemContext> findEligibleItemsForRule(PromotionContext context, PromotionRule rule) {
        List<ma.foodplus.ordering.system.promos.model.Condition> productConditions = rule.getConditions().stream()
                .filter(c -> c.getConditionType() == ma.foodplus.ordering.system.promos.model.Condition.ConditionType.PRODUCT_IN_CART)
                .collect(Collectors.toList());

        if (productConditions.isEmpty()) {
            return context.getCart().getItems();
        }

        return productConditions.stream()
                .flatMap(condition -> {
                    if ("PRODUCT".equalsIgnoreCase(condition.getEntityType())) {
                        return context.getCart().findItemsByProductId(Long.valueOf(condition.getEntityId())).stream();
                    } else if ("PRODUCT_FAMILY".equalsIgnoreCase(condition.getEntityType())) {
                        return context.getCart().findItemsByFamilyId(Long.valueOf(condition.getEntityId())).stream();
                    }
                    return null;
                })
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}