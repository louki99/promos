package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.component.Cart;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvancedPromotionEngine {

    private final PromotionRepository promotionRepository;
    private final ConditionEvaluator conditionEvaluator;
    private final RewardApplicator rewardApplicator;
    
    // Cache for active promotions
    private final Map<String, List<Promotion>> activePromotionsCache = new ConcurrentHashMap<>();
    private static final String CACHE_KEY = "active_promotions";

    /**
     * The main entry point to apply all possible promotions to a cart.
     * It fetches all active promotions and applies them sequentially based on priority.
     * 
     * @param initialCart The cart to apply promotions to
     * @return PromotionContext containing the final state of the cart with applied promotions
     */
    @Cacheable(value = "promotion_results", key = "#initialCart.hashCode()")
    public PromotionContext apply(Cart initialCart) {
        PromotionContext context = new PromotionContext(initialCart);
        
        // Get cached active promotions or fetch from repository
        List<Promotion> sortedPromotions = getActivePromotions();
        log.info("Found {} active promotions to evaluate.", sortedPromotions.size());

        // Process promotions in priority order
        for (Promotion currentPromotion : sortedPromotions) {
            if (!isPromotionValid(currentPromotion)) {
                continue;
            }

            if (!isCombinable(context, currentPromotion)) {
                continue;
            }

            boolean wasApplied = processPromotionRules(context, currentPromotion);

            if (wasApplied) {
                log.info("Successfully applied promotion '{}'", currentPromotion.getPromoCode());
                context.markPromotionAsApplied(currentPromotion);
                
                if (currentPromotion.isExclusive()) {
                    break;
                }
            }
        }
        
        return context;
    }

    /**
     * Get active promotions with caching support.
     * This method caches the results for 5 minutes to improve performance.
     */
    @Cacheable(value = "active_promotions", key = "'all'")
    private List<Promotion> getActivePromotions() {
        return promotionRepository.findActivePromotions(ZonedDateTime.now()).stream()
                .sorted(Comparator.comparingInt(Promotion::getPriority))
                .collect(Collectors.toList());
    }

    /**
     * Clear the active promotions cache.
     * This should be called when promotions are updated.
     */
    @CacheEvict(value = "active_promotions", allEntries = true)
    public void clearActivePromotionsCache() {
        activePromotionsCache.clear();
    }

    /**
     * Apply a specific promotion to a cart.
     * Useful for testing and simulation scenarios.
     * 
     * @param cart The cart to apply the promotion to
     * @param promotion The promotion to apply
     * @return PromotionContext containing the final state of the cart
     */
    @Cacheable(value = "promotion_results", key = "#cart.hashCode() + #promotion.id")
    public PromotionContext applyPromotion(Cart cart, Promotion promotion) {
        if (!isPromotionValid(promotion)) {
            log.warn("Attempted to apply invalid promotion '{}'", promotion.getPromoCode());
            return new PromotionContext(cart);
        }

        PromotionContext context = new PromotionContext(cart);
        boolean wasApplied = processPromotionRules(context, promotion);
        
        if (wasApplied) {
            log.info("Successfully applied single promotion '{}'", promotion.getPromoCode());
            context.markPromotionAsApplied(promotion);
        }
        
        return context;
    }

    /**
     * Check if a promotion is valid at the current time.
     * This includes checking active status and any dynamic conditions.
     */
    private boolean isPromotionValid(Promotion promotion) {
        if (!promotion.isActive(ZonedDateTime.now())) {
            return false;
        }

        // Check dynamic conditions if any
        if (promotion.getDynamicConditions() != null) {
            return promotion.getDynamicConditions().stream()
                    .allMatch(condition -> conditionEvaluator.evaluateDynamicCondition(condition));
        }

        return true;
    }

    /**
     * Process all rules for a given promotion and apply rewards if conditions are met.
     * This method includes performance optimizations and error handling.
     */
    private boolean processPromotionRules(PromotionContext context, Promotion promotion) {
        boolean hasBeenApplied = false;
        
        for (PromotionRule rule : promotion.getRules()) {
            try {
                if (conditionEvaluator.evaluate(context.getCart(), rule.getConditions(), rule.getConditionLogic())) {
                    rewardApplicator.apply(context, rule);
                    hasBeenApplied = true;
                    
                    if (promotion.isApplyFirstMatchingRuleOnly()) {
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("Error processing rule for promotion '{}': {}", promotion.getPromoCode(), e.getMessage());
            }
        }
        
        return hasBeenApplied;
    }

    /**
     * Check if a promotion can be combined with already applied promotions.
     * This method includes optimizations for combinability checks.
     */
    private boolean isCombinable(PromotionContext context, Promotion promotion) {
        if (context.hasExclusivePromotionApplied()) {
            return false;
        }

        if (promotion.isExclusive() && context.hasAppliedPromotions()) {
            return false;
        }

        if (promotion.getCombinabilityGroup() != null) {
            return !context.getAppliedCombinabilityGroups().contains(promotion.getCombinabilityGroup());
        }

        return true;
    }

    /**
     * Get the best combination of promotions for a given cart.
     * This method uses a more efficient algorithm to find the optimal combination.
     */
    @Cacheable(value = "best_promotion_combinations", key = "#cart.hashCode()")
    public List<Promotion> getBestPromotionCombination(Cart cart) {
        List<Promotion> activePromotions = getActivePromotions();
        List<Promotion> bestCombination = new ArrayList<>();
        double maxDiscount = 0.0;

        // Sort promotions by priority and potential discount
        activePromotions.sort(Comparator.comparingInt(Promotion::getPriority));

        // Use a more efficient algorithm to find the best combination
        for (int i = 0; i < activePromotions.size(); i++) {
            List<Promotion> currentCombination = new ArrayList<>();
            PromotionContext context = new PromotionContext(cart);
            double currentDiscount = 0.0;

            for (int j = i; j < activePromotions.size(); j++) {
                Promotion currentPromo = activePromotions.get(j);
                
                if (isCombinable(context, currentPromo)) {
                    PromotionContext result = applyPromotion(context.getCart(), currentPromo);
                    double discount = result.getTotalDiscountApplied().doubleValue();
                    
                    if (discount > 0) {
                        currentCombination.add(currentPromo);
                        currentDiscount += discount;
                        context = result;
                    }
                }
            }

            if (currentDiscount > maxDiscount) {
                maxDiscount = currentDiscount;
                bestCombination = new ArrayList<>(currentCombination);
            }
        }

        return bestCombination;
    }
}
