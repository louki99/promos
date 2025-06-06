package ma.foodplus.ordering.system.product.service.dataaccess.product.adapter;


import ma.foodplus.ordering.system.product.service.dataaccess.product.entity.ProductEntity;
import ma.foodplus.ordering.system.product.service.dataaccess.product.mapper.ProductDataAccessMapper;
import ma.foodplus.ordering.system.product.service.dataaccess.product.repository.ProductJpaRepository;
import ma.foodplus.ordering.system.product.service.domain.entity.Product;
import ma.foodplus.ordering.system.product.service.domain.ports.output.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductRepositoryImpl implements ProductRepository{

    private final ProductJpaRepository productJpaRepository;

    private final ProductDataAccessMapper productDataAccessMapper;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository,ProductDataAccessMapper productDataAccessMapper){
        this.productJpaRepository=productJpaRepository;
        this.productDataAccessMapper=productDataAccessMapper;
    }

    @Override
    public Product createProduct(Product product){
        ProductEntity productEntity = productDataAccessMapper.productToProductEntity(product);
        return productDataAccessMapper.productEntityToProduct(
                productJpaRepository.save(productEntity)
        );

    }

    @Override
    public Optional<Product> findProductById(UUID productId){
        return Optional.empty();
    }

    @Override
    public void deleteProduct(Product product){

    }

    @Override
    public List<Product> getAllProducts(int page,int size,String sortBy,String sortDirection){
        return List.of();
    }
}
