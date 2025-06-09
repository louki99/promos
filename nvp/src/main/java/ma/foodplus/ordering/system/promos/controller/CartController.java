package ma.foodplus.ordering.system.promos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.dto.*;
import ma.foodplus.ordering.system.promos.service.PromotionApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final PromotionApplicationService promotionApplicationService;

    @PostMapping("/calculate")
    public ResponseEntity<ApplyPromotionResponse> calculateCartWithPromotions(
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.calculatePromotions(request));
    }

    @GetMapping("/eligible-promotions")
    public ResponseEntity<List<PromotionDTO>> getEligiblePromotions(
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.getEligiblePromotions(request));
    }

    @GetMapping("/validate-promotion/{promoCode}")
    public ResponseEntity<Boolean> validatePromotionForCart(
            @PathVariable String promoCode,
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.validatePromotionCode(request, promoCode));
    }

    @GetMapping("/promotion-breakdown/{promoCode}")
    public ResponseEntity<PromotionBreakdownDTO> getPromotionBreakdownForCart(
            @PathVariable String promoCode,
            @Valid @RequestBody ApplyPromotionRequest request) {
        return ResponseEntity.ok(promotionApplicationService.getPromotionBreakdown(request, promoCode));
    }
}

