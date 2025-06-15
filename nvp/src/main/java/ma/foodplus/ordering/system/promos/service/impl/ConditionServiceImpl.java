package ma.foodplus.ordering.system.promos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.promos.dto.ConditionDTO;
import ma.foodplus.ordering.system.promos.exception.ConditionNotFoundException;
import ma.foodplus.ordering.system.promos.mapper.ConditionMapper;
import ma.foodplus.ordering.system.promos.model.Condition;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import ma.foodplus.ordering.system.promos.repository.ConditionRepository;
import ma.foodplus.ordering.system.promos.repository.PromotionRuleRepository;
import ma.foodplus.ordering.system.promos.service.ConditionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConditionServiceImpl implements ConditionService {

    private final ConditionRepository conditionRepository;
    private final PromotionRuleRepository ruleRepository;
    private final ConditionMapper conditionMapper;

    @Override
    @Transactional
    public ConditionDTO createCondition(ConditionDTO conditionDTO) {
        Condition condition = conditionMapper.toEntity(conditionDTO);
        Condition savedCondition = conditionRepository.save(condition);
        return conditionMapper.toDTO(savedCondition);
    }

    @Override
    @Transactional
    public ConditionDTO updateCondition(ConditionDTO conditionDTO) {
        Condition existingCondition = conditionRepository.findById(conditionDTO.getId().longValue())
                .orElseThrow(() -> new ConditionNotFoundException("Condition not found with id: " + conditionDTO.getId()));
        
        conditionMapper.updateEntityFromDTO(conditionDTO, existingCondition);
        Condition updatedCondition = conditionRepository.save(existingCondition);
        return conditionMapper.toDTO(updatedCondition);
    }

    @Override
    @Transactional
    public void deleteCondition(Integer id) {
        if (!conditionRepository.existsById(id.longValue())) {
            throw new ConditionNotFoundException("Condition not found with id: " + id);
        }
        conditionRepository.deleteById(id.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public ConditionDTO getConditionById(Integer id) {
        Condition condition = conditionRepository.findById(id.longValue())
                .orElseThrow(() -> new ConditionNotFoundException("Condition not found with id: " + id));
        return conditionMapper.toDTO(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConditionDTO> getConditionsByRuleId(Integer ruleId) {
        PromotionRule rule = ruleRepository.findById(ruleId.longValue())
                .orElseThrow(() -> new ConditionNotFoundException("Rule not found with id: " + ruleId));
        
        return rule.getConditions().stream()
                .map(conditionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConditionDTO addConditionToRule(Integer ruleId, ConditionDTO conditionDTO) {
        PromotionRule rule = ruleRepository.findById(ruleId.longValue())
                .orElseThrow(() -> new ConditionNotFoundException("Rule not found with id: " + ruleId));
        
        Condition condition = conditionMapper.toEntity(conditionDTO);
        condition.setRule(rule);
        Condition savedCondition = conditionRepository.save(condition);
        return conditionMapper.toDTO(savedCondition);
    }

    @Override
    @Transactional
    public void removeConditionFromRule(Integer ruleId, Integer conditionId) {
        PromotionRule rule = ruleRepository.findById(ruleId.longValue())
                .orElseThrow(() -> new ConditionNotFoundException("Rule not found with id: " + ruleId));
        
        Condition condition = conditionRepository.findById(conditionId.longValue())
                .orElseThrow(() -> new ConditionNotFoundException("Condition not found with id: " + conditionId));
        
        if (!condition.getRule().getId().equals(rule.getId())) {
            throw new ConditionNotFoundException("Condition does not belong to the specified rule");
        }
        
        conditionRepository.delete(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getConditionTypes() {
        return Arrays.stream(Condition.ConditionType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getOperators() {
        return Arrays.stream(Condition.Operator.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
} 