package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.dto.PromotionDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionRuleDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionTierDTO;
import ma.foodplus.ordering.system.promos.dto.ConditionDTO;
import ma.foodplus.ordering.system.promos.mapper.PromotionMapper;
import ma.foodplus.ordering.system.product.service.ProductService;
import ma.foodplus.ordering.system.promos.service.PromoFamilyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.time.ZonedDateTime;

@Service
@Transactional
public class PromotionCalculationService {

    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;
    private final ProductService productService;
    private final PromoFamilyService promoFamilyService;

    public PromotionCalculationService(
            PromotionService promotionService,
            PromotionMapper promotionMapper,
            ProductService productService,
            PromoFamilyService promoFamilyService) {
        this.promotionService = promotionService;
        this.promotionMapper = promotionMapper;
        this.productService = productService;
        this.promoFamilyService = promoFamilyService;
    }

    public BigDecimal calculateNestedPromotions(Integer promotionId, Map<Long, Integer> basketItems) {
        PromotionDTO promotionDTO = promotionService.getPromotionById(promotionId);
        if (promotionDTO == null || !promotionDTO.isActive(ZonedDateTime.now())) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalDiscount = BigDecimal.ZERO;
        Set<Integer> processedPromotions = new HashSet<>();

        // Calculate main promotion
        totalDiscount = totalDiscount.add(calculatePromotionDiscount(promotionDTO, basketItems));
        processedPromotions.add(promotionId);

        // Find nested promotions
        List<PromotionDTO> nestedPromotions = promotionService.findByParentPromotionId(promotionId);
        for (PromotionDTO nestedPromo : nestedPromotions) {
            if (!processedPromotions.contains(nestedPromo.getId()) && 
                isEligibleForNestedPromotion(nestedPromo, basketItems)) {
                totalDiscount = totalDiscount.add(calculatePromotionDiscount(nestedPromo, basketItems));
                processedPromotions.add(nestedPromo.getId());
            }
        }

        return totalDiscount;
    }

    public int calculateProductPoints(Integer promotionId, Long productId, int quantity) {
        PromotionDTO promotionDTO = promotionService.getPromotionById(promotionId);
        if (promotionDTO == null || !promotionDTO.isActive(ZonedDateTime.now())) {
            return 0;
        }

        Map<Long, Integer> productPoints = promotionDTO.getProductPoints();
        if (productPoints == null || !productPoints.containsKey(productId)) {
            return 0;
        }

        return productPoints.get(productId) * quantity;
    }

    private BigDecimal calculatePromotionDiscount(PromotionDTO promotionDTO, Map<Long, Integer> basketItems) {
        if (!promotionDTO.isWithinTimeRestriction()) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = BigDecimal.ZERO;
        for (PromotionRuleDTO ruleDTO : promotionDTO.getRules()) {
            if (evaluateConditions(ruleDTO, basketItems)) {
                BigDecimal ruleValue = calculateRuleValue(ruleDTO, basketItems);
                if (ruleDTO.getCalculationMethod() == PromotionRuleDTO.CalculationMethod.BRACKET) {
                    discount = discount.add(ruleValue);
                } else if (ruleDTO.getCalculationMethod() == PromotionRuleDTO.CalculationMethod.CUMULATIVE) {
                    discount = discount.add(ruleValue);
                }
            }
        }
        return discount;
    }

    private boolean evaluateConditions(PromotionRuleDTO ruleDTO, Map<Long, Integer> basketItems) {
        if (ruleDTO.getConditions() == null || ruleDTO.getConditions().isEmpty()) {
            return true;
        }

        boolean result = true;
        for (ConditionDTO condition : ruleDTO.getConditions()) {
            boolean conditionResult = evaluateCondition(condition, basketItems);
            
            // Apply condition logic (AND/OR)
            if (condition.getLogic() == ConditionDTO.ConditionLogic.AND) {
                result = result && conditionResult;
            } else {
                result = result || conditionResult;
            }
        }
        
        return result;
    }

    private boolean evaluateCondition(ConditionDTO condition, Map<Long, Integer> basketItems) {
        switch (condition.getConditionType()) {
            case MINIMUM_AMOUNT:
                return evaluateMinimumAmount(condition, basketItems);
            case MINIMUM_QUANTITY:
                return evaluateMinimumQuantity(condition, basketItems);
            case PRODUCT_IN_CART:
                return evaluateProductInCart(condition, basketItems);
            case CATEGORY_IN_CART:
                return evaluateCategoryInCart(condition, basketItems);
            case CUSTOMER_GROUP:
                return evaluateCustomerGroup(condition);
            default:
                return false;
        }
    }

    private boolean evaluateMinimumAmount(ConditionDTO condition, Map<Long, Integer> basketItems) {
        BigDecimal basketTotal = calculateAmountValue(basketItems);
        return basketTotal.compareTo(condition.getThreshold()) >= 0;
    }

    private boolean evaluateMinimumQuantity(ConditionDTO condition, Map<Long, Integer> basketItems) {
        int totalQuantity = calculateQuantityValue(basketItems);
        return totalQuantity >= condition.getThreshold().intValue();
    }

    private boolean evaluateProductInCart(ConditionDTO condition, Map<Long, Integer> basketItems) {
        return basketItems.containsKey(Long.parseLong(condition.getEntityId()));
    }

    private boolean evaluateCategoryInCart(ConditionDTO condition, Map<Long, Integer> basketItems) {
        String familyCode = condition.getEntityId();
        for (Long productId : basketItems.keySet()) {
            String productFamily = productService.getProductFamily(productId);
            if (productFamily != null && promoFamilyService.isMemberInFamily(familyCode, productFamily)) {
                return true;
            }
        }
        return false;
    }

    private boolean evaluateCustomerGroup(ConditionDTO condition) {
        String customerCode = condition.getEntityId();
        String familyCode = condition.getThreshold().toString();
        return promoFamilyService.isMemberInFamily(familyCode, customerCode);
    }

    private BigDecimal calculateRuleValue(PromotionRuleDTO ruleDTO, Map<Long, Integer> basketItems) {
        BigDecimal totalValue = BigDecimal.ZERO;
        
        switch (ruleDTO.getBreakpointType()) {
            case AMOUNT:
                totalValue = calculateAmountValue(basketItems);
                break;
            case QUANTITY:
                totalValue = new BigDecimal(calculateQuantityValue(basketItems));
                break;
            case SKU_POINTS:
                totalValue = new BigDecimal(calculatePointsValue(ruleDTO.getPromotionId(), basketItems));
                break;
        }

        PromotionTierDTO applicableTier = findApplicableTier(ruleDTO, totalValue);
        if (applicableTier != null) {
            return applicableTier.getDiscountAmount();
        }
        
        return BigDecimal.ZERO;
    }

    private PromotionTierDTO findApplicableTier(PromotionRuleDTO ruleDTO, BigDecimal value) {
        return ruleDTO.getTiers().stream()
            .filter(tier -> value.compareTo(tier.getMinimumThreshold()) >= 0)
            .max(Comparator.comparing(PromotionTierDTO::getMinimumThreshold))
            .orElse(null);
    }

    private BigDecimal calculateAmountValue(Map<Long, Integer> basketItems) {
        // TODO: Implement actual basket total calculation using ProductService
        return BigDecimal.ZERO;
    }

    private int calculateQuantityValue(Map<Long, Integer> basketItems) {
        return basketItems.values().stream().mapToInt(Integer::intValue).sum();
    }

    private int calculatePointsValue(Integer promotionId, Map<Long, Integer> basketItems) {
        int totalPoints = 0;
        for (Map.Entry<Long, Integer> entry : basketItems.entrySet()) {
            totalPoints += calculateProductPoints(promotionId, entry.getKey(), entry.getValue());
        }
        return totalPoints;
    }

    private boolean isEligibleForNestedPromotion(PromotionDTO promotionDTO, Map<Long, Integer> basketItems) {
        if (!promotionDTO.isNestedPromotion() || !promotionDTO.isActive(ZonedDateTime.now())) {
            return false;
        }

        // Check minimum purchase
        if (promotionDTO.getMinPurchaseAmount() != null) {
            BigDecimal basketTotal = calculateAmountValue(basketItems);
            if (basketTotal.compareTo(promotionDTO.getMinPurchaseAmount()) < 0) {
                return false;
            }
        }

        // Check excluded products
        if (promotionDTO.getExcludedProductIds() != null) {
            for (Long productId : basketItems.keySet()) {
                if (promotionDTO.getExcludedProductIds().contains(productId)) {
                    return false;
                }
            }
        }

        // Check excluded product families
        if (promotionDTO.getExcludedFamilyCodes() != null) {
            for (Long productId : basketItems.keySet()) {
                String productFamily = productService.getProductFamily(productId);
                if (productFamily != null && promotionDTO.getExcludedFamilyCodes().contains(productFamily)) {
                    return false;
                }
            }
        }

        return true;
    }
} 