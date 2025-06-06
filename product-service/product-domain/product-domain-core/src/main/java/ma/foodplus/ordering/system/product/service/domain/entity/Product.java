package ma.foodplus.ordering.system.product.service.domain.entity;

import ma.foodplus.ordering.system.domain.entity.BaseEntity;
import ma.foodplus.ordering.system.domain.valueobject.CategoryId;
import ma.foodplus.ordering.system.domain.valueobject.Money;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.service.domain.exception.ProductDomainException;
import ma.foodplus.ordering.system.product.service.domain.valueobject.ProductAttributeValueId;
import ma.foodplus.ordering.system.product.service.domain.valueobject.Quantity;
import ma.foodplus.ordering.system.product.service.domain.valueobject.UnitId;

import java.util.ArrayList;
import java.util.List;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private String description;
    private Boolean isActive;
    private Boolean featured;
    private Money price;
    private UnitId unit;
    private String slug;
    private String metaTitle;
    private String metaDescription;
    private CategoryId category;
    private Quantity quantity;
    private List<ProductAttributeValue> attributes;

    private Product(Builder builder) {
        super.setId(builder.id);
        this.name = builder.name;
        this.description = builder.description;
        this.isActive = builder.isActive;
        this.featured = builder.featured;
        this.price = builder.price;
        this.unit= builder.unit;
        this.slug = builder.slug;
        this.metaTitle = builder.metaTitle;
        this.metaDescription = builder.metaDescription;
        this.category = builder.category;
        this.quantity = builder.quantity;
        this.attributes = builder.attributes;
        validateProduct();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validateProduct() {
        if (name == null || name.trim().isEmpty()) {
            throw new ProductDomainException("Product name cannot be empty");
        }
        if (price == null || price.getAmount().compareTo(Money.ZERO.getAmount()) <= 0) {
            throw new ProductDomainException("Product price must be greater than zero");
        }
        if (unit== null) {
            throw new ProductDomainException("Product unit cannot be null");
        }
        if (category == null) {
            throw new ProductDomainException("Product category cannot be null");
        }
        if (quantity == null) {
            throw new ProductDomainException("Product quantity cannot be null");
        }
    }

    public void activate() {
        if (this.isActive) {
            throw new ProductDomainException("Product is already active");
        }
        this.isActive = true;
    }

    public void deactivate() {
        if (!this.isActive) {
            throw new ProductDomainException("Product is already inactive");
        }
        this.isActive = false;
    }

    public void updatePrice(Money newPrice) {
        if (newPrice == null || newPrice.getAmount().compareTo(Money.ZERO.getAmount()) <= 0) {
            throw new ProductDomainException("New price must be greater than zero");
        }
        this.price = newPrice;
    }

    public void updateQuantity(Quantity newQuantity) {
        if (newQuantity == null) {
            throw new ProductDomainException("New quantity cannot be null");
        }
        this.quantity = newQuantity;
    }

    public void addAttribute(ProductAttributeValue attribute) {
        if (attribute == null) {
            throw new ProductDomainException("Attribute cannot be null");
        }
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        }
        this.attributes.add(attribute);
    }

    public void removeAttribute(ProductAttributeValueId attributeId) {
        if (this.attributes != null) {
            this.attributes.removeIf(attr -> attr.getId().equals(attributeId));
        }
    }

    public void updateCategory(CategoryId newCategory) {
        if (newCategory == null) {
            throw new ProductDomainException("New category cannot be null");
        }
        this.category = newCategory;
    }

    public void updateMetaInformation(String metaTitle, String metaDescription) {
        this.metaTitle = metaTitle;
        this.metaDescription = metaDescription;
    }

    public void updateSlug(String newSlug) {
        if (newSlug == null || newSlug.trim().isEmpty()) {
            throw new ProductDomainException("Slug cannot be empty");
        }
        this.slug = newSlug;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public Money getPrice() {
        return price;
    }

    public UnitId getUnit() {
        return unit;
    }

    public String getSlug() {
        return slug;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public CategoryId getCategory() {
        return category;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public List<ProductAttributeValue> getAttributes() {
        return attributes;
    }

    public static final class Builder {
        private ProductId id;
        private String name;
        private String description;
        private Boolean isActive = false;
        private Boolean featured = false;
        private Money price;
        private UnitId unit;
        private String slug;
        private String metaTitle;
        private String metaDescription;
        private CategoryId category;
        private Quantity quantity;
        private List<ProductAttributeValue> attributes = new ArrayList<>();

        private Builder() {
        }

        public Builder id(ProductId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder isActive(Boolean val) {
            isActive = val;
            return this;
        }

        public Builder featured(Boolean val) {
            featured = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder unit(UnitId val) {
            unit= val;
            return this;
        }

        public Builder slug(String val) {
            slug = val;
            return this;
        }

        public Builder metaTitle(String val) {
            metaTitle = val;
            return this;
        }

        public Builder metaDescription(String val) {
            metaDescription = val;
            return this;
        }

        public Builder category(CategoryId val) {
            category = val;
            return this;
        }

        public Builder quantity(Quantity val) {
            quantity = val;
            return this;
        }

        public Builder attributes(List<ProductAttributeValue> val) {
            attributes = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
