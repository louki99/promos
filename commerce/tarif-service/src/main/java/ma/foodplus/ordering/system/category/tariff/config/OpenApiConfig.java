package ma.foodplus.ordering.system.category.tariff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI categoryTarifOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Category Tariff Service API")
                        .description("APIs for managing category tariffs (hotels, restaurants, cafes)")
                        .version("v1.0.0"));
    }
} 