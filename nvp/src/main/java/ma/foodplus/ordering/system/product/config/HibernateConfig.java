package ma.foodplus.ordering.system.product.config;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig implements TypeContributor {
    
    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        typeContributions.contributeType(new JsonBinaryType(JsonNode.class));
    }
} 