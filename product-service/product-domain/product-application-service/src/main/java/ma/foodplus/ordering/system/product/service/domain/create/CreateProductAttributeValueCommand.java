package ma.foodplus.ordering.system.product.service.domain.create;

import ma.foodplus.ordering.system.product.service.domain.enums.DataType;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateProductAttributeValueCommand(@NotNull UUID attributeId,String valueString,BigDecimal valueNumber,
                                                 Boolean valueBoolean,LocalDate valueDate,@NotNull DataType dataType){
}