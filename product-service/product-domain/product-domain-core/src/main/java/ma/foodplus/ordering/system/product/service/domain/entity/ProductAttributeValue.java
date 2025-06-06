package ma.foodplus.ordering.system.product.service.domain.entity;

import ma.foodplus.ordering.system.domain.entity.BaseEntity;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.service.domain.enums.DataType;
import ma.foodplus.ordering.system.product.service.domain.exception.ProductDomainException;
import ma.foodplus.ordering.system.product.service.domain.valueobject.ProductAttributeValueId;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductAttributeValue extends BaseEntity<ProductAttributeValueId> {
    private ProductId productId;
    private Attribute attribute;
    private String valueString;
    private BigDecimal valueNumber;
    private Boolean valueBoolean;
    private LocalDate valueDate;

    private ProductAttributeValue(Builder builder) {
        super.setId(builder.id);
        this.productId = builder.productId;
        this.attribute = builder.attribute;
        this.valueString = builder.valueString;
        this.valueNumber = builder.valueNumber;
        this.valueBoolean = builder.valueBoolean;
        this.valueDate = builder.valueDate;
        validateAttributeValue();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validateAttributeValue() {
        if (productId == null) {
            throw new ProductDomainException("Product ID cannot be null");
        }
        if (attribute == null) {
            throw new ProductDomainException("Attribute cannot be null");
        }

        DataType dataType = attribute.getDataType();
        switch (dataType) {
            case STRING:
                if (valueString == null || valueString.trim().isEmpty()) {
                    throw new ProductDomainException("String value cannot be null or empty for STRING type attribute");
                }
                break;
            case NUMBER:
                if (valueNumber == null) {
                    throw new ProductDomainException("Number value cannot be null for NUMBER type attribute");
                }
                break;
            case BOOLEAN:
                if (valueBoolean == null) {
                    throw new ProductDomainException("Boolean value cannot be null for BOOLEAN type attribute");
                }
                break;
            case DATE:
                if (valueDate == null) {
                    throw new ProductDomainException("Date value cannot be null for DATE type attribute");
                }
                break;
            case ENUM:
                if (valueString == null || valueString.trim().isEmpty()) {
                    throw new ProductDomainException("Enum value cannot be null or empty for ENUM type attribute");
                }
                break;
            default:
                throw new ProductDomainException("Unsupported data type: " + dataType);
        }
    }

    public void updateValue(Object newValue) {
        if (newValue == null) {
            throw new ProductDomainException("New value cannot be null");
        }

        DataType dataType = attribute.getDataType();
        switch (dataType) {
            case STRING:
            case ENUM:
                if (!(newValue instanceof String)) {
                    throw new ProductDomainException("Value must be a String for STRING/ENUM type attribute");
                }
                this.valueString = (String) newValue;
                this.valueNumber = null;
                this.valueBoolean = null;
                this.valueDate = null;
                break;
            case NUMBER:
                if (!(newValue instanceof BigDecimal)) {
                    throw new ProductDomainException("Value must be a BigDecimal for NUMBER type attribute");
                }
                this.valueNumber = (BigDecimal) newValue;
                this.valueString = null;
                this.valueBoolean = null;
                this.valueDate = null;
                break;
            case BOOLEAN:
                if (!(newValue instanceof Boolean)) {
                    throw new ProductDomainException("Value must be a Boolean for BOOLEAN type attribute");
                }
                this.valueBoolean = (Boolean) newValue;
                this.valueString = null;
                this.valueNumber = null;
                this.valueDate = null;
                break;
            case DATE:
                if (!(newValue instanceof LocalDate)) {
                    throw new ProductDomainException("Value must be a LocalDate for DATE type attribute");
                }
                this.valueDate = (LocalDate) newValue;
                this.valueString = null;
                this.valueNumber = null;
                this.valueBoolean = null;
                break;
            default:
                throw new ProductDomainException("Unsupported data type: " + dataType);
        }
    }

    public Object getValue() {
        DataType dataType = attribute.getDataType();
        switch (dataType) {
            case STRING:
            case ENUM:
                return valueString;
            case NUMBER:
                return valueNumber;
            case BOOLEAN:
                return valueBoolean;
            case DATE:
                return valueDate;
            default:
                throw new ProductDomainException("Unsupported data type: " + dataType);
        }
    }

    // Getters
    public ProductId getProductId() {
        return productId;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getValueString() {
        return valueString;
    }

    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public static final class Builder {
        private ProductAttributeValueId id;
        private ProductId productId;
        private Attribute attribute;
        private String valueString;
        private BigDecimal valueNumber;
        private Boolean valueBoolean;
        private LocalDate valueDate;

        private Builder() {
        }

        public Builder id(ProductAttributeValueId val) {
            id = val;
            return this;
        }

        public Builder productId(ProductId val) {
            productId = val;
            return this;
        }

        public Builder attribute(Attribute val) {
            attribute = val;
            return this;
        }

        public Builder valueString(String val) {
            valueString = val;
            return this;
        }

        public Builder valueNumber(BigDecimal val) {
            valueNumber = val;
            return this;
        }

        public Builder valueBoolean(Boolean val) {
            valueBoolean = val;
            return this;
        }

        public Builder valueDate(LocalDate val) {
            valueDate = val;
            return this;
        }

        public ProductAttributeValue build() {
            return new ProductAttributeValue(this);
        }
    }
}

