package ma.foodplus.ordering.system.product.service.domain.valueobject;

import ma.foodplus.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class AttributeId extends BaseId<UUID>{
    public AttributeId(UUID value) {
        super(value);
    }
}
