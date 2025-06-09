package ma.foodplus.ordering.system.customer.mapper;

import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import ma.foodplus.ordering.system.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    
    @Mapping(target = "categoryTarif.id", source = "categoryTarifId")
    Customer toEntity(CustomerDTO dto);

    @Mapping(target = "categoryTarifId", source = "categoryTarif.id")
    CustomerDTO toDTO(Customer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "categoryTarif.id", source = "categoryTarifId")
    void updateEntityFromDTO(CustomerDTO dto, @MappingTarget Customer entity);
} 