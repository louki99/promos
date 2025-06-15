package ma.foodplus.ordering.system.order.mapper;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.order.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "subtotal", source = "subtotal")
    @Mapping(target = "total", source = "total")
    @Mapping(target = "status", source = "status")
    OrderDto toDto(Order order);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderDto orderDto);
    
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productFamilyId", source = "productFamilyId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "skuPoints", source = "skuPoints")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "notes", source = "notes")
    OrderItemDto toDto(OrderItem orderItem);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productFamilyId", source = "productFamilyId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "skuPoints", source = "skuPoints")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "discountAmount", constant = "0")
    @Mapping(target = "taxAmount", constant = "0")
    @Mapping(target = "consumedQuantity", constant = "0")
    @Mapping(target = "appliedPromotions", ignore = true)
    @Mapping(target = "totalPrice", expression = "java(orderItemDto.getUnitPrice() != null && orderItemDto.getQuantity() != null ? orderItemDto.getUnitPrice().multiply(new java.math.BigDecimal(orderItemDto.getQuantity())) : java.math.BigDecimal.ZERO)")
    OrderItem toOrderItem(OrderItemDto orderItemDto);
} 