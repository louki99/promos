package ma.foodplus.ordering.system.product.service.domain.entity;

import lombok.Builder;
import ma.foodplus.ordering.system.domain.entity.BaseEntity;
import ma.foodplus.ordering.system.domain.valueobject.CategoryId;

public class Category extends BaseEntity<CategoryId> {
    private String name;
    private String slug;

    @Builder
    public Category(CategoryId id, String name, String slug) {
        super.setId(id);
        this.name = name;
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }
}