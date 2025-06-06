package ma.foodplus.ordering.system.product.service.domain;

import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.service.domain.mapper.ProductDataMapper;
import ma.foodplus.ordering.system.product.service.domain.ports.output.repository.ProductRepository;
import ma.foodplus.ordering.system.product.service.domain.entity.Product;
import ma.foodplus.ordering.system.product.service.domain.event.product.ProductCreatedEvent;
import ma.foodplus.ordering.system.product.service.domain.exception.ProductDomainException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
class ProductCreateCommandHandler {

    private final ProductDomainService productDomainService;
    private final ProductRepository productRepository;
    private final ProductDataMapper productDataMapper;

    public ProductCreateCommandHandler(ProductDomainService productDomainService,
                                     ProductRepository productRepository,
                                     ProductDataMapper productDataMapper) {
        this.productDomainService = productDomainService;
        this.productRepository = productRepository;
        this.productDataMapper = productDataMapper;
    }

    @Transactional
    public ProductCreatedEvent createProduct(CreateProductCommand createProductCommand) {
        log.info("Creating product with id: {}", createProductCommand.productId());
        Product product = productDataMapper.createProductCommandToProduct(createProductCommand);
        ProductCreatedEvent productCreatedEvent = productDomainService.createProduct(product);
        Product savedProduct = productRepository.createProduct(product);
        
        if (savedProduct == null) {
            log.error("Could not save product with id: {}", createProductCommand.productId());
            throw new ProductDomainException("Could not save product with id " +
                    createProductCommand.productId());
        }
        
        log.info("Product created successfully with id: {}", createProductCommand.productId());
        return productCreatedEvent;
    }
}
