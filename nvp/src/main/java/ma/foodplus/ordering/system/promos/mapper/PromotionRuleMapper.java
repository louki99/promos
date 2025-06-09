package ma.foodplus.ordering.system.promos.mapper;

import ma.foodplus.ordering.system.promos.dto.PromotionRuleDTO;
import ma.foodplus.ordering.system.promos.model.PromotionRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ConditionMapper.class, PromotionTierMapper.class})
public interface PromotionRuleMapper {
    
    @Mapping(target = "conditions", source = "conditions")
    @Mapping(target = "tiers", source = "tiers")
    PromotionRuleDTO toDTO(PromotionRule rule);
    
    @Mapping(target = "conditions", source = "conditions")
    @Mapping(target = "tiers", source = "tiers")
    PromotionRule toEntity(PromotionRuleDTO dto);
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(PromotionRuleDTO dto, @MappingTarget PromotionRule rule);
} 