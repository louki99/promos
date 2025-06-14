package ma.foodplus.ordering.system.order.controller;

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
@RequestMapping("/api/v1/orders/promotions")
@RequiredArgsConstructor
@Tag(name = "Order Promotions", description = "Endpoints for managing promotions in orders for the FoodPlus B2B system.")
public class OrderPromotionController {

    private final PromotionApplicationService promotionApplicationService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate order with promotions", description = "Calculates and applies all eligible promotions to the order.")
    @ApiResponse(responseCode = "200", description = "Order calculated with promotions", content = @Content(schema = @Schema(implementation = ApplyPromotionResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid order data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ApplyPromotionResponse> calculateOrderWithPromotions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order data to apply promotions", required = true, content = @Content(schema = @Schema(implementation = ApplyPromotionRequest.class)))
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.calculatePromotions(request));
    }

    @GetMapping("/eligible")
    @Operation(summary = "Get eligible promotions for order", description = "Retrieves all promotions that are eligible for the current order.")
    @ApiResponse(responseCode = "200", description = "Eligible promotions retrieved", content = @Content(schema = @Schema(implementation = PromotionDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid order data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<PromotionDTO>> getEligiblePromotions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order data to check eligibility", required = true, content = @Content(schema = @Schema(implementation = ApplyPromotionRequest.class)))
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.getEligiblePromotions(request));
    }

    @GetMapping("/validate/{promoCode}")
    @Operation(summary = "Validate promotion code for order", description = "Checks if a specific promotion code is valid for the current order.")
    @ApiResponse(responseCode = "200", description = "Promotion code validity returned", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @ApiResponse(responseCode = "400", description = "Invalid order data or promo code", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Boolean> validatePromotionForOrder(
            @Parameter(description = "Promotion code to validate", required = true) @PathVariable String promoCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order data to check promo code", required = true, content = @Content(schema = @Schema(implementation = ApplyPromotionRequest.class)))
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.validatePromotionCode(request, promoCode));
    }

    @GetMapping("/breakdown/{promoCode}")
    @Operation(summary = "Get promotion breakdown for order", description = "Provides a detailed breakdown of how a specific promotion will be applied to the order.")
    @ApiResponse(responseCode = "200", description = "Promotion breakdown returned", content = @Content(schema = @Schema(implementation = PromotionBreakdownDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid order data or promo code", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PromotionBreakdownDTO> getPromotionBreakdownForOrder(
            @Parameter(description = "Promotion code to get breakdown for", required = true) @PathVariable String promoCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order data for breakdown", required = true, content = @Content(schema = @Schema(implementation = ApplyPromotionRequest.class)))
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.getPromotionBreakdown(request, promoCode));
    }
} 