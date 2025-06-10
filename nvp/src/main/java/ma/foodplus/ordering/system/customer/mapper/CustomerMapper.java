package ma.foodplus.ordering.system.customer.mapper;

import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import ma.foodplus.ordering.system.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerGroups", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CustomerDTO dto);
    
    CustomerDTO toDTO(Customer entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerGroups", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CustomerDTO dto, @MappingTarget Customer entity);
} 