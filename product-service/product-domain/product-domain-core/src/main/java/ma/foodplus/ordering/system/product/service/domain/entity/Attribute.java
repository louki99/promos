package ma.foodplus.ordering.system.product.service.domain.entity;

import lombok.Builder;
import ma.foodplus.ordering.system.domain.entity.BaseEntity;
import ma.foodplus.ordering.system.product.service.domain.enums.DataType;
import ma.foodplus.ordering.system.product.service.domain.valueobject.AttributeId;

public class Attribute extends BaseEntity<AttributeId> {

    private String name;
    private String label;
    private DataType dataType;

    @Builder
    public Attribute(AttributeId id, String name, String label, DataType dataType) {
        super.setId(id);
        this.name = name;
        this.label = label;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public DataType getDataType() {
        return dataType;
    }

}
