package ma.foodplus.ordering.system.promos.controller;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.dto.*;
import ma.foodplus.ordering.system.promos.service.PromotionApplicationService;
import ma.foodplus.ordering.system.promos.service.PromotionService;
import ma.foodplus.ordering.system.promos.service.PromotionCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import ma.foodplus.ordering.system.common.dto.ErrorResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
@Tag(name = "Promotions", description = "Endpoints for managing promotions, rules, lines, and customer families.")
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionApplicationService promotionApplicationService;
    private final PromotionCalculationService calculationService;

    @PostMapping("/apply")
    @Operation(summary = "Apply promotions to a cart/order",
               description = "Calculates and applies all eligible promotions to the given cart or order. Supports complex promotional scenarios including:\n" +
                          "- Tiered discounts based on order value\n" +
                          "- Free items based on purchase conditions\n" +
                          "- Conditional promotions with dynamic rules\n" +
                          "- Customer family-specific promotions",
               tags = {"Promotions", "Order Processing"},
               requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                   description = "Request body to apply promotions. It includes customer ID, list of order items, and an optional promotion code.",
                   required = true,
                   content = @Content(
                       mediaType = "application/json",
                       schema = @Schema(implementation = ApplyPromotionRequest.class),
                       examples = {
                           @ExampleObject(name = "10% Off Total", summary = "Applies a 10% discount to the total order.",
                               value = "{\n  \"customerId\": 123,\n  \"orderItems\": [\n    {\n      \"productId\": 101,\n      \"quantity\": 2,\n      \"unitPrice\": 50.00\n    },\n    {\n      \"productId\": 102,\n      \"quantity\": 1,\n      \"unitPrice\": 75.00\n    }\n  ],\n  \"promoCode\": \"SAVE10\"\n}"),
                           @ExampleObject(name = "Buy 2 Get 1 Free", summary = "Applies a 'Buy 2 Product A, Get 1 Product A Free' promotion.",
                               value = "{\n  \"customerId\": 124,\n  \"orderItems\": [\n    {\n      \"productId\": 201,\n      \"quantity\": 3,\n      \"unitPrice\": 30.00\n    }\n  ],\n  \"promoCode\": \"B2G1FREE_A\"\n}"),
                           @ExampleObject(name = "Tiered Discount ($15 off $150+)", summary = "Applies a '$15 off on orders over $150' promotion.",
                               value = "{\n  \"customerId\": 125,\n  \"orderItems\": [\n    {\n      \"productId\": 301,\n      \"quantity\": 1,\n      \"unitPrice\": 100.00\n    },\n    {\n      \"productId\": 302,\n      \"quantity\": 1,\n      \"unitPrice\": 60.00\n    }\n  ],\n  \"promoCode\": \"SPEND150GET15\"\n}")
                       }
                   )
               )
    )
    @ApiResponse(responseCode = "200", description = "Successfully applied promotions",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApplyPromotionResponse.class),
            examples = {
                @ExampleObject(name = "10% Off Total Response", summary = "Response for a 10% discount.",
                    value = "{\n  \"originalTotal\": 175.00,\n  \"discountTotal\": 17.50,\n  \"finalTotal\": 157.50,\n  \"lineItems\": [\n    {\n      \"productId\": 101,\n      \"productName\": \"Product Alpha\",\n      \"quantity\": 2,\n      \"originalPrice\": 100.00,\n      \"totalDiscount\": 10.00,\n      \"finalPrice\": 90.00,\n      \"appliedPromotions\": [\"SAVE10\"]\n    },\n    {\n      \"productId\": 102,\n      \"productName\": \"Product Beta\",\n      \"quantity\": 1,\n      \"originalPrice\": 75.00,\n      \"totalDiscount\": 7.50,\n      \"finalPrice\": 67.50,\n      \"appliedPromotions\": [\"SAVE10\"]\n    }\n  ],\n  \"freeItems\": [],\n  \"appliedPromotions\": [\n    {\n      \"promotionCode\": \"SAVE10\",\n      \"description\": \"10% off total order\",\n      \"discountAmount\": 17.50,\n      \"affectedItems\": [\n        { \"originalItem\": { \"productId\": 101, \"productName\": \"Product Alpha\", \"quantity\": 2, \"price\": 50.00 }, \"discountAmount\": 10.00 },\n        { \"originalItem\": { \"productId\": 102, \"productName\": \"Product Beta\", \"quantity\": 1, \"price\": 75.00 }, \"discountAmount\": 7.50 }\n      ]\n    }\n  ],\n  \"promotionDiscounts\": {\n    \"SAVE10\": 17.50\n  }\n}"),
                @ExampleObject(name = "Buy 2 Get 1 Free Response", summary = "Response for a 'Buy 2 Get 1 Free' promotion.",
                    value = "{\n  \"originalTotal\": 90.00,\n  \"discountTotal\": 30.00,\n  \"finalTotal\": 60.00,\n  \"lineItems\": [\n    {\n      \"productId\": 201,\n      \"productName\": \"Product Gamma\",\n      \"quantity\": 3,\n      \"originalPrice\": 90.00,\n      \"totalDiscount\": 30.00,\n      \"finalPrice\": 60.00,\n      \"appliedPromotions\": [\"B2G1FREE_A\"]\n    }\n  ],\n  \"freeItems\": [\n    {\n      \"productId\": 201,\n      \"productName\": \"Product Gamma\",\n      \"quantity\": 1,\n      \"reason\": \"Promotional gift from 'B2G1FREE_A' offer\"\n    }\n  ],\n  \"appliedPromotions\": [\n    {\n      \"promotionCode\": \"B2G1FREE_A\",\n      \"description\": \"Buy 2 Product Gamma, Get 1 Product Gamma Free\",\n      \"discountAmount\": 30.00,\n      \"affectedItems\": [\n         { \"originalItem\": { \"productId\": 201, \"productName\": \"Product Gamma\", \"quantity\": 3, \"price\": 30.00 }, \"discountAmount\": 30.00, \"consumedQuantity\": 1 }\n      ]\n    }\n  ],\n  \"promotionDiscounts\": {\n    \"B2G1FREE_A\": 30.00\n  }\n}"),
                @ExampleObject(name = "Tiered Discount ($15 off $150+) Response", summary = "Response for a tiered discount.",
                    value = "{\n  \"originalTotal\": 160.00,\n  \"discountTotal\": 15.00,\n  \"finalTotal\": 145.00,\n  \"lineItems\": [\n    {\n      \"productId\": 301,\n      \"productName\": \"Product Delta\",\n      \"quantity\": 1,\n      \"originalPrice\": 100.00,\n      \"totalDiscount\": 9.38,\n      \"finalPrice\": 90.62,\n      \"appliedPromotions\": [\"SPEND150GET15\"]\n    },\n    {\n      \"productId\": 302,\n      \"productName\": \"Product Epsilon\",\n      \"quantity\": 1,\n      \"originalPrice\": 60.00,\n      \"totalDiscount\": 5.62,\n      \"finalPrice\": 54.38,\n      \"appliedPromotions\": [\"SPEND150GET15\"]\n    }\n  ],\n  \"freeItems\": [],\n  \"appliedPromotions\": [\n    {\n      \"promotionCode\": \"SPEND150GET15\",\n      \"description\": \"Spend $150 or more, get $15 off\",\n      \"discountAmount\": 15.00,\n      \"affectedItems\": [\n        { \"originalItem\": { \"productId\": 301, \"productName\": \"Product Delta\", \"quantity\": 1, \"price\": 100.00 }, \"discountAmount\": 9.38 },\n        { \"originalItem\": { \"productId\": 302, \"productName\": \"Product Epsilon\", \"quantity\": 1, \"price\": 60.00 }, \"discountAmount\": 5.62 }\n      ]\n    }\n  ],\n  \"promotionDiscounts\": {\n    \"SPEND150GET15\": 15.00\n  }\n}")
            }
        )
    )
    @ApiResponse(responseCode = "400", description = "Invalid request data",
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class))) // Assuming ErrorResponse.class exists
    public ResponseEntity<ApplyPromotionResponse> applyPromotions(@RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.calculatePromotions(request));
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<PromotionDTO>> getEligiblePromotions(@RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.getEligiblePromotions(request));
    }

    @GetMapping("/validate/{promoCode}")
    public ResponseEntity<Boolean> validatePromotionCode(
            @PathVariable String promoCode,
            @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.validatePromotionCode(request, promoCode));
    }

    @GetMapping("/breakdown/{promoCode}")
    public ResponseEntity<PromotionBreakdownDTO> getPromotionBreakdown(
            @PathVariable String promoCode,
            @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.getPromotionBreakdown(request, promoCode));
    }

    @PostMapping
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody PromotionDTO promotionDTO) {
        return ResponseEntity.ok(promotionService.createPromotion(promotionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable Integer id) {
        try {
            PromotionDTO dto = promotionService.getPromotionById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PromotionDTO> getPromotionByCode(@PathVariable String code) {
        try {
            PromotionDTO dto = promotionService.getPromotionByCode(code);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionDTO> updatePromotion(
            @PathVariable Integer id,
            @RequestBody PromotionDTO promotionDTO) {
        promotionDTO.setId(id);
        return ResponseEntity.ok(promotionService.updatePromotion(promotionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Integer id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<PromotionDTO>> getActivePromotions() {
        return ResponseEntity.ok(promotionService.getActivePromotions());
    }

    @PostMapping("/{promotionId}/rules")
    public ResponseEntity<PromotionRuleDTO> addRuleToPromotion(
            @PathVariable Integer promotionId,
            @RequestBody PromotionRuleDTO ruleDTO) {
        return ResponseEntity.ok(promotionService.addRuleToPromotion(promotionId, ruleDTO));
    }

    @DeleteMapping("/{promotionId}/rules/{ruleId}")
    public ResponseEntity<Void> removeRuleFromPromotion(
            @PathVariable Integer promotionId,
            @PathVariable Integer ruleId) {
        promotionService.removeRuleFromPromotion(promotionId, ruleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{promotionId}/rules")
    public ResponseEntity<List<PromotionRuleDTO>> getPromotionRules(@PathVariable Integer promotionId) {
        return ResponseEntity.ok(promotionService.getPromotionRules(promotionId));
    }

    @PostMapping("/best-combination")
    public ResponseEntity<List<PromotionDTO>> getBestPromotionCombination(
            @RequestParam Double orderAmount,
            @RequestParam Integer itemQuantity) {
        return ResponseEntity.ok(promotionService.getBestPromotionCombination(orderAmount, itemQuantity));
    }

    // --- PromotionLine Endpoints ---
    @PostMapping("/{promotionId}/lines")
    public ResponseEntity<PromotionLineDTO> addPromotionLine(
            @PathVariable Long promotionId,
            @RequestBody PromotionLineDTO lineDTO) {
        return ResponseEntity.ok(promotionService.addPromotionLine(promotionId, lineDTO));
    }

    @DeleteMapping("/{promotionId}/lines/{lineId}")
    public ResponseEntity<Void> deletePromotionLine(
            @PathVariable Long promotionId,
            @PathVariable Long lineId) {
        promotionService.deletePromotionLine(promotionId, lineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{promotionId}/lines")
    public ResponseEntity<List<PromotionLineDTO>> getPromotionLines(@PathVariable Long promotionId) {
        return ResponseEntity.ok(promotionService.getPromotionLines(promotionId));
    }

    // --- PromotionCustomerFamily Endpoints ---
    @PostMapping("/{promotionId}/customer-families")
    public ResponseEntity<PromotionCustomerFamilyDTO> addPromotionCustomerFamily(
            @PathVariable Long promotionId,
            @RequestBody PromotionCustomerFamilyDTO familyDTO) {
        return ResponseEntity.ok(promotionService.addPromotionCustomerFamily(promotionId, familyDTO));
    }

    @DeleteMapping("/{promotionId}/customer-families/{familyId}")
    public ResponseEntity<Void> deletePromotionCustomerFamily(
            @PathVariable Long promotionId,
            @PathVariable Long familyId) {
        promotionService.deletePromotionCustomerFamily(promotionId, familyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{promotionId}/customer-families")
    public ResponseEntity<List<PromotionCustomerFamilyDTO>> getPromotionCustomerFamilies(@PathVariable Long promotionId) {
        return ResponseEntity.ok(promotionService.getPromotionCustomerFamilies(promotionId));
    }

    @PostMapping("/calculate")
    public ResponseEntity<PromotionCalculationResponse> calculatePromotions(
            @RequestBody PromotionCalculationRequest request) {
        BigDecimal totalDiscount = calculationService.calculateNestedPromotions(
            request.getPromotionId(), 
            request.getBasketItems()
        );
        
        return ResponseEntity.ok(new PromotionCalculationResponse(totalDiscount));
    }

    @PostMapping("/validate-code")
    public ResponseEntity<PromotionValidationResponse> validatePromoCode(
            @RequestBody PromotionValidationRequest request) {
        boolean isValid = promotionService.validatePromoCode(
            request.getPromoCode(),
            request.getCustomerId(),
            request.getBasketItems()
        );
        
        return ResponseEntity.ok(new PromotionValidationResponse(isValid));
    }

    @GetMapping("/{promotionId}/points")
    public ResponseEntity<Map<Long, Integer>> getProductPoints(
            @PathVariable Integer promotionId) {
        PromotionDTO promotion = promotionService.getPromotionById(promotionId);
        return ResponseEntity.ok(promotion.getProductPoints());
    }

    @PostMapping("/{promotionId}/points/calculate")
    public ResponseEntity<Integer> calculatePoints(
            @PathVariable Integer promotionId,
            @RequestBody Map<Long, Integer> basketItems) {
        int totalPoints = 0;
        for (Map.Entry<Long, Integer> entry : basketItems.entrySet()) {
            totalPoints += calculationService.calculateProductPoints(
                promotionId,
                entry.getKey(),
                entry.getValue()
            );
        }
        return ResponseEntity.ok(totalPoints);
    }
} 