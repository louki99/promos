package ma.foodplus.ordering.system.promos.mapper;

import ma.foodplus.ordering.system.promos.dto.PromotionTierDTO;
import ma.foodplus.ordering.system.promos.model.PromotionTier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionTierMapper {
    
    PromotionTierDTO toDTO(PromotionTier tier);
    
    PromotionTier toEntity(PromotionTierDTO dto);
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(PromotionTierDTO dto, @MappingTarget PromotionTier tier);
} 