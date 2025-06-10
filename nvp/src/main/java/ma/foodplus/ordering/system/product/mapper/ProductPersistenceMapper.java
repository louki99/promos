package ma.foodplus.ordering.system.product.mapper;

import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.dto.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.dto.update.UpdateProductCommand;
import ma.foodplus.ordering.system.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    uses = {ProductId.class}
)
public interface ProductPersistenceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleUnit", source = "saleUnit")
    @Mapping(target = "priceIncludingTax", source = "priceIncludingTax")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "deliverable", source = "deliverable")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "stockTracking", source = "stockTracking")
    Product createCommandToEntity(CreateProductCommand command);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleUnit", source = "saleUnit")
    @Mapping(target = "priceIncludingTax", source = "priceIncludingTax")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "deliverable", source = "deliverable")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "stockTracking", source = "stockTracking")
    ProductResponse entityToResponse(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleUnit", source = "saleUnit")
    @Mapping(target = "priceIncludingTax", source = "priceIncludingTax")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "deliverable", source = "deliverable")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "stockTracking", source = "stockTracking")
    void updateEntityFromCommand(CreateProductCommand command, @MappingTarget Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleUnit", source = "saleUnit")
    @Mapping(target = "priceIncludingTax", source = "priceIncludingTax")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "deliverable", source = "deliverable")
    @Mapping(target = "inactive", source = "inactive")
    @Mapping(target = "stockTracking", source = "stockTracking")
    void updateEntityFromCommand(UpdateProductCommand command, @MappingTarget Product entity);

    default ProductId entityToProductId(Product entity) {
        return entity != null ? new ProductId(entity.getId()) : null;
    }
} 