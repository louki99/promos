package ma.foodplus.ordering.system.product.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EntityScan(basePackages = "ma.foodplus.ordering.system.product.model")
@EnableJpaAuditing
public class DatabaseConfiguration {
}