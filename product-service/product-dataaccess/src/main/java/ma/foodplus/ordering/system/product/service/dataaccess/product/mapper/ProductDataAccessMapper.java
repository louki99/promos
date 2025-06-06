package ma.foodplus.ordering.system.product.service.dataaccess.product.mapper;

import ma.foodplus.ordering.system.domain.valueobject.CategoryId;
import ma.foodplus.ordering.system.domain.valueobject.Money;
import ma.foodplus.ordering.system.product.service.dataaccess.product.entity.ProductEntity;
import ma.foodplus.ordering.system.product.service.domain.entity.Product;
import ma.foodplus.ordering.system.product.service.domain.valueobject.Quantity;
import ma.foodplus.ordering.system.product.service.domain.valueobject.UnitId;
import ma.foodplus.ordering.system.dataaccess.utils.SlugUtil;
import org.springframework.stereotype.Component;

@Component
public class ProductDataAccessMapper{

    public Product productEntityToProduct(ProductEntity productEntity) {

        return Product.builder()
                .name(productEntity.getName())
                .description(productEntity.getMetaDescription())
                .quantity(new Quantity(productEntity.getQuantity()))
                .isActive(false)
                .featured(false)
                .price(new Money(productEntity.getPrice()))
                .slug(SlugUtil.toSlug(productEntity.getName()))
                .unit(new UnitId(productEntity.getUnit()))
                .category(new CategoryId(productEntity.getCategory()))
                .metaDescription(productEntity.getMetaTitle())
                .metaTitle(productEntity.getMetaTitle())
                .build();
    }

    public ProductEntity productToProductEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId().getValue())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity().amount())
                .build();
    }

}
