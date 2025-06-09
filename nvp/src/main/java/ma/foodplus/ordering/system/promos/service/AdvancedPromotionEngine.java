package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.component.Cart;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvancedPromotionEngine {

    private final PromotionRepository promotionRepository;
    private final ConditionEvaluator conditionEvaluator; // خدمة لتقييم الشروط
    private final RewardApplicator rewardApplicator;   // خدمة لتطبيق المكافآت

    public Cart apply(Cart initialCart) {
        // 1. إنشاء سياق (Context) جديد لكل عملية
        PromotionContext context = new PromotionContext(initialCart);

        // 2. جلب العروض المرتبة حسب الأولوية
        List<Promotion> promotions = promotionRepository.findActivePromotions(ZonedDateTime.now());

        // 3. المرور على العروض لتطبيقها
        for (Promotion promotion : promotions) {
            // التحقق من التوافق
            if (isCombinable(context, promotion)) {
                processPromotionRules(context, promotion);
            }
        }

        return context.getCart();
    }

    /**
     * Applies a specific promotion to a cart.
     *
     * @param cart The cart to apply the promotion to
     * @param promotion The promotion to apply
     * @return The cart with the promotion applied
     */
    public Cart applyPromotion(Cart cart, Promotion promotion) {
        PromotionContext context = new PromotionContext(cart);
        processPromotionRules(context, promotion);
        return context.getCart();
    }

    private void processPromotionRules(PromotionContext context, Promotion promotion) {
        for (PromotionRule rule : promotion.getRules()) {
            // تقييم ما إذا كانت كل شروط القاعدة متحققة
            if (conditionEvaluator.evaluate(context.getCart(), rule.getConditions(), rule.getConditionLogic())) {
                // إذا تحققت الشروط، قم بتطبيق المكافأة
                rewardApplicator.apply(context, rule);
            }
        }
    }

    private boolean isCombinable(PromotionContext context, Promotion promotion) {
        if (context.isExclusivePromotionApplied()) {
            return false; // تم تطبيق عرض حصري بالفعل
        }
        if (promotion.isExclusive() && !context.getAppliedCombinabilityGroups().isEmpty()) {
            return false; // هذا العرض حصري، وهناك عروض أخرى مطبقة
        }
        if (promotion.getCombinabilityGroup() != null && context.getAppliedCombinabilityGroups().contains(promotion.getCombinabilityGroup())) {
            return false; // تم استخدام مجموعة التوافق هذه بالفعل
        }
        return true;
    }
}
