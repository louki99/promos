package ma.foodplus.ordering.system.partner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI partnerServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Partner Service API")
                        .description("Comprehensive API for managing business partners, customers, and B2B relationships. " +
                                   "Supports partner lifecycle management, loyalty programs, credit management, " +
                                   "contract management, and business analytics.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("FoodPlus Development Team")
                                .email("dev@foodsolutions.com")
                                .url("https://foodsolutions.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:2000").description("Development Server"),
                        new Server().url("https://api.partner.foodsolutions.com").description("Production Server")
                ));
    }
} 