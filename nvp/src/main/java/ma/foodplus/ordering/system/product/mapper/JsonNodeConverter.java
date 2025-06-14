package ma.foodplus.ordering.system.product.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class JsonNodeConverter {
    
    private final ObjectMapper objectMapper;

    public JsonNodeConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Named("jsonNodeToString")
    public String jsonNodeToString(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JsonNode to String", e);
        }
    }

    @Named("stringToJsonNode")
    public JsonNode stringToJsonNode(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Error converting String to JsonNode", e);
        }
    }
} 