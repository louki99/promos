package ma.foodplus.ordering.system.product.service;


import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.dto.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.dto.update.UpdateProductCommand;

import java.util.List;

public interface ProductManagementUseCase{
    ProductId createProduct(CreateProductCommand command);
    ProductResponse getProduct(ProductId id);
    ProductResponse getProductByReference(String reference);
    ProductResponse getProductByBarcode(String barcode);
    List<ProductResponse> getProductsByFamilyCode(String familyCode);
    List<ProductResponse> getDeliverableProducts();
    List<ProductResponse> getActiveProducts();
    List<ProductResponse> getAllProducts();
    ProductId updateProduct(ProductId productId,UpdateProductCommand command);
    void deleteProduct(ProductId id);
    boolean existsByReference(String reference);
    boolean existsByBarcode(String barcode);
    ProductResponse getProductNameById(ProductId id);

    List<String>  getProductCategory(String entityId);

    double getProductPrice(String entityId);
}