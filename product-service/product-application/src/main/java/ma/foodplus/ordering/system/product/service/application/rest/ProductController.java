package ma.foodplus.ordering.system.product.service.application.rest;

import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.service.domain.create.CreateProductResponse;
import ma.foodplus.ordering.system.product.service.domain.ports.input.service.ProductApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/customers", produces = "application/vnd.api.v1+json")
public class ProductController{

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService=productApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateProductResponse> createCustomer(@RequestBody CreateProductCommand
                                                                             createCustomerCommand) {
        log.info("Creating product with name: {}", createCustomerCommand.name());
        CreateProductResponse response = productApplicationService.createProduct(createCustomerCommand);
        return ResponseEntity.ok(response);
    }

}
