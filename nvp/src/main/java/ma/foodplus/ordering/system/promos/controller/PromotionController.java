package ma.foodplus.ordering.system.promos.controller;

import ma.foodplus.ordering.system.promos.dto.PromotionDTO;
import ma.foodplus.ordering.system.promos.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody PromotionDTO promotionDTO) {
        return ResponseEntity.ok(promotionService.createPromotion(promotionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotion(@PathVariable Integer id) {
        return promotionService.getPromotionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{promoCode}")
    public ResponseEntity<PromotionDTO> getPromotionByCode(@PathVariable String promoCode) {
        return promotionService.getPromotionByCode(promoCode)
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
        return ResponseEntity.ok(promotionService.updatePromotion(promotionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Integer id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<PromotionDTO>> getActivePromotions() {
        return ResponseEntity.ok(promotionService.getActivePromotions());
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<PromotionDTO>> getEligiblePromotions(
            @RequestParam Double orderAmount,
            @RequestParam Integer itemQuantity) {
        return ResponseEntity.ok(promotionService.getEligiblePromotions(orderAmount, itemQuantity));
    }

    @GetMapping("/best-combination")
    public ResponseEntity<List<PromotionDTO>> getBestPromotionCombination(
            @RequestParam Double orderAmount,
            @RequestParam Integer itemQuantity) {
        return ResponseEntity.ok(promotionService.getBestPromotionCombination(orderAmount, itemQuantity));
    }
} 