package ma.foodplus.ordering.system.promos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.dto.*;
import ma.foodplus.ordering.system.promos.service.PromotionApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import ma.foodplus.ordering.system.common.dto.ErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Endpoints for managing the cart and applying promotions in the FoodPlus B2B system.")
public class CartController {

    private final PromotionApplicationService promotionApplicationService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate cart with promotions", description = "Calculates and applies all eligible promotions to the cart.")
    @ApiResponse(responseCode = "200", description = "Cart calculated with promotions", content = @Content(schema = @Schema(implementation = ApplyPromotionResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid cart data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ApplyPromotionResponse> calculateCartWithPromotions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart and order data to apply promotions", required = true, content = @Content(schema = @Schema(implementation = ApplyPromotionRequest.class)))
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.calculatePromotions(request));
    }

    @GetMapping("/eligible-promotions")
    @Operation(summary = "Get eligible promotions for cart", description = "Retrieves all promotions that are eligible for the current cart.")
    @ApiResponse(responseCode = "200", description = "Eligible promotions retrieved", content = @Content(schema = @Schema(implementation = PromotionDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid cart data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<PromotionDTO>> getEligiblePromotions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart and order data to check eligibility", required = true, content = @Content(schema = @Schema(implementation = ApplyPromotionRequest.class)))
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.getEligiblePromotions(request));
    }

    @GetMapping("/validate-promotion/{promoCode}")
    @Operation(summary = "Validate promotion code for cart", description = "Checks if a specific promotion code is valid for the current cart.")
    @ApiResponse(responseCode = "200", description = "Promotion code validity returned", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @ApiResponse(responseCode = "400", description = "Invalid cart data or promo code", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Boolean> validatePromotionForCart(
            @Parameter(description = "Promotion code to validate", required = true) @PathVariable String promoCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart and order data to check promo code", required = true, content = @Content(schema = @Schema(implementation = ApplyPromotionRequest.class)))
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.validatePromotionCode(request, promoCode));
    }

    @GetMapping("/promotion-breakdown/{promoCode}")
    @Operation(summary = "Get promotion breakdown for cart", description = "Provides a detailed breakdown of how a specific promotion will be applied to the cart.")
    @ApiResponse(responseCode = "200", description = "Promotion breakdown returned", content = @Content(schema = @Schema(implementation = PromotionBreakdownDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid cart data or promo code", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PromotionBreakdownDTO> getPromotionBreakdownForCart(
            @Parameter(description = "Promotion code to get breakdown for", required = true) @PathVariable String promoCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart and order data for breakdown", required = true, content = @Content(schema = @Schema(implementation = ApplyPromotionRequest.class)))
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.getPromotionBreakdown(request, promoCode));
    }
}

