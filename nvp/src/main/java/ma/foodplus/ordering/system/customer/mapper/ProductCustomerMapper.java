package ma.foodplus.ordering.system.customer.mapper;

import ma.foodplus.ordering.system.customer.dto.ProductCustomerDTO;
import ma.foodplus.ordering.system.customer.model.ProductCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductCustomerMapper {
    
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "customer.id", source = "customerId")
    ProductCustomer toEntity(ProductCustomerDTO dto);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "customerId", source = "customer.id")
    ProductCustomerDTO toDTO(ProductCustomer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "customer.id", source = "customerId")
    void updateEntityFromDTO(ProductCustomerDTO dto, @MappingTarget ProductCustomer entity);
} 