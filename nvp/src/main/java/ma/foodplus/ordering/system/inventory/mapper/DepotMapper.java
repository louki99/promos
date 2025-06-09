package ma.foodplus.ordering.system.inventory.mapper;

import ma.foodplus.ordering.system.inventory.dto.DepotDTO;
import ma.foodplus.ordering.system.inventory.model.Depot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepotMapper {
    
    @Mapping(target = "site.id", source = "siteId")
    Depot toEntity(DepotDTO dto);
    
    @Mapping(target = "siteId", source = "site.id")
    DepotDTO toDTO(Depot entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productStocks", ignore = true)
    @Mapping(target = "site.id", source = "siteId")
    void updateEntityFromDTO(DepotDTO dto, @MappingTarget Depot entity);
} 