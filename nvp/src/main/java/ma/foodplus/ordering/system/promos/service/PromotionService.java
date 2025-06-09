package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.dto.PromotionDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionRuleDTO;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface PromotionService {
    
    // Basic CRUD operations
    PromotionDTO createPromotion(PromotionDTO promotion);
    Optional<PromotionDTO> getPromotionById(Integer id);
    Optional<PromotionDTO> getPromotionByCode(String promoCode);
    List<PromotionDTO> getAllPromotions();
    PromotionDTO updatePromotion(PromotionDTO promotion);
    void deletePromotion(Integer id);
    
    // Business logic operations
    List<PromotionDTO> getActivePromotions();
    List<PromotionDTO> getEligiblePromotions(Double orderAmount, Integer itemQuantity);
    boolean isPromotionValid(PromotionDTO promotion, ZonedDateTime date);
    boolean canPromotionsBeCombined(PromotionDTO promo1, PromotionDTO promo2);
    
    // Rule management
    PromotionRuleDTO addRuleToPromotion(Integer promotionId, PromotionRuleDTO rule);
    void removeRuleFromPromotion(Integer promotionId, Integer ruleId);
    List<PromotionRuleDTO> getPromotionRules(Integer promotionId);
    
    // Promotion calculation
    Double calculatePromotionDiscount(PromotionDTO promotion, Double orderAmount, Integer itemQuantity);
    List<PromotionDTO> getBestPromotionCombination(Double orderAmount, Integer itemQuantity);
} 