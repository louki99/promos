package ma.foodplus.ordering.system.common.mapper;

import ma.foodplus.ordering.system.common.dto.CategoryTarifDTO;
import ma.foodplus.ordering.system.common.model.CategoryTarif;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryTarifMapper {
    CategoryTarifDTO toDTO(CategoryTarif categoryTarif);
    CategoryTarif toEntity(CategoryTarifDTO categoryTarifDTO);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CategoryTarifDTO dto, @MappingTarget CategoryTarif entity);
} 