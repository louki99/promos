package ma.foodplus.ordering.system.order.mapper;

import ma.foodplus.ordering.system.order.dto.OrderDto;
import ma.foodplus.ordering.system.order.dto.OrderItemDto;
import ma.foodplus.ordering.system.order.model.Order;
import ma.foodplus.ordering.system.order.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderDto orderDto);
    
    OrderItemDto toDto(OrderItem orderItem);
    
    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItem(OrderItemDto orderItemDto);
} 