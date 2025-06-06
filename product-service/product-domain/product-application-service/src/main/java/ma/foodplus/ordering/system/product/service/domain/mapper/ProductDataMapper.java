package ma.foodplus.ordering.system.product.service.domain.mapper;

import ma.foodplus.ordering.system.domain.valueobject.CategoryId;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductResponse;
import ma.foodplus.ordering.system.product.service.domain.entity.Product;
import ma.foodplus.ordering.system.product.service.domain.valueobject.UnitId;
import org.springframework.stereotype.Component;

@Component
public class ProductDataMapper {

    public Product createProductCommandToProduct(CreateProductCommand createProductCommand) {
        return Product.builder()
                .id(new ProductId(createProductCommand.productId()))
                .name(createProductCommand.name())
                .description(createProductCommand.description())
                .isActive(createProductCommand.isActive())
                .featured(createProductCommand.featured())
                .price(createProductCommand.price())
                .unit(new UnitId(createProductCommand.unitId()))
                .slug(createProductCommand.slug())
                .metaTitle(createProductCommand.metaTitle())
                .metaDescription(createProductCommand.metaDescription())
                .category(new CategoryId(createProductCommand.categoryId()))
                .quantity(createProductCommand.quantity())
                .build();
    }


    public CreateProductResponse productToCreateProductResponse(Product product, String message) {
        return CreateProductResponse.builder()
                .productId(product.getId().getValue())
                .message(message)
                .build();
    }
}
