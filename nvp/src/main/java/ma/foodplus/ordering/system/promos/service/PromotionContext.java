package ma.foodplus.ordering.system.promos.service;

import lombok.Getter;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.promos.component.AppliedPromotion;
import ma.foodplus.ordering.system.promos.dto.FreeItemLog;
import ma.foodplus.ordering.system.promos.model.Promotion;

import java.math.BigDecimal;
import java.util.*;

/**
 * A stateful context object that holds the live state of a promotion calculation run.
 *
 * It encapsulates the cart, tracks which promotions have been applied, which
 * combinability groups have been used, and whether an exclusive promotion has
 .
 * This class is not thread-safe and is intended to be used for a single
 * execution of the promotion engine.
 */
@Getter
public class PromotionContext {

    private final Order order;
    private final Set<Promotion> appliedPromotions;
    private final Set<String> appliedCombinabilityGroups;
    private final Map<String, BigDecimal> appliedDiscounts;
    private final Map<String, List<FreeItem>> freeItems;

    // --- State Tracking Fields ---

    /** A flag that is set to true once an exclusive promotion is applied, preventing any further promotions. */
    private boolean exclusivePromotionApplied = false;

    /** A log of all promotions that were successfully applied, along with their impact. */
    private final List<AppliedPromotion> appliedPromotionsLog = new ArrayList<>();

    /** A map to store free items granted by promotions. Key: Product ID, Value: A simple holder for quantity and reason. */
    private final Map<Long, FreeItemLog> freeItemsLog = new HashMap<>();

    /**
     * Constructs a new PromotionContext for a given cart.
     * @param order The initial cart to be processed.
     */
    public PromotionContext(Order order) {
        if (order== null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        this.order=order;
        this.appliedPromotions = new HashSet<>();
        this.appliedCombinabilityGroups = new HashSet<>();
        this.appliedDiscounts = new HashMap<>();
        this.freeItems = new HashMap<>();
    }


    // --- Methods for Updating State (called by RewardApplicator) ---

    /**
     * Marks a promotion as applied by updating the context's state.
     * This should be called by the RewardApplicator immediately after a reward is applied.
     *
     * @param promotion The promotion that was just applied.
     */
    public void markPromotionAsApplied(Promotion promotion) {
        appliedPromotions.add(promotion);
        if (promotion.isExclusive()) {
            this.exclusivePromotionApplied = true;
        }
        if (promotion.getCombinabilityGroup() != null && !promotion.getCombinabilityGroup().isBlank()) {
            this.appliedCombinabilityGroups.add(promotion.getCombinabilityGroup());
        }
    }

    /**
     * Logs the details of an applied promotion for the final response.
     *
     * @param promoCode The code of the promotion.
     * @param description A summary of the benefit.
     * @param discountAmount The monetary value of the discount.
     */
    public void logAppliedDiscount(String promoCode, String description, BigDecimal discountAmount) {
        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.appliedPromotionsLog.add(new AppliedPromotion(promoCode, description, discountAmount, Collections.emptyList()));
            appliedDiscounts.merge(promoCode, discountAmount, BigDecimal::add);
        }
    }

    /**
     * Logs a free item granted by a promotion.
     * If the same product is granted as a free item by multiple promotions, their quantities are summed up.
     *
     * @param productId The ID of the free product.
     * @param quantity The number of free units.
     * @param promoCode The code of the promotion granting the item.
     */
    public void logFreeItem(Long productId, int quantity, String promoCode) {
        this.freeItemsLog.compute(productId, (key, existingLog) -> {
            if (existingLog == null) {
                return new FreeItemLog(quantity, promoCode);
            } else {
                existingLog.addQuantity(quantity);
                return existingLog;
            }
        });
        freeItems.computeIfAbsent(promoCode, k -> new ArrayList<>())
                .add(new FreeItem(productId, quantity));
    }


    // --- Methods for Querying State (called by the Engine and Applicator) ---

    public Order getOrder() {
        return order;
    }

    public boolean isExclusivePromotionApplied() {
        return exclusivePromotionApplied;
    }

    public Set<String> getUsedCombinabilityGroups() {
        // Return a copy to prevent external modification
        return new HashSet<>(appliedCombinabilityGroups);
    }

    public List<AppliedPromotion> getAppliedPromotionsLog() {
        // Return an unmodifiable view to protect the internal list
        return Collections.unmodifiableList(appliedPromotionsLog);
    }

    public Map<Long, FreeItemLog> getFreeItemsLog() {
        // Return an unmodifiable view
        return Collections.unmodifiableMap(freeItemsLog);
    }

    /**
     * Returns the set of combinability group names that have been used so far in this context.
     * The `isCombinable` method in the engine uses this to prevent applying multiple promotions
     * from the same group.
     *
     * @return A read-only set of used combinability group names.
     */
    public Set<String> getAppliedCombinabilityGroups() {
        // I am returning the direct reference here for performance. Since the Set itself is final
        // and only modified internally, this is safe within the context of a single-threaded engine run.
        // For absolute safety, one could return `new HashSet<>(appliedCombinabilityGroups)`, but it's often not necessary.
        return this.appliedCombinabilityGroups;
    }

    public boolean hasExclusivePromotionApplied() {
        return appliedPromotions.stream().anyMatch(Promotion::isExclusive);
    }

    public boolean hasAppliedPromotions() {
        return !appliedPromotions.isEmpty();
    }

    public BigDecimal getTotalDiscountApplied() {
        return appliedDiscounts.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static class FreeItem {
        private final Long productId;
        private final int quantity;

        public FreeItem(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    // This method lives inside the AdvancedPromotionEngine class.

    /**
     * Checks if a given promotion can be applied based on the current state of the context.
     * It enforces rules about exclusivity and combinability groups.
     *
     * @param context   The current PromotionContext, which holds the state of applied promotions.
     * @param promotion The promotion we are considering applying next.
     * @return True if the promotion is allowed to be applied, false otherwise.
     */
    private boolean isCombinable(PromotionContext context, Promotion promotion) {

        // --- Check 1: Has an exclusive promotion already been applied? ---
        // This calls the `isExclusivePromotionApplied()` method on the context.
        if (context.isExclusivePromotionApplied()) {
            // If true, it means a previous promotion with `isExclusive=true` was already applied.
            // We must stop and not apply any more promotions.
            return false;
        }

        // --- Check 2: Is the current promotion exclusive when others have already been applied? ---
        // If the promotion we are about to apply is exclusive...
        if (promotion.isExclusive()) {
            // ...we check if the log of applied promotions is NOT empty.
            // We can get this from the context. If the log is not empty, it means
            // other non-exclusive promotions have already run. An exclusive one cannot run after them.
            if (!context.getAppliedPromotionsLog().isEmpty()) {
                return false;
            }
        }

        // --- Check 3: Does this promotion belong to a group that has already been used? ---
        String group = promotion.getCombinabilityGroup();
        if (group != null && !group.isBlank()) {
            // This is the key line. It calls the method you asked about.
            // It gets the set of already used group names from the context...
            Set<String> usedGroups = context.getAppliedCombinabilityGroups();

            // ...and then checks if the current promotion's group is in that set.
            if (usedGroups.contains(group)) {
                // If it is, we cannot apply another promotion from the same group.
                return false;
            }
        }

        // If all checks pass, the promotion is combinable.
        return true;
    }
}