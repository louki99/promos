package ma.foodplus.ordering.system.common.mapper;

import ma.foodplus.ordering.system.common.dto.CategoryTarifDTO;
import ma.foodplus.ordering.system.common.model.CategoryTarif;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface CategoryTarifMapper {
    
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localDateTimeToZonedDateTime")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "localDateTimeToZonedDateTime")
    CategoryTarifDTO toDTO(CategoryTarif categoryTarif);
    
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "zonedDateTimeToLocalDateTime")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "zonedDateTimeToLocalDateTime")
    CategoryTarif toEntity(CategoryTarifDTO categoryTarifDTO);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CategoryTarifDTO dto, @MappingTarget CategoryTarif entity);

    @Named("localDateTimeToZonedDateTime")
    default ZonedDateTime localDateTimeToZonedDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.atZone(ZoneId.systemDefault()) : null;
    }

    @Named("zonedDateTimeToLocalDateTime")
    default LocalDateTime zonedDateTimeToLocalDateTime(ZonedDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDateTime() : null;
    }
} 