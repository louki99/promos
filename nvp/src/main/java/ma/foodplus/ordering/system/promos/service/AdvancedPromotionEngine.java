package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"promotions"})
public class AdvancedPromotionEngine {

    private final PromotionRepository promotionRepository;
    private final ConditionEvaluator conditionEvaluator;
    private final RewardApplicator rewardApplicator;
    
    private static final String ACTIVE_PROMOTIONS_CACHE = "active_promotions";
    private static final String PROMOTION_RESULTS_CACHE = "promotion_results";
    private static final String BEST_COMBINATIONS_CACHE = "best_combinations";

    @Cacheable(value = PROMOTION_RESULTS_CACHE, key = "#initialOrder.hashCode()")
    public PromotionContext apply(Order initialOrder) {
        validateOrder(initialOrder);
        PromotionContext context = new PromotionContext(initialOrder);
        
        List<Promotion> sortedPromotions = getActivePromotions();
        log.info("Found {} active promotions to evaluate.", sortedPromotions.size());

        for (Promotion currentPromotion : sortedPromotions) {
            if (!isPromotionValid(currentPromotion, initialOrder)) {
                log.debug("Skipping invalid promotion: {}", currentPromotion.getPromoCode());
                continue;
            }

            if (!isCombinable(context, currentPromotion)) {
                log.debug("Skipping non-combinable promotion: {}", currentPromotion.getPromoCode());
                continue;
            }

            boolean wasApplied = processPromotionRules(context, currentPromotion);

            if (wasApplied) {
                log.info("Successfully applied promotion '{}'", currentPromotion.getPromoCode());
                context.markPromotionAsApplied(currentPromotion);
                
                if (currentPromotion.isExclusive()) {
                    log.info("Exclusive promotion applied, stopping further promotions");
                    break;
                }
            }
        }
        
        return context;
    }

    @Cacheable(value = ACTIVE_PROMOTIONS_CACHE, key = "'all'")
    private List<Promotion> getActivePromotions() {
        return promotionRepository.findActivePromotions(ZonedDateTime.now()).stream()
                .sorted(Comparator.comparingInt(Promotion::getPriority))
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {ACTIVE_PROMOTIONS_CACHE, PROMOTION_RESULTS_CACHE, BEST_COMBINATIONS_CACHE}, allEntries = true)
    public void clearPromotionCaches() {
        log.info("Clearing all promotion caches");
    }

    @Cacheable(value = PROMOTION_RESULTS_CACHE, key = "#order.hashCode() + #promotion.id")
    public PromotionContext applyPromotion(Order order, Promotion promotion) {
        validateOrder(order);
        validatePromotion(promotion);

        if (!isPromotionValid(promotion, order)) {
            log.warn("Attempted to apply invalid promotion '{}'", promotion.getPromoCode());
            return new PromotionContext(order);
        }

        PromotionContext context = new PromotionContext(order);
        boolean wasApplied = processPromotionRules(context, promotion);
        
        if (wasApplied) {
            log.info("Successfully applied single promotion '{}'", promotion.getPromoCode());
            context.markPromotionAsApplied(promotion);
        }
        
        return context;
    }

    private boolean isPromotionValid(Promotion promotion, Order order) {
        if (!promotion.isActive(ZonedDateTime.now())) {
            return false;
        }

        if (promotion.getRules() == null || promotion.getRules().isEmpty()) {
            log.warn("Promotion '{}' has no rules", promotion.getPromoCode());
            return false;
        }

        if (promotion.getDynamicConditions() != null) {
            return promotion.getDynamicConditions().stream()
                    .allMatch(condition -> conditionEvaluator.evaluateDynamicCondition(condition, order));
        }

        return true;
    }

    private boolean processPromotionRules(PromotionContext context, Promotion promotion) {
        boolean hasBeenApplied = false;
        
        for (PromotionRule rule : promotion.getRules()) {
            try {
                if (validateRule(rule) && 
                    conditionEvaluator.evaluate(context.getOrder(), rule.getConditions(), rule.getConditionLogic())) {
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

    @Cacheable(value = BEST_COMBINATIONS_CACHE, key = "#order.hashCode()")
    public List<Promotion> getBestPromotionCombination(Order order) {
        validateOrder(order);
        List<Promotion> activePromotions = getActivePromotions();
        List<Promotion> bestCombination = new ArrayList<>();
        double maxDiscount = 0.0;

        activePromotions.sort(Comparator.comparingInt(Promotion::getPriority));

        for (int i = 0; i < activePromotions.size(); i++) {
            List<Promotion> currentCombination = new ArrayList<>();
            PromotionContext context = new PromotionContext(order);
            double currentDiscount = 0.0;

            for (int j = i; j < activePromotions.size(); j++) {
                Promotion currentPromo = activePromotions.get(j);
                
                if (isCombinable(context, currentPromo)) {
                    PromotionContext result = applyPromotion(context.getOrder(), currentPromo);
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

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }

    private void validatePromotion(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }
        if (promotion.getPromoCode() == null || promotion.getPromoCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Promotion code cannot be empty");
        }
    }

    private boolean validateRule(PromotionRule rule) {
        if (rule == null) {
            return false;
        }
        if (rule.getConditions() == null || rule.getConditions().isEmpty()) {
            return false;
        }
        if (rule.getTiers() == null || rule.getTiers().isEmpty()) {
            return false;
        }
        return true;
    }
}
