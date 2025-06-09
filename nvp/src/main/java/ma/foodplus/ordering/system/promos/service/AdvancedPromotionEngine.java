package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.component.Cart;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvancedPromotionEngine {

    private final PromotionRepository promotionRepository;
    private final ConditionEvaluator conditionEvaluator;
    private final RewardApplicator rewardApplicator;

    /**
     * The main entry point to apply all possible promotions to a cart.
     * It fetches all active promotions and applies them sequentially based on priority.
     */
    public PromotionContext apply(Cart initialCart) {
        PromotionContext context = new PromotionContext(initialCart);
        
        // Step 1: Fetch all active promotions and sort by priority
        List<Promotion> activePromotions = promotionRepository.findActivePromotions(ZonedDateTime.now());
        List<Promotion> sortedPromotions = activePromotions.stream()
                .sorted(Comparator.comparingInt(Promotion::getPriority))
                .collect(Collectors.toList());
        
        log.info("Found {} active promotions to evaluate.", sortedPromotions.size());

        // Step 2: Process promotions in priority order
        for (Promotion currentPromotion : sortedPromotions) {
            // Skip if promotion is not active
            if (!currentPromotion.isActive(ZonedDateTime.now())) {
                log.debug("Skipping inactive promotion '{}'", currentPromotion.getPromoCode());
                continue;
            }

            // Check for combinability
            if (!isCombinable(context, currentPromotion)) {
                log.debug("Skipping non-combinable promotion '{}'", currentPromotion.getPromoCode());
                continue;
            }

            // Process promotion rules
            boolean wasApplied = processPromotionRules(context, currentPromotion);

            if (wasApplied) {
                log.info("Successfully applied promotion '{}'", currentPromotion.getPromoCode());
                context.markPromotionAsApplied(currentPromotion);
                
                // Handle exclusive promotions
                if (currentPromotion.isExclusive()) {
                    log.info("Exclusive promotion '{}' applied, stopping further promotions", 
                            currentPromotion.getPromoCode());
                    break;
                }
            }
        }
        
        return context;
    }

    /**
     * Apply a specific promotion to a cart.
     * Useful for testing and simulation scenarios.
     */
    public PromotionContext applyPromotion(Cart cart, Promotion promotion) {
        if (!promotion.isActive(ZonedDateTime.now())) {
            log.warn("Attempted to apply inactive promotion '{}'", promotion.getPromoCode());
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
     * Process all rules for a given promotion and apply rewards if conditions are met.
     */
    private boolean processPromotionRules(PromotionContext context, Promotion promotion) {
        boolean hasBeenApplied = false;
        
        for (PromotionRule rule : promotion.getRules()) {
            try {
                if (conditionEvaluator.evaluate(context.getCart(), rule.getConditions(), rule.getConditionLogic())) {
                    rewardApplicator.apply(context, rule);
                    hasBeenApplied = true;
                    
                    // If promotion should only apply first matching rule
                    if (promotion.isApplyFirstMatchingRuleOnly()) {
                        log.debug("First matching rule applied for promotion '{}', stopping further rules", 
                                promotion.getPromoCode());
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("Error processing rule for promotion '{}': {}", promotion.getPromoCode(), e.getMessage());
                // Continue with next rule instead of failing the entire promotion
            }
        }
        
        return hasBeenApplied;
    }

    /**
     * Check if a promotion can be combined with already applied promotions.
     */
    private boolean isCombinable(PromotionContext context, Promotion promotion) {
        // Check if an exclusive promotion has already been applied
        if (context.hasExclusivePromotionApplied()) {
            return false;
        }

        // Check if current promotion is exclusive and other promotions are already applied
        if (promotion.isExclusive() && context.hasAppliedPromotions()) {
            return false;
        }

        // Check combinability groups
        if (promotion.getCombinabilityGroup() != null) {
            return !context.getAppliedCombinabilityGroups().contains(promotion.getCombinabilityGroup());
        }

        return true;
    }

    /**
     * Get the best combination of promotions for a given cart.
     * This method evaluates different combinations of promotions to find the one
     * that provides the maximum discount while respecting combinability rules.
     */
    public List<Promotion> getBestPromotionCombination(Cart cart) {
        List<Promotion> activePromotions = promotionRepository.findActivePromotions(ZonedDateTime.now());
        List<Promotion> bestCombination = new ArrayList<>();
        double maxDiscount = 0.0;

        // Sort promotions by priority
        activePromotions.sort(Comparator.comparingInt(Promotion::getPriority));

        // Try different combinations
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
