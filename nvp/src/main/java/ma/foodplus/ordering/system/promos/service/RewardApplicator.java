package ma.foodplus.ordering.system.promos.service;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.order.model.OrderItemContext;
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
        validateRule(rule);
        List<OrderItemContext> eligibleItems = findEligibleItemsForRule(context, rule);
        if (eligibleItems.isEmpty()) {
            return;
        }

        // --- Apply repetition limit if set ---
        Integer repetition = rule.getRepetition();
        if (repetition != null && repetition > 0) {
            eligibleItems = limitItemsByRepetition(eligibleItems, repetition, rule.getBreakpointType());
        }
        // --- End repetition logic ---

        BigDecimal breakpointValue = calculateBreakpointValue(eligibleItems, rule.getBreakpointType());
        if (breakpointValue.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        List<PromotionTier> tiers = rule.getTiers().stream()
                .sorted(Comparator.comparing(PromotionTier::getMinimumThreshold))
                .collect(Collectors.toList());

        if (rule.getCalculationMethod() == PromotionRule.CalculationMethod.BRACKET) {
            applyBracket(context, rule, eligibleItems, breakpointValue, tiers);
        } else {
            applyCumulative(context, rule, eligibleItems, breakpointValue, tiers);
        }
    }

    private void validateRule(PromotionRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }
        if (rule.getTiers() == null || rule.getTiers().isEmpty()) {
            throw new IllegalArgumentException("Rule must have at least one tier");
        }
        if (rule.getBreakpointType() == null) {
            throw new IllegalArgumentException("Breakpoint type cannot be null");
        }
    }

    private BigDecimal calculateBreakpointValue(List<OrderItemContext> eligibleItems, PromotionRule.BreakpointType breakpointType) {
        switch (breakpointType) {
            case AMOUNT:
                return eligibleItems.stream()
                        .map(item -> item.getOriginalItem().getUnitPrice().multiply(new BigDecimal(item.getOriginalItem().getQuantity())))
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

    private void applyBracket(PromotionContext context, PromotionRule rule, List<OrderItemContext> eligibleItems,
                            BigDecimal breakpointValue, List<PromotionTier> sortedTiers) {
        PromotionTier bestTier = sortedTiers.stream()
                .sorted(Comparator.comparing(PromotionTier::getMinimumThreshold).reversed())
                .filter(tier -> breakpointValue.compareTo(tier.getMinimumThreshold()) >= 0)
                .findFirst()
                .orElse(null);

        if (bestTier == null) {
            return;
        }

        applyRewardForTier(context, bestTier, eligibleItems, rule.getPromotion());
        updateContextAfterApplication(context, rule.getPromotion());
    }

    private void updateContextAfterApplication(PromotionContext context, Promotion promotion) {
        context.markPromotionAsApplied(promotion);
    }

    private void applyRewardForTier(PromotionContext context, PromotionTier tier, List<OrderItemContext> itemsToReward, Promotion promotion) {
        Reward reward = tier.getReward();
        if (reward == null) {
            throw new IllegalArgumentException("Reward cannot be null");
        }

        BigDecimal rewardValue = reward.getValue();
        if (rewardValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Reward value must be greater than zero");
        }

        BigDecimal totalDiscount = BigDecimal.ZERO;

        switch (reward.getRewardType()) {
            case DISCOUNT_PERCENTAGE:
                BigDecimal basePrice = itemsToReward.stream()
                        .map(OrderItemContext::getRemainingPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                totalDiscount = basePrice.multiply(rewardValue.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                distributeDiscountProportionally(itemsToReward, totalDiscount);
                break;

            case DISCOUNT_AMOUNT:
                BigDecimal availablePrice = itemsToReward.stream()
                        .map(OrderItemContext::getRemainingPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                totalDiscount = rewardValue.min(availablePrice);
                distributeDiscountProportionally(itemsToReward, totalDiscount);
                break;

            case FREE_PRODUCT:
                if (reward.getProductId() == null) {
                    throw new IllegalArgumentException("Product ID is required for free product reward");
                }
                int quantity = rewardValue.intValue();
                if (quantity <= 0) {
                    throw new IllegalArgumentException("Free product quantity must be greater than zero");
                }
                context.logFreeItem(Long.valueOf(reward.getProductId()), quantity, promotion.getPromoCode());
                break;

            default:
                throw new IllegalArgumentException("Unsupported reward type: " + reward.getRewardType());
        }

        if (totalDiscount.compareTo(BigDecimal.ZERO) > 0) {
            context.logAppliedDiscount(promotion.getPromoCode(), "Bracket Discount: " + reward.getRewardType(), totalDiscount);
        }
    }

    private void applyCumulative(PromotionContext context, PromotionRule rule, List<OrderItemContext> eligibleItems,
                               BigDecimal totalBreakpointValue, List<PromotionTier> sortedTiers) {
        BigDecimal lastThreshold = BigDecimal.ZERO;

        for (PromotionTier tier : sortedTiers) {
            if (totalBreakpointValue.compareTo(lastThreshold) <= 0) {
                break;
            }

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

    private void applyRewardForSlice(PromotionContext context, PromotionTier tier, List<OrderItemContext> allEligibleItems,
                                   BigDecimal valueSlice, PromotionRule.BreakpointType breakpointType, Promotion promotion) {
        Reward reward = tier.getReward();
        if (reward == null) {
            throw new IllegalArgumentException("Reward cannot be null");
        }

        BigDecimal totalDiscountForSlice = BigDecimal.ZERO;

        if (reward.getRewardType() == Reward.RewardType.DISCOUNT_PERCENTAGE) {
            if (breakpointType == PromotionRule.BreakpointType.AMOUNT) {
                totalDiscountForSlice = valueSlice.multiply(reward.getValue().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                distributeDiscountProportionally(allEligibleItems, totalDiscountForSlice);
            } else if (breakpointType == PromotionRule.BreakpointType.QUANTITY) {
                BigDecimal priceOfSlice = calculatePriceForQuantitySlice(allEligibleItems, valueSlice.intValue());
                totalDiscountForSlice = priceOfSlice.multiply(reward.getValue().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
                distributeDiscountProportionally(allEligibleItems, totalDiscountForSlice);
            }
        }

        if (totalDiscountForSlice.compareTo(BigDecimal.ZERO) > 0) {
            context.logAppliedDiscount(promotion.getPromoCode(), "Cumulative Slice Discount", totalDiscountForSlice);
        }
    }

    private void distributeDiscountProportionally(List<OrderItemContext> items, BigDecimal totalDiscount) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be null or empty");
        }
        if (totalDiscount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total discount must be greater than zero");
        }

        BigDecimal totalRemainingPrice = items.stream()
                .map(OrderItemContext::getRemainingPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (totalRemainingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal allocatedDiscount = BigDecimal.ZERO;
        for (int i = 0; i < items.size() - 1; i++) {
            OrderItemContext item = items.get(i);
            BigDecimal proportion = item.getRemainingPrice().divide(totalRemainingPrice, 4, RoundingMode.HALF_UP);
            BigDecimal discountForThisItem = totalDiscount.multiply(proportion).setScale(2, RoundingMode.HALF_UP);
            item.applyDiscount(discountForThisItem);
            allocatedDiscount = allocatedDiscount.add(discountForThisItem);
        }
        
        if (!items.isEmpty()) {
            OrderItemContext lastItem = items.get(items.size() - 1);
            BigDecimal remainingDiscount = totalDiscount.subtract(allocatedDiscount);
            lastItem.applyDiscount(remainingDiscount);
        }
    }

    private BigDecimal calculatePriceForQuantitySlice(List<OrderItemContext> items, int quantitySlice) {
        if (quantitySlice <= 0) {
            throw new IllegalArgumentException("Quantity slice must be greater than zero");
        }

        BigDecimal price = BigDecimal.ZERO;
        int quantityToAccountFor = quantitySlice;
        
        for (OrderItemContext item : items) {
            if (quantityToAccountFor <= 0) {
                break;
            }
            int quantityFromThisItem = Math.min(quantityToAccountFor, item.getRemainingQuantityForRewards());
            BigDecimal pricePerUnit = item.getOriginalItem().getUnitPrice();
            price = price.add(pricePerUnit.multiply(new BigDecimal(quantityFromThisItem)));
            item.consumeQuantity(quantityFromThisItem);
            quantityToAccountFor -= quantityFromThisItem;
        }
        
        return price;
    }

    private List<OrderItemContext> findEligibleItemsForRule(PromotionContext context, PromotionRule rule) {
        List<OrderItemContext> allItems = context.getOrder().getItems().stream()
            .map(OrderItemContext::new)
            .collect(Collectors.toList());
        List<PromotionLine> lines = rule.getPromotion() != null ? rule.getPromotion().getPromotionLines() : null;
        if (lines == null || lines.isEmpty()) {
            return allItems;
        }
        return allItems.stream().filter(item ->
            lines.stream().anyMatch(line ->
                (line.getPaidProductId() != null && line.getPaidProductId().equals(item.getOriginalItem().getProductId())) ||
                (line.getPaidFamilyCode() != null && line.getPaidFamilyCode().equals(item.getOriginalItem().getProductFamilyId() != null ? item.getOriginalItem().getProductFamilyId().toString() : null))
            )
        ).collect(Collectors.toList());
    }

    private List<OrderItemContext> limitItemsByRepetition(List<OrderItemContext> items, int repetition, PromotionRule.BreakpointType type) {
        if (type == PromotionRule.BreakpointType.QUANTITY) {
            int total = 0;
            List<OrderItemContext> result = new java.util.ArrayList<>();
            for (OrderItemContext item : items) {
                int qty = item.getOriginalItem().getQuantity();
                if (total + qty > repetition) {
                    // Only take part of this item
                    // (Assume we can clone or partially use the item, or just break)
                    break;
                }
                result.add(item);
                total += qty;
                if (total >= repetition) break;
            }
            return result;
        }
        // For amount or SKU_POINTS, similar logic can be added if needed
        return items;
    }
}