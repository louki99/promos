package ma.foodplus.ordering.system.promos.mapper;

import ma.foodplus.ordering.system.promos.dto.ConditionDTO;
import ma.foodplus.ordering.system.promos.model.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ConditionMapper {
    
    @Mapping(target = "conditionType", source = "conditionType", qualifiedByName = "mapConditionTypeToDTO")
    ConditionDTO toDTO(Condition condition);
    
    @Mapping(target = "conditionType", source = "conditionType", qualifiedByName = "mapConditionTypeToEntity")
    Condition toEntity(ConditionDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conditionType", source = "conditionType", qualifiedByName = "mapConditionTypeToEntity")
    void updateEntityFromDTO(ConditionDTO dto, @MappingTarget Condition condition);

    @Named("mapConditionTypeToDTO")
    default ConditionDTO.ConditionType mapConditionTypeToDTO(Condition.ConditionType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case CART_SUBTOTAL:
                return ConditionDTO.ConditionType.CART_SUBTOTAL;
            case PRODUCT_IN_CART:
                return ConditionDTO.ConditionType.PRODUCT_IN_CART;
            case CUSTOMER_IN_GROUP:
                return ConditionDTO.ConditionType.CUSTOMER_GROUP;
            case TIME_OF_DAY:
                return ConditionDTO.ConditionType.TIME_OF_DAY;
            case DAY_OF_WEEK:
                return ConditionDTO.ConditionType.DAY_OF_WEEK;
            case CUSTOMER_LOYALTY_LEVEL:
                return ConditionDTO.ConditionType.CUSTOMER_LOYALTY_LEVEL;
            case PAYMENT_METHOD:
                return ConditionDTO.ConditionType.PAYMENT_METHOD;
            default:
                return null;
        }
    }

    @Named("mapConditionTypeToEntity")
    default Condition.ConditionType mapConditionTypeToEntity(ConditionDTO.ConditionType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case CART_SUBTOTAL:
                return Condition.ConditionType.CART_SUBTOTAL;
            case MINIMUM_AMOUNT:
                return Condition.ConditionType.CART_SUBTOTAL;
            case MINIMUM_QUANTITY:
                return Condition.ConditionType.CART_SUBTOTAL;
            case PRODUCT_IN_CART:
                return Condition.ConditionType.PRODUCT_IN_CART;
            case CATEGORY_IN_CART:
                return Condition.ConditionType.PRODUCT_IN_CART;
            case CUSTOMER_GROUP:
                return Condition.ConditionType.CUSTOMER_IN_GROUP;
            case TIME_OF_DAY:
                return Condition.ConditionType.TIME_OF_DAY;
            case DAY_OF_WEEK:
                return Condition.ConditionType.DAY_OF_WEEK;
            case CUSTOMER_LOYALTY_LEVEL:
                return Condition.ConditionType.CUSTOMER_LOYALTY_LEVEL;
            case PAYMENT_METHOD:
                return Condition.ConditionType.PAYMENT_METHOD;
            default:
                return null;
        }
    }
} 