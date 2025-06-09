package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.dto.PromotionDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionRuleDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionLineDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionCustomerFamilyDTO;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface PromotionService {
    
    /**
     * Get a promotion by ID
     * @param id the promotion ID
     * @return the promotion DTO
     */
    PromotionDTO getPromotionById(Integer id);

    /**
     * Get a promotion by code
     * @param promoCode the promotion code
     * @return the promotion DTO
     */
    PromotionDTO getPromotionByCode(String promoCode);

    /**
     * Get all promotions
     * @return list of all promotions
     */
    List<PromotionDTO> getAllPromotions();

    /**
     * Create a new promotion
     * @param promotionDTO the promotion to create
     * @return the created promotion
     */
    PromotionDTO createPromotion(PromotionDTO promotionDTO);

    /**
     * Update an existing promotion
     * @param promotionDTO the updated promotion data
     * @return the updated promotion
     */
    PromotionDTO updatePromotion(PromotionDTO promotionDTO);

    /**
     * Delete a promotion
     * @param id the promotion ID
     */
    void deletePromotion(Integer id);

    /**
     * Get active promotions
     * @return list of active promotions
     */
    List<PromotionDTO> getActivePromotions();

    /**
     * Get eligible promotions for an order
     * @param orderAmount the order amount
     * @param itemQuantity the number of items
     * @return list of eligible promotions
     */
    List<PromotionDTO> getEligiblePromotions(Double orderAmount, Integer itemQuantity);

    /**
     * Check if a promotion is valid
     * @param promotion the promotion to check
     * @param date the date to check against
     * @return true if the promotion is valid
     */
    boolean isPromotionValid(PromotionDTO promotion, ZonedDateTime date);

    /**
     * Check if promotions can be combined
     * @param promo1 the first promotion
     * @param promo2 the second promotion
     * @return true if the promotions can be combined
     */
    boolean canPromotionsBeCombined(PromotionDTO promo1, PromotionDTO promo2);

    /**
     * Check if a product has active promotions
     * @param productId the product ID
     * @return true if the product has active promotions
     */
    boolean hasActivePromotions(Long productId);

    /**
     * Get the best promotion combination for an order
     * @param orderAmount the order amount
     * @param itemQuantity the number of items
     * @return list of the best promotion combination
     */
    List<PromotionDTO> getBestPromotionCombination(Double orderAmount, Integer itemQuantity);
    
    // Rule management
    PromotionRuleDTO addRuleToPromotion(Integer promotionId, PromotionRuleDTO rule);
    void removeRuleFromPromotion(Integer promotionId, Integer ruleId);
    List<PromotionRuleDTO> getPromotionRules(Integer promotionId);
    
    // Promotion calculation
    Double calculatePromotionDiscount(PromotionDTO promotion, Double orderAmount, Integer itemQuantity);

    // PromotionLine management
    PromotionLineDTO addPromotionLine(Long promotionId, PromotionLineDTO lineDTO);
    void deletePromotionLine(Long promotionId, Long lineId);
    List<PromotionLineDTO> getPromotionLines(Long promotionId);

    // PromotionCustomerFamily management
    PromotionCustomerFamilyDTO addPromotionCustomerFamily(Long promotionId, PromotionCustomerFamilyDTO familyDTO);
    void deletePromotionCustomerFamily(Long promotionId, Long familyId);
    List<PromotionCustomerFamilyDTO> getPromotionCustomerFamilies(Long promotionId);
} 