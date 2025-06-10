package ma.foodplus.ordering.system.promos.service.impl;

import ma.foodplus.ordering.system.promos.dto.PromotionDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionRuleDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionLineDTO;
import ma.foodplus.ordering.system.promos.dto.PromotionCustomerFamilyDTO;
import ma.foodplus.ordering.system.promos.mapper.PromotionMapper;
import ma.foodplus.ordering.system.promos.mapper.PromotionRuleMapper;
import ma.foodplus.ordering.system.promos.model.Promotion;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.model.PromotionTier;
import ma.foodplus.ordering.system.promos.model.Condition;
import ma.foodplus.ordering.system.promos.model.PromotionLine;
import ma.foodplus.ordering.system.promos.model.PromotionCustomerFamily;
import ma.foodplus.ordering.system.promos.repository.PromotionRepository;
import ma.foodplus.ordering.system.promos.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public PromotionDTO getPromotionById(Integer id) {
        return promotionRepository.findById(id)
                .map(promotionMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    @Override
    public PromotionDTO getPromotionByCode(String promoCode) {
        return promotionRepository.findByPromoCode(promoCode)
                .map(promotionMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
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

    @Override
    public boolean hasActivePromotions(Long productId) {
        return promotionRepository.findActivePromotions(ZonedDateTime.now())
                .stream()
                .anyMatch(promo -> promo.getRules().stream()
                        .anyMatch(rule -> rule.getConditions().stream()
                                .anyMatch(condition -> condition.getConditionType() == Condition.ConditionType.PRODUCT_IN_CART &&
                                        condition.getEntityId() != null && 
                                        condition.getEntityId().equals(productId.toString()))));
    }

    private boolean isPromotionEligible(Promotion promotion, Double orderAmount, Integer itemQuantity) {
        return promotion.getRules().stream()
                .allMatch(rule -> isRuleEligible(rule, orderAmount, itemQuantity));
    }

    private boolean isRuleEligible(PromotionRule rule, Double orderAmount, Integer itemQuantity) {
        if (!rule.hasValidConditions()) {
            return false;
        }

        // Convert order amount and quantity to BigDecimal for comparison
        BigDecimal amount = BigDecimal.valueOf(orderAmount);
        BigDecimal quantity = BigDecimal.valueOf(itemQuantity);

        // Check if any tier is applicable
        if (rule.hasValidTiers()) {
            PromotionTier applicableTier = rule.findApplicableTier(
                    rule.getBreakpointType() == PromotionRule.BreakpointType.AMOUNT ? amount : quantity
            );
            if (applicableTier == null) {
                return false;
            }
        }

        // Evaluate conditions based on the rule's logic
        return rule.evaluateConditions(rule.getConditions());
    }

    private Double calculateRuleDiscount(PromotionRuleDTO ruleDTO, Double orderAmount, Integer itemQuantity) {
        PromotionRule rule = ruleMapper.toEntity(ruleDTO);
        if (!isRuleEligible(rule, orderAmount, itemQuantity)) {
            return 0.0;
        }

        BigDecimal amount = BigDecimal.valueOf(orderAmount);
        BigDecimal quantity = BigDecimal.valueOf(itemQuantity);
        BigDecimal discount = BigDecimal.ZERO;

        if (rule.hasValidTiers()) {
            PromotionTier applicableTier = rule.findApplicableTier(
                    rule.getBreakpointType() == PromotionRule.BreakpointType.AMOUNT ? amount : quantity
            );
            if (applicableTier != null) {
                if (rule.getCalculationMethod() == PromotionRule.CalculationMethod.BRACKET) {
                    // For bracket calculation, apply the tier's reward to the entire amount
                    discount = applicableTier.calculateReward(amount);
                } else {
                    // For cumulative calculation, apply the tier's reward to the amount above the threshold
                    BigDecimal amountAboveThreshold = amount.subtract(applicableTier.getMinimumThreshold());
                    if (amountAboveThreshold.compareTo(BigDecimal.ZERO) > 0) {
                        discount = applicableTier.calculateReward(amountAboveThreshold);
                    }
                }
            }
        }

        return discount.doubleValue();
    }

    // --- PromotionLine management ---
    @Override
    public PromotionLineDTO addPromotionLine(Long promotionId, PromotionLineDTO lineDTO) {
        Promotion promotion = promotionRepository.findById(promotionId.intValue())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        PromotionLine line = new PromotionLine();
        line.setPromotion(promotion);
        line.setPaidFamilyCode(lineDTO.getPaidFamilyCode());
        line.setPaidProductId(lineDTO.getPaidProductId());
        line.setFreeFamilyCode(lineDTO.getFreeFamilyCode());
        line.setFreeProductId(lineDTO.getFreeProductId());
        promotion.getPromotionLines().add(line);
        promotionRepository.save(promotion);
        lineDTO.setId(line.getId());
        lineDTO.setPromotionId(promotionId);
        return lineDTO;
    }

    @Override
    public void deletePromotionLine(Long promotionId, Long lineId) {
        Promotion promotion = promotionRepository.findById(promotionId.intValue())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotion.getPromotionLines().removeIf(line -> line.getId().equals(lineId));
        promotionRepository.save(promotion);
    }

    @Override
    public List<PromotionLineDTO> getPromotionLines(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId.intValue())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        List<PromotionLineDTO> dtos = new java.util.ArrayList<>();
        for (PromotionLine line : promotion.getPromotionLines()) {
            PromotionLineDTO dto = new PromotionLineDTO();
            dto.setId(line.getId());
            dto.setPromotionId(promotionId);
            dto.setPaidFamilyCode(line.getPaidFamilyCode());
            dto.setPaidProductId(line.getPaidProductId());
            dto.setFreeFamilyCode(line.getFreeFamilyCode());
            dto.setFreeProductId(line.getFreeProductId());
            dtos.add(dto);
        }
        return dtos;
    }

    // --- PromotionCustomerFamily management ---
    @Override
    public PromotionCustomerFamilyDTO addPromotionCustomerFamily(Long promotionId, PromotionCustomerFamilyDTO familyDTO) {
        Promotion promotion = promotionRepository.findById(promotionId.intValue())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        PromotionCustomerFamily family = new PromotionCustomerFamily();
        family.setPromotion(promotion);
        family.setCustomerFamilyCode(familyDTO.getCustomerFamilyCode());
        family.setStartDate(familyDTO.getStartDate());
        family.setEndDate(familyDTO.getEndDate());
        promotion.getCustomerFamilies().add(family);
        promotionRepository.save(promotion);
        familyDTO.setId(family.getId());
        familyDTO.setPromotionId(promotionId);
        return familyDTO;
    }

    @Override
    public void deletePromotionCustomerFamily(Long promotionId, Long familyId) {
        Promotion promotion = promotionRepository.findById(promotionId.intValue())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotion.getCustomerFamilies().removeIf(fam -> fam.getId().equals(familyId));
        promotionRepository.save(promotion);
    }

    @Override
    public List<PromotionCustomerFamilyDTO> getPromotionCustomerFamilies(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId.intValue())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        List<PromotionCustomerFamilyDTO> dtos = new java.util.ArrayList<>();
        for (PromotionCustomerFamily fam : promotion.getCustomerFamilies()) {
            PromotionCustomerFamilyDTO dto = new PromotionCustomerFamilyDTO();
            dto.setId(fam.getId());
            dto.setPromotionId(promotionId);
            dto.setCustomerFamilyCode(fam.getCustomerFamilyCode());
            dto.setStartDate(fam.getStartDate());
            dto.setEndDate(fam.getEndDate());
            dtos.add(dto);
        }
        return dtos;
    }
} 