package ma.foodplus.ordering.system.product.service.domain;

import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductResponse;
import ma.foodplus.ordering.system.product.service.domain.mapper.ProductDataMapper;
import ma.foodplus.ordering.system.product.service.domain.ports.input.service.ProductApplicationService;
import ma.foodplus.ordering.system.product.service.domain.ports.output.message.publisher.ProductMessagePublisher;
import ma.foodplus.ordering.system.product.service.domain.event.product.ProductCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
class ProductApplicationServiceImpl implements ProductApplicationService {

    private final ProductCreateCommandHandler productCreateCommandHandler;
    private final ProductDataMapper productDataMapper;
    private final ProductMessagePublisher productMessagePublisher;

    public ProductApplicationServiceImpl(
            ProductCreateCommandHandler productCreateCommandHandler,
            ProductDataMapper productDataMapper,
            ProductMessagePublisher productMessagePublisher) {
        this.productCreateCommandHandler = productCreateCommandHandler;
        this.productDataMapper = productDataMapper;
        this.productMessagePublisher = productMessagePublisher;
    }

    @Override
    @Transactional
    public CreateProductResponse createProduct(CreateProductCommand createProductCommand) {
        log.info("Creating product with id: {}", createProductCommand.productId());
        ProductCreatedEvent productCreatedEvent = productCreateCommandHandler.createProduct(createProductCommand);
        productMessagePublisher.publish(productCreatedEvent);
        return productDataMapper.productToCreateProductResponse(productCreatedEvent.getProduct(),
                "Product created successfully!");
    }
}