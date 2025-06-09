package ma.foodplus.ordering.system.product.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "ma.foodplus.ordering.system.product")
@EnableJpaRepositories(basePackages = "ma.foodplus.ordering.system.product.repository")
@EnableTransactionManagement
public class ProductConfiguration {
}