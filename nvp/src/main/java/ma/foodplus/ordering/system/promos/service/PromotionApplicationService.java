package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.product.service.ProductService;
import ma.foodplus.ordering.system.promos.component.Cart;
import ma.foodplus.ordering.system.promos.dto.*;
import ma.foodplus.ordering.system.promos.exception.PromotionApplicationException;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A high-level Facade service responsible for the entire promotion application workflow.
 * This service orchestrates the promotion calculation process by coordinating between
 * different components and providing a simplified interface for clients.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionApplicationService {

    private final AdvancedPromotionEngine promotionEngine;
    private final ProductService productService;
    private final PromotionRepository promotionRepository;
    private final ConditionEvaluator conditionEvaluator;

    /**
     * Calculates all applicable promotions for a given cart.
     *
     * @param request The request containing cart items and customer information
     * @return A response containing the calculated discounts and final prices
     * @throws PromotionApplicationException if the calculation fails
     */
    @Transactional(readOnly = true)
    public ApplyPromotionResponse calculatePromotions(ApplyPromotionRequest request) {
        try {
            validateRequest(request);
            Cart cart = createCartFromRequest(request);
            Cart finalCart = promotionEngine.apply(cart);
            return createResponseFromCart(finalCart);
        } catch (Exception e) {
            log.error("Failed to calculate promotions for request: {}", request, e);
            throw new PromotionApplicationException("Failed to calculate promotions", e);
        }
    }

    /**
     * Gets all active promotions that could potentially apply to a cart.
     *
     * @param request The cart request to check promotions for
     * @return List of eligible promotions
     */
    @Transactional(readOnly = true)
    public List<PromotionDTO> getEligiblePromotions(ApplyPromotionRequest request) {
        try {
            Cart cart = createCartFromRequest(request);
            List<Promotion> activePromotions = promotionRepository.findActivePromotions(ZonedDateTime.now());
            
            return activePromotions.stream()
                    .filter(promotion -> isPromotionEligible(cart, promotion))
                    .map(this::convertToPromotionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get eligible promotions for request: {}", request, e);
            throw new PromotionApplicationException("Failed to get eligible promotions", e);
        }
    }

    /**
     * Validates if a specific promotion code can be applied to the cart.
     *
     * @param request The cart request
     * @param promotionCode The promotion code to validate
     * @return True if the promotion can be applied
     */
    @Transactional(readOnly = true)
    public boolean validatePromotionCode(ApplyPromotionRequest request, String promotionCode) {
        try {
            Cart cart = createCartFromRequest(request);
            Optional<Promotion> promotion = promotionRepository.findByCode(promotionCode);
            
            return promotion.isPresent() && 
                   promotion.get().isActive(ZonedDateTime.now()) && 
                   isPromotionEligible(cart, promotion.get());
        } catch (Exception e) {
            log.error("Failed to validate promotion code: {} for request: {}", promotionCode, request, e);
            throw new PromotionApplicationException("Failed to validate promotion code", e);
        }
    }

    /**
     * Gets a detailed breakdown of how a specific promotion would affect the cart.
     *
     * @param request The cart request
     * @param promotionCode The promotion code to analyze
     * @return Detailed breakdown of the promotion's effects
     */
    @Transactional(readOnly = true)
    public PromotionBreakdownDTO getPromotionBreakdown(ApplyPromotionRequest request, String promotionCode) {
        try {
            Cart cart = createCartFromRequest(request);
            Optional<Promotion> promotion = promotionRepository.findByCode(promotionCode);
            
            if (promotion.isEmpty()) {
                throw new PromotionApplicationException("Promotion not found: " + promotionCode);
            }

            if (!isPromotionEligible(cart, promotion.get())) {
                throw new PromotionApplicationException("Promotion is not eligible for this cart");
            }

            // Apply the promotion to a copy of the cart to see its effects
            Cart cartWithPromotion = promotionEngine.applyPromotion(cart, promotion.get());
            
            return createPromotionBreakdown(cart, cartWithPromotion, promotion.get());
        } catch (Exception e) {
            log.error("Failed to get promotion breakdown for code: {} and request: {}", promotionCode, request, e);
            throw new PromotionApplicationException("Failed to get promotion breakdown", e);
        }
    }

    // --- Private Helper Methods ---

    private void validateRequest(ApplyPromotionRequest request) {
        if (request == null) {
            throw new PromotionApplicationException("Request cannot be null");
        }
        if (request.getCustomerId() == null) {
            throw new PromotionApplicationException("Customer ID cannot be null");
        }
        if (request.getCartItems() == null || request.getCartItems().isEmpty()) {
            throw new PromotionApplicationException("Cart items cannot be empty");
        }
    }

    private Cart createCartFromRequest(ApplyPromotionRequest request) {
        return new Cart(request.getCustomerId(), request.getCartItems());
    }

    private boolean isPromotionEligible(Cart cart, Promotion promotion) {
        return promotion.getRules().stream()
                .anyMatch(rule -> conditionEvaluator.evaluate(cart, rule.getConditions(), rule.getConditionLogic()));
    }

    private PromotionDTO convertToPromotionDTO(Promotion promotion) {
        return PromotionDTO.builder()
                .id(promotion.getId())
                .promoCode(promotion.getPromoCode())
                .name(promotion.getName())
                .description(promotion.getDescription())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .priority(promotion.getPriority())
                .isExclusive(promotion.isExclusive())
                .combinabilityGroup(promotion.getCombinabilityGroup())
                .build();
    }

    private ApplyPromotionResponse createResponseFromCart(Cart cart) {
        List<LineItemResultDto> lineItemResults = cart.getItems().stream()
                .map(ctx -> new LineItemResultDto(
                        ctx.getOriginalItem().getProductId(),
                        ctx.getOriginalItem().getProductName(),
                        ctx.getOriginalItem().getQuantity(),
                        ctx.getOriginalItem().getOriginalTotalPrice(),
                        ctx.getDiscountAmount()
                ))
                .collect(Collectors.toList());

        BigDecimal originalTotal = lineItemResults.stream()
                .map(LineItemResultDto::getOriginalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDiscount = lineItemResults.stream()
                .map(LineItemResultDto::getTotalDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal finalTotal = originalTotal.subtract(totalDiscount);

        return ApplyPromotionResponse.builder()
                .originalTotal(originalTotal)
                .discountTotal(totalDiscount)
                .finalTotal(finalTotal)
                .lineItems(lineItemResults)
                .build();
    }

    private PromotionBreakdownDTO createPromotionBreakdown(Cart originalCart, Cart cartWithPromotion, Promotion promotion) {
        Map<Long, BigDecimal> itemDiscounts = new HashMap<>();
        
        // Calculate discounts per item
        cartWithPromotion.getItems().forEach(ctx -> {
            BigDecimal originalPrice = ctx.getOriginalItem().getOriginalTotalPrice();
            BigDecimal finalPrice = ctx.getFinalPrice();
            BigDecimal discount = originalPrice.subtract(finalPrice);
            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                itemDiscounts.put(ctx.getOriginalItem().getProductId(), discount);
            }
        });

        return PromotionBreakdownDTO.builder()
                .promotionCode(promotion.getPromoCode())
                .promotionName(promotion.getName())
                .originalTotal(originalCart.getFinalTotalPrice())
                .discountTotal(cartWithPromotion.getFinalTotalPrice().subtract(originalCart.getFinalTotalPrice()))
                .finalTotal(cartWithPromotion.getFinalTotalPrice())
                .itemDiscounts(itemDiscounts)
                .build();
    }
}