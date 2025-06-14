package ma.foodplus.ordering.system.product.mapper;

import ma.foodplus.ordering.system.product.dto.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.dto.update.UpdateProductCommand;
import ma.foodplus.ordering.system.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductPersistenceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productFamily", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "unitPrice", source = "unitPrice", defaultValue = "0.0")
    @Mapping(target = "promoSkuPoints", source = "promoSkuPoints", defaultValue = "0")
    @Mapping(target = "isBulkItem", source = "isBulkItem", defaultValue = "false")
    @Mapping(target = "isPerishable", source = "isPerishable", defaultValue = "false")
    @Mapping(target = "isWholesaleOnly", source = "isWholesaleOnly", defaultValue = "false")
    @Mapping(target = "requiresApproval", source = "requiresApproval", defaultValue = "false")
    @Mapping(target = "requiresColdStorage", source = "requiresColdStorage", defaultValue = "false")
    @Mapping(target = "requiresContract", source = "requiresContract", defaultValue = "false")
    @Mapping(target = "deliverable", source = "deliverable", defaultValue = "false")
    @Mapping(target = "vendable", source = "vendable", defaultValue = "true")
    @Mapping(target = "visible", source = "visible", defaultValue = "true")
    @Mapping(target = "inactive", source = "inactive", defaultValue = "false")
    @Mapping(target = "stockTracking", source = "stockTracking", defaultValue = "Aucun")
    Product toEntity(CreateProductCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "unitPrice", source = "unitPrice", defaultValue = "0.0")
    @Mapping(target = "promoSkuPoints", source = "promoSkuPoints", defaultValue = "0")
    @Mapping(target = "isBulkItem", source = "isBulkItem", defaultValue = "false")
    @Mapping(target = "isPerishable", source = "isPerishable", defaultValue = "false")
    @Mapping(target = "isWholesaleOnly", source = "isWholesaleOnly", defaultValue = "false")
    @Mapping(target = "requiresApproval", source = "requiresApproval", defaultValue = "false")
    @Mapping(target = "requiresColdStorage", source = "requiresColdStorage", defaultValue = "false")
    @Mapping(target = "requiresContract", source = "requiresContract", defaultValue = "false")
    @Mapping(target = "deliverable", source = "deliverable", defaultValue = "false")
    @Mapping(target = "inactive", source = "inactive", defaultValue = "false")
    @Mapping(target = "vendable", source = "vendable", defaultValue = "true")
    @Mapping(target = "visible", source = "visible", defaultValue = "true")
    @Mapping(target = "stockTracking", source = "stockTracking", defaultValue = "Aucun")
    void updateEntity(@MappingTarget Product product, UpdateProductCommand command);
}