package ma.foodplus.ordering.system.promos.controller;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.dto.*;
import ma.foodplus.ordering.system.promos.service.PromotionApplicationService;
import ma.foodplus.ordering.system.promos.service.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionApplicationService promotionApplicationService;

    @PostMapping("/apply")
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
        return promotionService.getPromotionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PromotionDTO> getPromotionByCode(@PathVariable String code) {
        return promotionService.getPromotionByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
} 