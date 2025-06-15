package ma.foodplus.ordering.system.promos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.dto.RewardDTO;
import ma.foodplus.ordering.system.promos.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
@Tag(name = "Promotion Rewards", description = "APIs for managing promotion rewards")
@Validated
public class RewardController {

    private final RewardService rewardService;

    @PostMapping
    @Operation(summary = "Create a new reward")
    public ResponseEntity<RewardDTO> createReward(@Valid @RequestBody RewardDTO rewardDTO) {
        return ResponseEntity.ok(rewardService.createReward(rewardDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing reward")
    public ResponseEntity<RewardDTO> updateReward(
            @PathVariable @NotNull Integer id,
            @Valid @RequestBody RewardDTO rewardDTO) {
        rewardDTO.setId(id);
        return ResponseEntity.ok(rewardService.updateReward(rewardDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reward")
    public ResponseEntity<Void> deleteReward(@PathVariable @NotNull Integer id) {
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a reward by ID")
    public ResponseEntity<RewardDTO> getRewardById(@PathVariable @NotNull Integer id) {
        return ResponseEntity.ok(rewardService.getRewardById(id));
    }

    @GetMapping("/promotion/{promotionId}")
    @Operation(summary = "Get all rewards for a promotion")
    public ResponseEntity<List<RewardDTO>> getRewardsByPromotionId(@PathVariable @NotNull Integer promotionId) {
        return ResponseEntity.ok(rewardService.getRewardsByPromotionId(promotionId));
    }

    @PostMapping("/promotion/{promotionId}")
    @Operation(summary = "Add a reward to a promotion")
    public ResponseEntity<RewardDTO> addRewardToPromotion(
            @PathVariable @NotNull Integer promotionId,
            @Valid @RequestBody RewardDTO rewardDTO) {
        return ResponseEntity.ok(rewardService.addRewardToPromotion(promotionId, rewardDTO));
    }

    @DeleteMapping("/promotion/{promotionId}/reward/{rewardId}")
    @Operation(summary = "Remove a reward from a promotion")
    public ResponseEntity<Void> removeRewardFromPromotion(
            @PathVariable @NotNull Integer promotionId,
            @PathVariable @NotNull Integer rewardId) {
        rewardService.removeRewardFromPromotion(promotionId, rewardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types")
    @Operation(summary = "Get all available reward types")
    public ResponseEntity<List<String>> getRewardTypes() {
        return ResponseEntity.ok(rewardService.getRewardTypes());
    }
} 