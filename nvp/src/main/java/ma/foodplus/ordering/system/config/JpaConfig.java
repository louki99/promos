package ma.foodplus.ordering.system.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {
    "ma.foodplus.ordering.system.common.repository",
    "ma.foodplus.ordering.system.product.repository",
    "ma.foodplus.ordering.system.customer.repository",
    "ma.foodplus.ordering.system.order.repository",
    "ma.foodplus.ordering.system.inventory.repository",
    "ma.foodplus.ordering.system.promos.repository"
})
@EntityScan(basePackages = "ma.foodplus.ordering.system")
@EnableTransactionManagement
public class JpaConfig {
} 