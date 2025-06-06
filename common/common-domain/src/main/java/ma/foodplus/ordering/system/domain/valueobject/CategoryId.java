package ma.foodplus.ordering.system.domain.valueobject;

import java.util.UUID;

public class CategoryId extends BaseId<UUID> {
    public CategoryId(UUID value) {
        super(value);
    }
}