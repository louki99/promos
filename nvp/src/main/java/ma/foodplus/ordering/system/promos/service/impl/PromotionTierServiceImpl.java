package ma.foodplus.ordering.system.promos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.promos.dto.PromotionTierDTO;
import ma.foodplus.ordering.system.promos.exception.PromotionTierNotFoundException;
import ma.foodplus.ordering.system.promos.mapper.PromotionTierMapper;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.model.PromotionTier;
import ma.foodplus.ordering.system.promos.repository.PromotionRuleRepository;
import ma.foodplus.ordering.system.promos.repository.PromotionTierRepository;
import ma.foodplus.ordering.system.promos.service.PromotionTierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionTierServiceImpl implements PromotionTierService {

    private final PromotionTierRepository tierRepository;
    private final PromotionRuleRepository ruleRepository;
    private final PromotionTierMapper tierMapper;

    @Override
    @Transactional
    public PromotionTierDTO createTier(PromotionTierDTO tierDTO) {
        PromotionTier tier = tierMapper.toEntity(tierDTO);
        PromotionTier savedTier = tierRepository.save(tier);
        return tierMapper.toDTO(savedTier);
    }

    @Override
    @Transactional
    public PromotionTierDTO updateTier(PromotionTierDTO tierDTO) {
        PromotionTier existingTier = tierRepository.findById(tierDTO.getId().longValue())
                .orElseThrow(() -> new PromotionTierNotFoundException("Tier not found with id: " + tierDTO.getId()));
        
        tierMapper.updateEntityFromDTO(tierDTO, existingTier);
        PromotionTier updatedTier = tierRepository.save(existingTier);
        return tierMapper.toDTO(updatedTier);
    }

    @Override
    @Transactional
    public void deleteTier(Integer id) {
        if (!tierRepository.existsById(id.longValue())) {
            throw new PromotionTierNotFoundException("Tier not found with id: " + id);
        }
        tierRepository.deleteById(id.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionTierDTO getTierById(Integer id) {
        PromotionTier tier = tierRepository.findById(id.longValue())
                .orElseThrow(() -> new PromotionTierNotFoundException("Tier not found with id: " + id));
        return tierMapper.toDTO(tier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionTierDTO> getTiersByRuleId(Integer ruleId) {
        PromotionRule rule = ruleRepository.findById(ruleId.longValue())
                .orElseThrow(() -> new PromotionTierNotFoundException("Rule not found with id: " + ruleId));
        
        return rule.getTiers().stream()
                .map(tierMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PromotionTierDTO addTierToRule(Integer ruleId, PromotionTierDTO tierDTO) {
        PromotionRule rule = ruleRepository.findById(ruleId.longValue())
                .orElseThrow(() -> new PromotionTierNotFoundException("Rule not found with id: " + ruleId));
        
        PromotionTier tier = tierMapper.toEntity(tierDTO);
        tier.setRule(rule);
        PromotionTier savedTier = tierRepository.save(tier);
        return tierMapper.toDTO(savedTier);
    }

    @Override
    @Transactional
    public void removeTierFromRule(Integer ruleId, Integer tierId) {
        PromotionRule rule = ruleRepository.findById(ruleId.longValue())
                .orElseThrow(() -> new PromotionTierNotFoundException("Rule not found with id: " + ruleId));
        
        PromotionTier tier = tierRepository.findById(tierId.longValue())
                .orElseThrow(() -> new PromotionTierNotFoundException("Tier not found with id: " + tierId));
        
        if (!tier.getRule().getId().equals(rule.getId())) {
            throw new PromotionTierNotFoundException("Tier does not belong to the specified rule");
        }
        
        tierRepository.delete(tier);
    }
} 