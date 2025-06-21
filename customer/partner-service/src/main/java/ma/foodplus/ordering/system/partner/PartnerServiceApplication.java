package ma.foodplus.ordering.system.partner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Partner Service Application - Advanced B2B/B2C Partner Management System
 * 
 * <p>This microservice provides comprehensive partner management capabilities including:</p>
 * <ul>
 *   <li>B2B, B2C, and Supplier partner lifecycle management</li>
 *   <li>Contract management and credit handling</li>
 *   <li>Loyalty programs and VIP management</li>
 *   <li>Advanced analytics and reporting</li>
 *   <li>Event-driven integration with Kafka</li>
 *   <li>Real-time notifications and alerts</li>
 * </ul>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class PartnerServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PartnerServiceApplication.class, args);
    }
}
