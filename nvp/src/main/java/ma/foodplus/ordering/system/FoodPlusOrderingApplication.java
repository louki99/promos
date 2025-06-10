package ma.foodplus.ordering.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "FoodPlus Ordering System API",
        version = "1.0",
        description = "Comprehensive API documentation for FoodPlus Ordering System (Promotions, Orders, Inventory, Customers, Products, etc.)",
        contact = @Contact(name = "FoodPlus Dev Team", email = "dev@foodplus.com")
    )
)
@SpringBootApplication
public class FoodPlusOrderingApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(FoodPlusOrderingApplication.class, args);
    }
}
