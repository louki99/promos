package ma.foodplus.ordering.system.promos.service.impl;

import ma.foodplus.ordering.system.promos.dto.PromotionDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionRuleDTO;
import ma.foodplus.ordering.system.promos.mapper.PromotionMapper;
import ma.foodplus.ordering.system.promos.mapper.PromotionRuleMapper;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import ma.foodplus.ordering.system.promos.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionMapper promotionMapper;

    @Autowired
    private PromotionRuleMapper ruleMapper;

    @Override
    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {
        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        promotion = promotionRepository.save(promotion);
        return promotionMapper.toDTO(promotion);
    }

    @Override
    public Optional<PromotionDTO> getPromotionById(Integer id) {
        return promotionRepository.findById(id)
                .map(promotionMapper::toDTO);
    }

    @Override
    public Optional<PromotionDTO> getPromotionByCode(String promoCode) {
        return promotionRepository.findByCode(promoCode)
                .map(promotionMapper::toDTO);
    }

    @Override
    public List<PromotionDTO> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(promotionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PromotionDTO updatePromotion(PromotionDTO promotionDTO) {
        Promotion promotion = promotionRepository.findById(promotionDTO.getId())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotionMapper.updateEntityFromDTO(promotionDTO, promotion);
        promotion = promotionRepository.save(promotion);
        return promotionMapper.toDTO(promotion);
    }

    @Override
    public void deletePromotion(Integer id) {
        promotionRepository.deleteById(id);
    }

    @Override
    public List<PromotionDTO> getActivePromotions() {
        return promotionRepository.findActivePromotions(ZonedDateTime.now())
                .stream()
                .map(promotionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionDTO> getEligiblePromotions(Double orderAmount, Integer itemQuantity) {
        List<Promotion> activePromotions = promotionRepository.findActivePromotions(ZonedDateTime.now());
        return activePromotions.stream()
                .filter(promo -> isPromotionValid(promotionMapper.toDTO(promo), ZonedDateTime.now()))
                .filter(promo -> isPromotionEligible(promo, orderAmount, itemQuantity))
                .map(promotionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isPromotionValid(PromotionDTO promotion, ZonedDateTime date) {
        return promotion.isActive() &&
               date.isAfter(promotion.getStartDate()) &&
               date.isBefore(promotion.getEndDate());
    }

    @Override
    public boolean canPromotionsBeCombined(PromotionDTO promo1, PromotionDTO promo2) {
        if (promo1.isExclusive() || promo2.isExclusive()) {
            return false;
        }
        return promo1.getCombinabilityGroup() == null ||
               promo2.getCombinabilityGroup() == null ||
               promo1.getCombinabilityGroup().equals(promo2.getCombinabilityGroup());
    }

    @Override
    public PromotionRuleDTO addRuleToPromotion(Integer promotionId, PromotionRuleDTO ruleDTO) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        PromotionRule rule = ruleMapper.toEntity(ruleDTO);
        rule.setPromotion(promotion);
        promotion.getRules().add(rule);
        promotion = promotionRepository.save(promotion);
        return ruleMapper.toDTO(rule);
    }

    @Override
    public void removeRuleFromPromotion(Integer promotionId, Integer ruleId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotion.getRules().removeIf(rule -> rule.getId().equals(ruleId));
        promotionRepository.save(promotion);
    }

    @Override
    public List<PromotionRuleDTO> getPromotionRules(Integer promotionId) {
        return promotionRepository.findById(promotionId)
                .map(Promotion::getRules)
                .orElse(Collections.emptyList())
                .stream()
                .map(ruleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Double calculatePromotionDiscount(PromotionDTO promotion, Double orderAmount, Integer itemQuantity) {
        if (!isPromotionValid(promotion, ZonedDateTime.now())) {
            return 0.0;
        }

        return promotion.getRules().stream()
                .mapToDouble(rule -> calculateRuleDiscount(rule, orderAmount, itemQuantity))
                .sum();
    }

    @Override
    public List<PromotionDTO> getBestPromotionCombination(Double orderAmount, Integer itemQuantity) {
        List<Promotion> eligiblePromotions = promotionRepository.findActivePromotions(ZonedDateTime.now());
        List<PromotionDTO> bestCombination = new ArrayList<>();
        double maxDiscount = 0.0;

        // Sort promotions by priority
        eligiblePromotions.sort(Comparator.comparingInt(Promotion::getPriority));

        // Try different combinations
        for (int i = 0; i < eligiblePromotions.size(); i++) {
            List<PromotionDTO> currentCombination = new ArrayList<>();
            double currentDiscount = 0.0;

            for (int j = i; j < eligiblePromotions.size(); j++) {
                Promotion currentPromo = eligiblePromotions.get(j);
                PromotionDTO currentPromoDTO = promotionMapper.toDTO(currentPromo);
                
                // Check if current promotion can be combined with existing ones
                boolean canCombine = currentCombination.stream()
                        .allMatch(existingPromo -> canPromotionsBeCombined(existingPromo, currentPromoDTO));

                if (canCombine) {
                    currentCombination.add(currentPromoDTO);
                    currentDiscount += calculatePromotionDiscount(currentPromoDTO, orderAmount, itemQuantity);
                }
            }

            if (currentDiscount > maxDiscount) {
                maxDiscount = currentDiscount;
                bestCombination = new ArrayList<>(currentCombination);
            }
        }

        return bestCombination;
    }

    private boolean isPromotionEligible(Promotion promotion, Double orderAmount, Integer itemQuantity) {
        return promotion.getRules().stream()
                .anyMatch(rule -> isRuleEligible(rule, orderAmount, itemQuantity));
    }

    private boolean isRuleEligible(PromotionRule rule, Double orderAmount, Integer itemQuantity) {
        // Implementation depends on specific rule conditions
        // This is a placeholder for the actual logic
        return true;
    }

    private Double calculateRuleDiscount(PromotionRuleDTO rule, Double orderAmount, Integer itemQuantity) {
        // Implementation depends on specific rule calculation method
        // This is a placeholder for the actual logic
        return 0.0;
    }
} 