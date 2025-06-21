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
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerStatisticsDTO;
import ma.foodplus.ordering.system.partner.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/partner-statistics")
@RequiredArgsConstructor
@Validated
@Tag(name = "Partner Statistics & Analytics", description = "Comprehensive analytics and reporting API for partner data")
public class PartnerStatisticsController {

    private final PartnerService partnerService;

    @GetMapping("/overview")
    @Operation(summary = "Get partner overview statistics", description = "Retrieves comprehensive overview statistics for all partners")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerStatisticsDTO.class)))
    })
    public ResponseEntity<Map<String, Object>> getOverviewStatistics() {
        log.debug("Fetching partner overview statistics");
        Map<String, Object> statistics = partnerService.getPartnerStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/top-spenders")
    @Operation(summary = "Get top spending partners", description = "Retrieves top partners by total spending amount")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Top spenders retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getTopSpenders(
            @Parameter(description = "Number of top partners to retrieve", required = true) @RequestParam int limit) {
        log.debug("Fetching top {} spending partners", limit);
        List<PartnerDTO> topSpenders = partnerService.getTopPartnersBySpending(limit);
        return ResponseEntity.ok(topSpenders);
    }

    @GetMapping("/distribution/type")
    @Operation(summary = "Get partner distribution by type", description = "Retrieves distribution of partners by type (B2B/B2C)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Distribution retrieved successfully")
    })
    public ResponseEntity<Map<String, Integer>> getDistributionByType() {
        log.debug("Fetching partner distribution by type");
        Map<String, Integer> distribution = partnerService.getPartnerDistributionByType();
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/average-order-value")
    @Operation(summary = "Get average order value by partner type", description = "Retrieves average order value statistics by partner type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Average order values retrieved successfully")
    })
    public ResponseEntity<Map<String, BigDecimal>> getAverageOrderValueByType() {
        log.debug("Fetching average order value by partner type");
        Map<String, BigDecimal> averages = partnerService.getAverageOrderValueByPartnerType();
        return ResponseEntity.ok(averages);
    }

    @GetMapping("/expiring-contracts")
    @Operation(summary = "Get partners with expiring contracts", description = "Retrieves partners with contracts expiring within specified days")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners with expiring contracts retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersWithExpiringContracts(
            @Parameter(description = "Days threshold for expiring contracts", required = true) @RequestParam int daysThreshold) {
        log.debug("Fetching partners with contracts expiring in {} days", daysThreshold);
        List<PartnerDTO> partners = partnerService.getPartnerWithExpiringContracts(daysThreshold);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/overdue-payments")
    @Operation(summary = "Get partners with overdue payments", description = "Retrieves partners with outstanding payments")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners with overdue payments retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersWithOverduePayments() {
        log.debug("Fetching partners with overdue payments");
        List<PartnerDTO> partners = partnerService.getPartnerWithOverduePayments();
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/by-credit-rating/{creditRating}")
    @Operation(summary = "Get partners by credit rating", description = "Retrieves partners filtered by credit rating")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersByCreditRating(
            @Parameter(description = "Credit rating to filter by", required = true) @PathVariable String creditRating) {
        log.debug("Fetching partners by credit rating: {}", creditRating);
        List<PartnerDTO> partners = partnerService.getPartnersByCreditRating(creditRating);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/by-business-activity")
    @Operation(summary = "Get partners by business activity", description = "Retrieves partners filtered by business activity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersByBusinessActivity(
            @Parameter(description = "Business activity to filter by", required = true) @RequestParam String activity) {
        log.debug("Fetching partners by business activity: {}", activity);
        List<PartnerDTO> partners = partnerService.getPartnerByBusinessActivity(activity);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/by-annual-turnover")
    @Operation(summary = "Get partners by annual turnover range", description = "Retrieves partners within specified annual turnover range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersByAnnualTurnoverRange(
            @Parameter(description = "Minimum annual turnover", required = true) @RequestParam BigDecimal min,
            @Parameter(description = "Maximum annual turnover", required = true) @RequestParam BigDecimal max) {
        log.debug("Fetching partners by annual turnover range: {} - {}", min, max);
        List<PartnerDTO> partners = partnerService.getPartnerByAnnualTurnoverRange(min, max);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/vip")
    @Operation(summary = "Get VIP partners", description = "Retrieves all VIP partners")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "VIP partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getVipPartners() {
        log.debug("Fetching VIP partners");
        List<PartnerDTO> partners = partnerService.getVipPartners();
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active partners", description = "Retrieves all active partners")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getActivePartners() {
        log.debug("Fetching active partners");
        List<PartnerDTO> partners = partnerService.getAllActivePartners();
        return ResponseEntity.ok(partners);
    }
} 