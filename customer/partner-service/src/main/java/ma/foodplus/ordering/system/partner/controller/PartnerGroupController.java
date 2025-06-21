package ma.foodplus.ordering.system.partner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.partner.domain.PartnerGroup;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import ma.foodplus.ordering.system.partner.service.PartnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/partner-groups")
@RequiredArgsConstructor
@Validated
@Tag(name = "Partner Group Management", description = "API for managing partner groups and group memberships")
public class PartnerGroupController {

    private final PartnerService partnerService;

    @GetMapping("/{groupId}/partners")
    @Operation(summary = "Get partners in group", description = "Retrieves all partners belonging to a specific group")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersInGroup(
            @Parameter(description = "Group ID", required = true) @PathVariable Long groupId) {
        log.debug("Fetching partners in group: {}", groupId);
        List<PartnerDTO> partners = partnerService.getPartnersByGroup(groupId);
        return ResponseEntity.ok(partners);
    }

    @PostMapping("/{groupId}/partners/{partnerId}")
    @Operation(summary = "Add partner to group", description = "Adds a partner to a specific group")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner added to group successfully"),
        @ApiResponse(responseCode = "404", description = "Partner or group not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Partner is already in this group", content = @Content)
    })
    public ResponseEntity<Void> addPartnerToGroup(
            @Parameter(description = "Group ID", required = true) @PathVariable Long groupId,
            @Parameter(description = "Partner ID", required = true) @PathVariable Long partnerId) {
        log.info("Adding partner {} to group {}", partnerId, groupId);
        partnerService.addPartnerToGroup(partnerId, groupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/partners/{partnerId}")
    @Operation(summary = "Remove partner from group", description = "Removes a partner from a specific group")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner removed from group successfully"),
        @ApiResponse(responseCode = "404", description = "Partner or group not found", content = @Content)
    })
    public ResponseEntity<Void> removePartnerFromGroup(
            @Parameter(description = "Group ID", required = true) @PathVariable Long groupId,
            @Parameter(description = "Partner ID", required = true) @PathVariable Long partnerId) {
        log.info("Removing partner {} from group {}", partnerId, groupId);
        partnerService.removePartnerFromGroup(partnerId, groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}/partners/{partnerId}/check")
    @Operation(summary = "Check partner group membership", description = "Checks if a partner belongs to a specific group")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group membership checked successfully")
    })
    public ResponseEntity<Boolean> isPartnerInGroup(
            @Parameter(description = "Group ID", required = true) @PathVariable Long groupId,
            @Parameter(description = "Partner ID", required = true) @PathVariable Long partnerId) {
        log.debug("Checking if partner {} is in group {}", partnerId, groupId);
        boolean isInGroup = partnerService.isPartnerInGroup(partnerId, groupId);
        return ResponseEntity.ok(isInGroup);
    }
} 