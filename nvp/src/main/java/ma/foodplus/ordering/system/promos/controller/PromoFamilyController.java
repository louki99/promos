package ma.foodplus.ordering.system.promos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.dto.PromoFamilyDTO;
import ma.foodplus.ordering.system.promos.model.PromoFamily.PromoFamilyType;
import ma.foodplus.ordering.system.promos.service.PromoFamilyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promo-families")
@RequiredArgsConstructor
@Tag(name = "Promo Family", description = "Promo Family management APIs")
@Validated
public class PromoFamilyController {

    private final PromoFamilyService promoFamilyService;

    @PostMapping
    @Operation(summary = "Create a new promo family")
    public ResponseEntity<PromoFamilyDTO> createPromoFamily(@Valid @RequestBody PromoFamilyDTO promoFamilyDTO) {
        return ResponseEntity.ok(promoFamilyService.createPromoFamily(promoFamilyDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing promo family")
    public ResponseEntity<PromoFamilyDTO> updatePromoFamily(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody PromoFamilyDTO promoFamilyDTO) {
        return ResponseEntity.ok(promoFamilyService.updatePromoFamily(id, promoFamilyDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a promo family")
    public ResponseEntity<Void> deletePromoFamily(@PathVariable @NotNull Long id) {
        promoFamilyService.deletePromoFamily(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a promo family by ID")
    public ResponseEntity<PromoFamilyDTO> getPromoFamilyById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(promoFamilyService.getPromoFamilyById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get a promo family by code")
    public ResponseEntity<PromoFamilyDTO> getPromoFamilyByCode(@PathVariable @NotBlank String code) {
        return ResponseEntity.ok(promoFamilyService.getPromoFamilyByCode(code));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get promo families by type")
    public ResponseEntity<List<PromoFamilyDTO>> getPromoFamiliesByType(@PathVariable @NotNull PromoFamilyType type) {
        return ResponseEntity.ok(promoFamilyService.getPromoFamiliesByType(type));
    }

    @GetMapping("/type/{type}/active")
    @Operation(summary = "Get active promo families by type")
    public ResponseEntity<List<PromoFamilyDTO>> getActivePromoFamiliesByType(@PathVariable @NotNull PromoFamilyType type) {
        return ResponseEntity.ok(promoFamilyService.getActivePromoFamiliesByType(type));
    }

    @GetMapping("/member/{memberCode}")
    @Operation(summary = "Get promo families by member code")
    public ResponseEntity<List<PromoFamilyDTO>> getPromoFamiliesByMemberCode(@PathVariable @NotBlank String memberCode) {
        return ResponseEntity.ok(promoFamilyService.getPromoFamiliesByMemberCode(memberCode));
    }

    @PostMapping("/{familyId}/members/{memberCode}")
    @Operation(summary = "Add a member to a promo family")
    public ResponseEntity<Void> addMemberToFamily(
            @PathVariable @NotNull Long familyId,
            @PathVariable @NotBlank String memberCode) {
        promoFamilyService.addMemberToFamily(familyId, memberCode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{familyId}/members/{memberCode}")
    @Operation(summary = "Remove a member from a promo family")
    public ResponseEntity<Void> removeMemberFromFamily(
            @PathVariable @NotNull Long familyId,
            @PathVariable @NotBlank String memberCode) {
        promoFamilyService.removeMemberFromFamily(familyId, memberCode);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{familyId}/members")
    @Operation(summary = "Add multiple members to a promo family")
    public ResponseEntity<Void> addMembersToFamily(
            @PathVariable @NotNull Long familyId,
            @Valid @RequestBody List<@NotBlank String> memberCodes) {
        promoFamilyService.addMembersToFamily(familyId, memberCodes);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{familyId}/members")
    @Operation(summary = "Remove multiple members from a promo family")
    public ResponseEntity<Void> removeMembersFromFamily(
            @PathVariable @NotNull Long familyId,
            @Valid @RequestBody List<@NotBlank String> memberCodes) {
        promoFamilyService.removeMembersFromFamily(familyId, memberCodes);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{familyCode}/members/{memberCode}")
    @Operation(summary = "Check if a member is in a promo family")
    public ResponseEntity<Boolean> isMemberInFamily(
            @PathVariable @NotBlank String familyCode,
            @PathVariable @NotBlank String memberCode) {
        return ResponseEntity.ok(promoFamilyService.isMemberInFamily(familyCode, memberCode));
    }

    @GetMapping("/{familyCode}/members")
    @Operation(summary = "Get all members of a promo family")
    public ResponseEntity<List<String>> getFamilyMembers(@PathVariable @NotBlank String familyCode) {
        return ResponseEntity.ok(promoFamilyService.getFamilyMembers(familyCode));
    }
} 