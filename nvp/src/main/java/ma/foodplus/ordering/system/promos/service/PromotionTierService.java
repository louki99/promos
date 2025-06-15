package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.dto.PromotionTierDTO;
import java.util.List;

public interface PromotionTierService {
    
    PromotionTierDTO createTier(PromotionTierDTO tierDTO);
    
    PromotionTierDTO updateTier(PromotionTierDTO tierDTO);
    
    void deleteTier(Integer id);
    
    PromotionTierDTO getTierById(Integer id);
    
    List<PromotionTierDTO> getTiersByRuleId(Integer ruleId);
    
    PromotionTierDTO addTierToRule(Integer ruleId, PromotionTierDTO tierDTO);
    
    void removeTierFromRule(Integer ruleId, Integer tierId);
} 