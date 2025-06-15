package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.dto.ConditionDTO;
import java.util.List;

public interface ConditionService {
    
    ConditionDTO createCondition(ConditionDTO conditionDTO);
    
    ConditionDTO updateCondition(ConditionDTO conditionDTO);
    
    void deleteCondition(Integer id);
    
    ConditionDTO getConditionById(Integer id);
    
    List<ConditionDTO> getConditionsByRuleId(Integer ruleId);
    
    ConditionDTO addConditionToRule(Integer ruleId, ConditionDTO conditionDTO);
    
    void removeConditionFromRule(Integer ruleId, Integer conditionId);
    
    List<String> getConditionTypes();
    
    List<String> getOperators();
} 