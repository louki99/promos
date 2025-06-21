package ma.foodplus.ordering.system.partner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerStatisticsDTO {
    
    // Basic counts
    private Long totalPartners;
    private Long activePartners;
    private Long inactivePartners;
    private Long vipPartners;
    private Long b2bPartners;
    private Long b2cPartners;
    
    // Financial statistics
    private BigDecimal totalCreditLimit;
    private BigDecimal totalOutstandingBalance;
    private BigDecimal averageCreditLimit;
    private BigDecimal averageOutstandingBalance;
    
    // Loyalty statistics
    private Long totalLoyaltyPoints;
    private BigDecimal averageLoyaltyPoints;
    private Long totalOrders;
    private BigDecimal totalSpent;
    private BigDecimal averageOrderValue;
    
    // Business statistics
    private Long partnersWithExpiringContracts;
    private Long partnersWithOverduePayments;
    private Map<String, Long> partnersByCreditRating;
    private Map<String, Long> partnersByBusinessActivity;
    
    // Top performers
    private List<PartnerDTO> topSpenders;
    private List<PartnerDTO> topLoyaltyPoints;
    private List<PartnerDTO> recentPartners;
    
    // Growth metrics
    private Long newPartnersThisMonth;
    private Long newPartnersThisYear;
    private BigDecimal revenueGrowth;
    private BigDecimal partnerGrowth;
    
    // Geographic distribution
    private Map<String, Long> partnersByCity;
    private Map<String, Long> partnersByRegion;
    private Map<String, Long> partnersByCountry;
} 