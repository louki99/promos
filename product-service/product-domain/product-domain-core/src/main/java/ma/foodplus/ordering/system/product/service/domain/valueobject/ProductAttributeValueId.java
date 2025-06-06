package ma.foodplus.ordering.system.product.service.domain.valueobject;

import ma.foodplus.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class ProductAttributeValueId extends BaseId<UUID>{
    public ProductAttributeValueId(UUID value) {
        super(value);
    }
}