package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import ma.foodplus.ordering.system.customer.service.CustomerService;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.enums.SuiviStock;
import ma.foodplus.ordering.system.product.service.ProductService;
import ma.foodplus.ordering.system.promos.dto.*;
import ma.foodplus.ordering.system.promos.exception.PromotionApplicationException;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionCustomerFamily;
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
    private final CustomerService customerService;

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
            Order order = createOrderFromRequest(request);
            PromotionContext context = promotionEngine.apply(order);
            return createResponseFromOrder(context.getOrder());
        } catch (Exception e) {
            log.error("Failed to calculate promotions for request: {}", request, e);
            throw new PromotionApplicationException("Failed to calculate promotions: " + e.getMessage(), e);
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
            Order order = createOrderFromRequest(request);
            List<Promotion> activePromotions = promotionRepository.findActivePromotions(ZonedDateTime.now());
            
            return activePromotions.stream()
                    .filter(promotion -> isPromotionEligible(order, promotion))
                    .map(this::convertToPromotionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get eligible promotions for request: {}", request, e);
            throw new PromotionApplicationException("Failed to get eligible promotions: " + e.getMessage(), e);
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
            Order order = createOrderFromRequest(request);
            Optional<Promotion> promotion = promotionRepository.findByPromoCode(promotionCode);
            
            if (promotion.isEmpty()) {
                return false;
            }

            Promotion promo = promotion.get();
            if (!promo.isActive(ZonedDateTime.now())) {
                return false;
            }

            return isPromotionEligible(order, promo);
        } catch (Exception e) {
            log.error("Failed to validate promotion code: {} for request: {}", promotionCode, request, e);
            throw new PromotionApplicationException("Failed to validate promotion code: " + e.getMessage(), e);
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
            Order order = createOrderFromRequest(request);
            Optional<Promotion> promotion = promotionRepository.findByPromoCode(promotionCode);
            
            if (promotion.isEmpty()) {
                throw new PromotionApplicationException("Promotion not found: " + promotionCode);
            }

            Promotion promo = promotion.get();
            if (!promo.isActive(ZonedDateTime.now())) {
                throw new PromotionApplicationException("Promotion is not active");
            }

            if (!isPromotionEligible(order, promo)) {
                throw new PromotionApplicationException("Promotion is not eligible for this order");
            }

            PromotionContext context = promotionEngine.applyPromotion(order, promo);
            return createPromotionBreakdown(order, context.getOrder(), promo);
        } catch (Exception e) {
            log.error("Failed to get promotion breakdown for code: {} and request: {}", promotionCode, request, e);
            throw new PromotionApplicationException("Failed to get promotion breakdown: " + e.getMessage(), e);
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
        if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
            throw new PromotionApplicationException("Order items cannot be empty");
        }
        if (request.getPromoCode() != null && !isValidPromoCodeFormat(request.getPromoCode())) {
            throw new PromotionApplicationException("Invalid promotion code format");
        }
    }

    private boolean isValidPromoCodeFormat(String promoCode) {
        // Promo code format: 2-10 alphanumeric characters, optional hyphens
        return promoCode != null && promoCode.matches("^[A-Za-z0-9-]{2,10}$");
    }

    private Order createOrderFromRequest(ApplyPromotionRequest request) {
        return new Order(request.getCustomerId(), request.getOrderItems());
    }

    private boolean isPromotionEligible(Order order, Promotion promotion) {
        // Check if promotion is expired
        if (promotion.getEndDate() != null && promotion.getEndDate().isBefore(ZonedDateTime.now())) {
            log.debug("Promotion {} is expired", promotion.getPromoCode());
            return false;
        }

        // Validate stock availability for all items
        if (!validateStockAvailability(order)) {
            log.debug("Insufficient stock for promotion {}", promotion.getPromoCode());
            return false;
        }

        // Validate customer eligibility
        if (!validateCustomerEligibility(order.getCustomerId(), promotion)) {
            log.debug("Customer {} is not eligible for promotion {}", order.getCustomerId(), promotion.getPromoCode());
            return false;
        }

        // Check usage limits
        if (promotion.getMaxUsageCount() != null && 
            promotion.getCurrentUsageCount() >= promotion.getMaxUsageCount()) {
            log.debug("Promotion {} has reached its usage limit", promotion.getPromoCode());
            return false;
        }

        // Check per-customer usage limits
        if (promotion.getMaxUsagePerCustomer() != null) {
            int customerUsageCount = promotion.getCustomerUsageCount(order.getCustomerId());
            if (customerUsageCount >= promotion.getMaxUsagePerCustomer()) {
                log.debug("Customer {} has reached usage limit for promotion {}", 
                    order.getCustomerId(), promotion.getPromoCode());
                return false;
            }
        }

        // Check minimum purchase requirement
        if (promotion.getMinPurchaseAmount() != null && 
            order.getTotal().compareTo(promotion.getMinPurchaseAmount()) < 0) {
            log.debug("Order total {} is below minimum purchase amount {} for promotion {}", 
                order.getTotal(), promotion.getMinPurchaseAmount(), promotion.getPromoCode());
            return false;
        }

        // Check if any excluded products are in the cart
        if (promotion.getExcludedProductIds() != null && !promotion.getExcludedProductIds().isEmpty()) {
            boolean hasExcludedProduct = order.getItems().stream()
                .anyMatch(item -> promotion.getExcludedProductIds().contains(item.getProductId()));
            if (hasExcludedProduct) {
                log.debug("Order contains excluded products for promotion {}", promotion.getPromoCode());
                return false;
            }
        }

        // Check if any excluded categories are in the cart
        if (promotion.getExcludedCategoryIds() != null && !promotion.getExcludedCategoryIds().isEmpty()) {
            boolean hasExcludedCategory = order.getItems().stream()
                .anyMatch(item -> promotion.getExcludedCategoryIds().contains(item.getProductFamilyId()));
            if (hasExcludedCategory) {
                log.debug("Order contains excluded categories for promotion {}", promotion.getPromoCode());
                return false;
            }
        }

        return promotion.getRules().stream()
                .anyMatch(rule -> conditionEvaluator.evaluate(order, rule.getConditions(), rule.getConditionLogic()));
    }

    private boolean validateStockAvailability(Order order) {
        return order.getItems().stream()
                .allMatch(item -> {
                    try {
                        ProductResponse product = productService.getProduct(new ProductId(item.getProductId()));
                        return product != null && product.stockTracking() != SuiviStock.Aucun;
                    } catch (Exception e) {
                        log.error("Error validating stock for product {}: {}", item.getProductId(), e.getMessage());
                        return false;
                    }
                });
    }

    private boolean validateCustomerEligibility(Long customerId, Promotion promotion) {
        // Check customer family restrictions
        if (!promotion.getCustomerFamilies().isEmpty()) {
            try {
                CustomerDTO customer = customerService.getCustomerById(customerId);
                if (!promotion.getCustomerFamilies().stream()
                        .anyMatch(family -> family.getCustomerFamilyCode().equals(customer.getCategoryTarifId().toString()))) {
                    return false;
                }
            } catch (Exception e) {
                log.error("Error validating customer family: {}", e.getMessage());
                return false;
            }
        }

        // Check customer type restrictions
        if (promotion.getCustomerGroup() != null) {
            try {
                CustomerDTO customer = customerService.getCustomerById(customerId);
                if (!promotion.getCustomerGroup().equals(customer.getCustomerType().toString())) {
                    return false;
                }
            } catch (Exception e) {
                log.error("Error validating customer type: {}", e.getMessage());
                return false;
            }
        }

        return true;
    }

    private PromotionDTO convertToPromotionDTO(Promotion promotion) {
        return PromotionDTO.builder()
                .id(promotion.getId().intValue())
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

    private ApplyPromotionResponse createResponseFromOrder(Order order) {
        List<LineItemResultDto> lineItemResults = order.getItems().stream()
                .map(item -> new LineItemResultDto(
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())),
                        item.getDiscountAmount(),
                        item.getAppliedPromotions()
                ))
                .collect(Collectors.toList());

        BigDecimal originalTotal = lineItemResults.stream()
                .map(LineItemResultDto::getOriginalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDiscount = lineItemResults.stream()
                .map(LineItemResultDto::getTotalDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal finalTotal = originalTotal.subtract(totalDiscount);

        // Add promotion summary to response
        Map<String, BigDecimal> promotionDiscounts = new HashMap<>();
        order.getItems().forEach(item -> {
            if (item.getAppliedPromotionCode() != null) {
                promotionDiscounts.merge(
                    item.getAppliedPromotionCode(),
                    item.getDiscountAmount(),
                    BigDecimal::add
                );
            }
        });

        return ApplyPromotionResponse.builder()
                .originalTotal(originalTotal)
                .discountTotal(totalDiscount)
                .finalTotal(finalTotal)
                .lineItems(lineItemResults)
                .promotionDiscounts(promotionDiscounts)
                .build();
    }

    private PromotionBreakdownDTO createPromotionBreakdown(Order originalOrder, Order orderWithPromotion, Promotion promotion) {
        Map<Long, BigDecimal> itemDiscounts = new HashMap<>();
        
        orderWithPromotion.getItems().forEach(item -> {
            BigDecimal originalPrice = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            BigDecimal finalPrice = originalPrice.subtract(item.getDiscountAmount());
            BigDecimal discount = originalPrice.subtract(finalPrice);
            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                itemDiscounts.put(item.getProductId(), discount);
            }
        });

        return PromotionBreakdownDTO.builder()
                .promotionCode(promotion.getPromoCode())
                .promotionName(promotion.getName())
                .originalTotal(originalOrder.getTotal())
                .discountTotal(orderWithPromotion.getTotal().subtract(originalOrder.getTotal()))
                .finalTotal(orderWithPromotion.getTotal())
                .itemDiscounts(itemDiscounts)
                .build();
    }
}