package ma.foodplus.ordering.system.promos.mapper;

import ma.foodplus.ordering.system.promos.dto.ConditionDTO;
import ma.foodplus.ordering.system.promos.model.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConditionMapper {
    
    ConditionDTO toDTO(Condition condition);
    
    Condition toEntity(ConditionDTO dto);
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ConditionDTO dto, @MappingTarget Condition condition);
} 