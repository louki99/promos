package ma.foodplus.ordering.system.promos.mapper;

import ma.foodplus.ordering.system.promos.dto.PromoFamilyDTO;
import ma.foodplus.ordering.system.promos.model.PromoFamily;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromoFamilyMapper {
    
    PromoFamily toEntity(PromoFamilyDTO dto);
    
    PromoFamilyDTO toDTO(PromoFamily entity);
    
    void updateEntity(@MappingTarget PromoFamily entity, PromoFamilyDTO dto);
} 