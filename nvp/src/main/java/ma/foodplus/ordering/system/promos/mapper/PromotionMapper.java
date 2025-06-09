package ma.foodplus.ordering.system.promos.mapper;

import ma.foodplus.ordering.system.promos.dto.PromotionDTO;
import ma.foodplus.ordering.system.promos.model.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PromotionRuleMapper.class})
public interface PromotionMapper {
    
    @Mapping(target = "rules", source = "rules")
    PromotionDTO toDTO(Promotion promotion);
    
    @Mapping(target = "rules", source = "rules")
    Promotion toEntity(PromotionDTO dto);
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(PromotionDTO dto, @MappingTarget Promotion promotion);
} 