package ma.foodplus.ordering.system.product.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI productOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FoodPlus Product API")
                        .description("API for managing products in FoodPlus ordering system")
                        .version("1.0"));
    }
}
