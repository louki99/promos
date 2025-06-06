package ma.foodplus.ordering.system.product.service.domain.entity;

import lombok.Builder;
import ma.foodplus.ordering.system.domain.entity.BaseEntity;

public class Unit extends BaseEntity<ma.foodplus.ordering.system.product.service.domain.valueobject.UnitId> {
    private String name;
    private String label;

    @Builder
    public Unit(ma.foodplus.ordering.system.product.service.domain.valueobject.UnitId id,String name,String label) {
        super.setId(id);
        this.name = name;
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }
}