package ma.foodplus.ordering.system.product.service.domain.valueobject;

import ma.foodplus.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class UnitId extends BaseId<UUID>{
    public UnitId(UUID value) {
        super(value);
    }
}