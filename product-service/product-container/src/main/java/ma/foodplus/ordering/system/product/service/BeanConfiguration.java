package ma.foodplus.ordering.system.product.service;

import ma.foodplus.ordering.system.product.service.domain.ProductDomainService;
import ma.foodplus.ordering.system.product.service.domain.ProductDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProductDomainService productDomainService() {
        return new ProductDomainServiceImpl();
    }
}
