package ma.foodplus.ordering.system.partner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Global Jackson configuration for the Partner Service.
 * 
 * <p>This configuration ensures that all ObjectMappers in the application
 * properly handle Java 8 date/time types like ZonedDateTime.</p>
 * 
 * @author FoodPlus Development Team
 * @version 1.0.0
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates the primary ObjectMapper with Java 8 date/time support.
     * 
     * @return the configured ObjectMapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
} 