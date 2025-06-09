package ma.foodplus.ordering.system.promos.service;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.model.PromotionTier;
import ma.foodplus.ordering.system.promos.model.Reward;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ma.foodplus.ordering.system.promos.model.PromotionRule.BreakpointType.SKU_POINTS;

@Service
@RequiredArgsConstructor
public class RewardApplicator{

    @Qualifier("databaseProductSkuResolver")
    private final ProductSkuResolver productSkuResolver; // خدمة لحل نقاط SKU

    public void apply(PromotionContext context,PromotionRule rule){
        // تحديد المنتجات المؤهلة التي حققت الشرط
        List<CartItemContext> eligibleItems=findEligibleItemsForRule(context,rule);
        if(eligibleItems.isEmpty()) return;

        // حساب القيمة الأساسية
        BigDecimal breakpointValue=calculateBreakpointValue(eligibleItems,rule.getBreakpointType());

        // جلب الشرائح المرتبة
        List<PromotionTier> tiers=rule.getTiers().stream()
                .sorted(Comparator.comparing(PromotionTier::getMinimumThreshold))
                .collect(Collectors.toList());

        if(rule.getCalculationMethod()==PromotionRule.CalculationMethod.BRACKET){
            applyBracket(context,rule,eligibleItems,breakpointValue,tiers);
        }else{
            applyCumulative(context,rule,eligibleItems,breakpointValue,tiers);
        }
    }

    private BigDecimal calculateBreakpointValue(List<CartItemContext> eligibleItems, PromotionRule.BreakpointType breakpointType) {
        // ...
        case SKU_POINTS:
        return eligibleItems.stream()
                .map(item -> {
                    // This call now goes to the database via the DatabaseProductSkuResolver
                    BigDecimal pointsPerUnit = productSkuResolver.getSkuPointsForProduct(item.getOriginalItem().getProductId());
                    return pointsPerUnit.multiply(new BigDecimal(item.getOriginalItem().getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // ...
    }

    private void applyBracket(PromotionContext context, PromotionRule rule, List<CartItemContext> eligibleItems,
                              BigDecimal breakpointValue, List<PromotionTier> sortedTiers) {

        // إيجاد أفضل شريحة (الأعلى) التي تم تحقيقها
        PromotionTier bestTier = sortedTiers.stream()
                .filter(tier -> breakpointValue.compareTo(tier.getMinimumThreshold()) >= 0)
                .max(Comparator.comparing(PromotionTier::getMinimumThreshold))
                .orElse(null);

        if (bestTier == null) return;

        // تطبيق المكافأة الخاصة بهذه الشريحة
        applyRewardForTier(context, bestTier, eligibleItems, rule.getPromotion().getPromoCode());

        // تحديث سياق العرض
        updateContextAfterApplication(context, rule.getPromotion());
    }

    private void applyRewardForTier(PromotionContext context, PromotionTier tier, List<CartItemContext> itemsToReward, String promoCode) {
        Reward reward = tier.getReward();
        BigDecimal rewardValue = reward.getValue();
        BigDecimal totalDiscount = BigDecimal.ZERO;

        switch (reward.getRewardType()) {
            case PERCENT_DISCOUNT_ON_ITEM:
                // حساب إجمالي السعر المتبقي للمنتجات المستهدفة
                BigDecimal basePrice = itemsToReward.stream()
                        .map(CartItemContext::getRemainingPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                totalDiscount = basePrice.multiply(rewardValue.divide(new BigDecimal("100")));
                distributeDiscountProportionally(itemsToReward, totalDiscount);
                break;

            case FIXED_DISCOUNT_ON_CART:
                // تأكد من أن الخصم لا يتجاوز السعر المتبقي
                BigDecimal availablePrice = itemsToReward.stream()
                        .map(CartItemContext::getRemainingPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                totalDiscount = rewardValue.min(availablePrice);
                distributeDiscountProportionally(itemsToReward, totalDiscount);
                break;

            case FREE_PRODUCT:
                // إضافة منتج مجاني
                // context.getCart().addFreeItem(reward.getTargetEntityId(), rewardValue.intValue());
                break;
        }

        if (totalDiscount.compareTo(BigDecimal.ZERO) > 0) {
            // تسجيل العرض المطبق
            context.logAppliedPromotion(promoCode, "Bracket Discount", totalDiscount);
        }
    }

    private void applyCumulative(PromotionContext context, PromotionRule rule, List<CartItemContext> eligibleItems,
                                 BigDecimal totalBreakpointValue, List<PromotionTier> sortedTiers) {

        BigDecimal cumulativeValueProcessed = BigDecimal.ZERO;
        BigDecimal lastThreshold = BigDecimal.ZERO;

        for (PromotionTier tier : sortedTiers) {
            BigDecimal currentThreshold = tier.getMinimumThreshold();
            // حجم الشريحة الحالية
            BigDecimal bracketSize = currentThreshold.subtract(lastThreshold);

            // القيمة المتبقية من إجمالي قيمة العميل
            BigDecimal remainingValue = totalBreakpointValue.subtract(cumulativeValueProcessed);

            if (remainingValue.compareTo(BigDecimal.ZERO) <= 0) break;

            // القيمة التي ستقع فعليًا داخل هذه الشريحة
            BigDecimal valueInThisBracket = remainingValue.min(bracketSize);

            // تطبيق المكافأة على هذه الشريحة فقط
            applyRewardForSlice(context, tier, eligibleItems, valueInThisBracket, rule.getBreakpointType(), rule.getPromotion().getPromoCode());

            cumulativeValueProcessed = cumulativeValueProcessed.add(valueInThisBracket);
            lastThreshold = currentThreshold;
        }

        // التعامل مع أي قيمة متبقية تتجاوز أعلى شريحة
        BigDecimal finalRemainingValue = totalBreakpointValue.subtract(cumulativeValueProcessed);
        if (finalRemainingValue.compareTo(BigDecimal.ZERO) > 0) {
            PromotionTier highestTier = sortedTiers.get(sortedTiers.size() - 1);
            applyRewardForSlice(context, highestTier, eligibleItems, finalRemainingValue, rule.getBreakpointType(), rule.getPromotion().getPromoCode());
        }

        updateContextAfterApplication(context, rule.getPromotion());
    }

    private void applyRewardForSlice(PromotionContext context,PromotionTier tier,List<CartItemContext> allEligibleItems,
                                     BigDecimal valueSlice,PromotionRule.BreakpointType breakpointType,String promoCode) {

        Reward reward = tier.getReward();
        BigDecimal totalDiscountForSlice = BigDecimal.ZERO;

        if (reward.getRewardType() == Reward.RewardType.PERCENT_DISCOUNT_ON_ITEM) {
            // هنا الخوارزمية الدقيقة للتوزيع
            if (breakpointType == PromotionRule.BreakpointType.AMOUNT) {
                // إذا كان الأساس هو "المبلغ"، فإن الخصم مباشر
                totalDiscountForSlice = valueSlice.multiply(reward.getValue().divide(new BigDecimal("100")));
                // الآن يجب توزيع هذا الخصم على المنتجات التي "ساهمت" في هذه الشريحة
                distributeDiscountProportionally(allEligibleItems, totalDiscountForSlice);

            } else if (breakpointType == PromotionRule.BreakpointType.QUANTITY) {
                // هذا هو السيناريو الأكثر تعقيدًا
                // `valueSlice` يمثل عدد الوحدات (مثلاً: 5 وحدات)
                // يجب أن نجد السعر الإجمالي لهذه الوحدات الخمس لنحسب الخصم
                BigDecimal priceOfSlice = calculatePriceForQuantitySlice(allEligibleItems, valueSlice.intValue());
                totalDiscountForSlice = priceOfSlice.multiply(reward.getValue().divide(new BigDecimal("100")));
                // التوزيع سيكون أكثر تعقيدًا، ولكن للتسهيل، سنوزعه بشكل نسبي
                distributeDiscountProportionally(allEligibleItems, totalDiscountForSlice);
            }
        }
        // ... يمكن إضافة أنواع مكافآت أخرى للشرائح

        if (totalDiscountForSlice.compareTo(BigDecimal.ZERO) > 0) {
            context.logAppliedPromotion(promoCode, "Cumulative Slice Discount", totalDiscountForSlice);
        }
    }

    private void distributeDiscountProportionally(List<CartItemContext> items, BigDecimal totalDiscount) {
        // حساب إجمالي السعر المتبقي كأساس للتوزيع
        BigDecimal totalRemainingPrice = items.stream()
                .map(CartItemContext::getRemainingPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalRemainingPrice.compareTo(BigDecimal.ZERO) <= 0) return;

        BigDecimal allocatedDiscount = BigDecimal.ZERO;
        // المرور على كل المنتجات ما عدا الأخير
        for (int i = 0; i < items.size() - 1; i++) {
            CartItemContext item = items.get(i);
            // حساب نسبة مساهمة المنتج في السعر الإجمالي
            BigDecimal proportion = item.getRemainingPrice().divide(totalRemainingPrice, 4, RoundingMode.HALF_UP);
            BigDecimal discountForThisItem = totalDiscount.multiply(proportion).setScale(2, RoundingMode.HALF_UP);

            item.applyDiscount(discountForThisItem);
            allocatedDiscount = allocatedDiscount.add(discountForThisItem);
        }

        // إعطاء الخصم المتبقي (بسبب أخطاء التقريب) للمنتج الأخير
        CartItemContext lastItem = items.get(items.size() - 1);
        BigDecimal remainingDiscount = totalDiscount.subtract(allocatedDiscount);
        lastItem.applyDiscount(remainingDiscount);
    }

    private BigDecimal calculatePriceForQuantitySlice(List<CartItemContext> items, int quantitySlice) {
        BigDecimal price = BigDecimal.ZERO;
        int quantityToAccountFor = quantitySlice;

        // المرور على المنتجات واستهلاك الكمية المتبقية منها
        for (CartItemContext item : items) {
            if (quantityToAccountFor <= 0) break;

            int quantityFromThisItem = Math.min(quantityToAccountFor, item.getRemainingQuantity());
            BigDecimal pricePerUnit = item.getOriginalItem().getUnitPrice();

            price = price.add(pricePerUnit.multiply(new BigDecimal(quantityFromThisItem)));

            // تحديث الكمية المتبقية في سياق المنتج (مهم جدًا)
            item.consumeQuantity(quantityFromThisItem);
            quantityToAccountFor -= quantityFromThisItem;
        }
        return price;
    }
}