package ma.foodplus.ordering.system.common.mapper;

import javax.annotation.processing.Generated;
import ma.foodplus.ordering.system.common.dto.CategoryTarifDTO;
import ma.foodplus.ordering.system.common.model.CategoryTarif;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-09T18:17:14+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class CategoryTarifMapperImpl implements CategoryTarifMapper {

    @Override
    public CategoryTarifDTO toDTO(CategoryTarif categoryTarif) {
        if ( categoryTarif == null ) {
            return null;
        }

        CategoryTarifDTO.CategoryTarifDTOBuilder categoryTarifDTO = CategoryTarifDTO.builder();

        categoryTarifDTO.createdAt( categoryTarif.getCreatedAt() );
        categoryTarifDTO.description( categoryTarif.getDescription() );
        categoryTarifDTO.id( categoryTarif.getId() );
        categoryTarifDTO.priceTTC( categoryTarif.getPriceTTC() );
        categoryTarifDTO.updatedAt( categoryTarif.getUpdatedAt() );

        return categoryTarifDTO.build();
    }

    @Override
    public CategoryTarif toEntity(CategoryTarifDTO categoryTarifDTO) {
        if ( categoryTarifDTO == null ) {
            return null;
        }

        CategoryTarif.CategoryTarifBuilder categoryTarif = CategoryTarif.builder();

        categoryTarif.createdAt( categoryTarifDTO.getCreatedAt() );
        categoryTarif.description( categoryTarifDTO.getDescription() );
        categoryTarif.id( categoryTarifDTO.getId() );
        categoryTarif.priceTTC( categoryTarifDTO.getPriceTTC() );
        categoryTarif.updatedAt( categoryTarifDTO.getUpdatedAt() );

        return categoryTarif.build();
    }

    @Override
    public void updateEntityFromDTO(CategoryTarifDTO dto, CategoryTarif entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDescription( dto.getDescription() );
        entity.setPriceTTC( dto.getPriceTTC() );
    }
}
