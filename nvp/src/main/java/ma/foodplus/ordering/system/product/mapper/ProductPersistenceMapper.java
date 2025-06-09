package ma.foodplus.ordering.system.product.mapper;

import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.dto.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.dto.update.UpdateProductCommand;
import ma.foodplus.ordering.system.product.enums.SuiviStock;
import ma.foodplus.ordering.system.product.model.ProductJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ProductPersistenceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "familyCode", source = "familyCode")
    @Mapping(target = "category1", source = "category1")
    @Mapping(target = "category2", source = "category2")
    @Mapping(target = "category3", source = "category3")
    @Mapping(target = "category4", source = "category4")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleUnit", source = "saleUnit")
    @Mapping(target = "priceIncludingTax", source = "priceIncludingTax")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "deliverable", source = "deliverable")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "stockTracking", source = "stockTracking")
    ProductJpaEntity createCommandToEntity(CreateProductCommand command);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "familyCode", source = "familyCode")
    @Mapping(target = "category1", source = "category1")
    @Mapping(target = "category2", source = "category2")
    @Mapping(target = "category3", source = "category3")
    @Mapping(target = "category4", source = "category4")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleUnit", source = "saleUnit")
    @Mapping(target = "priceIncludingTax", source = "priceIncludingTax")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "deliverable", source = "deliverable")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "stockTracking", source = "stockTracking")
    ProductResponse entityToResponse(ProductJpaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "familyCode", source = "familyCode")
    @Mapping(target = "category1", source = "category1")
    @Mapping(target = "category2", source = "category2")
    @Mapping(target = "category3", source = "category3")
    @Mapping(target = "category4", source = "category4")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleUnit", source = "saleUnit")
    @Mapping(target = "priceIncludingTax", source = "priceIncludingTax")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "deliverable", source = "deliverable")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "stockTracking", source = "stockTracking")
    void updateEntityFromCommand(CreateProductCommand command, @MappingTarget ProductJpaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "familyCode", source = "familyCode")
    @Mapping(target = "category1", source = "category1")
    @Mapping(target = "category2", source = "category2")
    @Mapping(target = "category3", source = "category3")
    @Mapping(target = "category4", source = "category4")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleUnit", source = "saleUnit")
    @Mapping(target = "priceIncludingTax", source = "priceIncludingTax")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "deliverable", source = "deliverable")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "stockTracking", source = "stockTracking")
    void updateEntityFromCommand(UpdateProductCommand command, @MappingTarget ProductJpaEntity entity);

    default ProductId entityToProductId(ProductJpaEntity entity) {
        return entity != null ? new ProductId(entity.getId()) : null;
    }
} 