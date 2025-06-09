package ma.foodplus.ordering.system.promos.controller;

import jakarta.validation.Valid;
import ma.foodplus.ordering.system.promos.dto.ApplyPromotionRequest;
import ma.foodplus.ordering.system.promos.dto.ApplyPromotionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    // ... inject your service here

    // When a POST request is made to /api/cart/calculate,
    // Spring Boot will automatically try to create an `ApplyPromotionRequest`
    // object from the JSON body. This request object would contain a list of `CartItemDto`.
    @PostMapping ("/api/promotions/apply")
    public ResponseEntity<ApplyPromotionResponse> applyPromotions(
            @Valid @RequestBody ApplyPromotionRequest request) {

        // The @Valid annotation tells Spring to run the validators (@NotNull, @Positive)
        // on the incoming request object and its fields.

        // ... call your promotion engine service with the validated DTO data

        return ResponseEntity.ok(yourCalculatedResponse);
    }
}

