package ma.foodplus.ordering.system.product.mapper;

import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseMapper {

    public Product toProduct(ProductResponse response) {
        if (response == null) {
            return null;
        }

        Product product = new Product();
        product.setId(response.id());
        product.setReference(response.reference());
        product.setTitle(response.title());
        product.setDescription(response.description());
        product.setBarcode(response.barcode());
        product.setSalePrice(response.salePrice());
        product.setSaleUnit(response.saleUnit());
        product.setPriceIncludingTax(response.priceIncludingTax());
        product.setPhoto(response.photo());
        product.setDeliverable(response.deliverable());
        product.setInactive(response.inactive());
        product.setStockTracking(response.stockTracking());

        return product;
    }
} 