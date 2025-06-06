package ma.foodplus.ordering.system.product.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {
        "ma.foodplus.ordering.system.product.service.dataaccess",
        "ma.foodplus.ordering.system.dataaccess"
})
@EntityScan(basePackages = {
        "ma.foodplus.ordering.system.product.service.dataaccess",
        "ma.foodplus.ordering.system.dataaccess"
})
@SpringBootApplication(scanBasePackages = "ma.foodplus.ordering.system")
public class ProductServiceApplication{
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
