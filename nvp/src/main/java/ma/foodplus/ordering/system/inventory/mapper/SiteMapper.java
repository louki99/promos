package ma.foodplus.ordering.system.inventory.mapper;

import ma.foodplus.ordering.system.inventory.dto.SiteDTO;
import ma.foodplus.ordering.system.inventory.model.Site;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SiteMapper {
    
    Site toEntity(SiteDTO dto);
    
    SiteDTO toDTO(Site entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "depots", ignore = true)
    void updateEntityFromDTO(SiteDTO dto, @MappingTarget Site entity);
} 