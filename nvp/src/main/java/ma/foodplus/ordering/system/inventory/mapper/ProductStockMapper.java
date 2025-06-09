package ma.foodplus.ordering.system.inventory.mapper;

import ma.foodplus.ordering.system.inventory.dto.ProductStockDTO;
import ma.foodplus.ordering.system.inventory.dto.request.ProductStockRequest;
import ma.foodplus.ordering.system.inventory.dto.response.ProductStockResponse;
import ma.foodplus.ordering.system.inventory.model.ProductStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductStockMapper {
    
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "depot.id", source = "depotId")
    ProductStock toEntity(ProductStockDTO dto);
    
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "depotId", source = "depot.id")
    ProductStockDTO toDTO(ProductStock entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "depot.id", source = "depotId")
    void updateEntityFromDTO(ProductStockDTO dto, @MappingTarget ProductStock entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "reservedQuantity", constant = "0")
    ProductStock toEntity(ProductStockRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ProductStockRequest request, @MappingTarget ProductStock entity);

    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "depotName", ignore = true)
    @Mapping(target = "totalValue", expression = "java(entity.getQuantity().multiply(entity.getUnitCost()))")
    @Mapping(target = "availableQuantity", expression = "java(entity.getQuantity().subtract(entity.getReservedQuantity()))")
    @Mapping(target = "isLowStock", expression = "java(entity.getMinimumQuantity() != null && entity.getQuantity().compareTo(entity.getMinimumQuantity()) <= 0)")
    @Mapping(target = "isExpired", expression = "java(entity.getExpiryDate() != null && entity.getExpiryDate().isBefore(java.time.LocalDate.now()))")
    ProductStockResponse toResponse(ProductStock entity);
} 