package ma.foodplus.ordering.system.product.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductResponseMapper {

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "id", source = "id", qualifiedByName = "toProductId")
    @Mapping(target = "availabilitySchedule", source = "availabilitySchedule", qualifiedByName = "jsonNodeToString")
    @Mapping(target = "customAttributes", source = "customAttributes", qualifiedByName = "jsonNodeToString")
    public abstract ProductResponse toResponse(Product product);

    @Mapping(target = "id", source = "id", qualifiedByName = "toLong")
    @Mapping(target = "availabilitySchedule", source = "availabilitySchedule", qualifiedByName = "stringToJsonNode")
    @Mapping(target = "customAttributes", source = "customAttributes", qualifiedByName = "stringToJsonNode")
    @Mapping(target = "productFamily", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Product toProduct(ProductResponse response);

    @Named("toProductId")
    protected ProductId toProductId(Long id) {
        return id != null ? new ProductId(id) : null;
    }

    @Named("toLong")
    protected Long toLong(ProductId id) {
        return id != null ? id.getValue() : null;
    }

    @Named("jsonNodeToString")
    protected String jsonNodeToString(JsonNode jsonNode) {
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
    protected JsonNode stringToJsonNode(String jsonString) {
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