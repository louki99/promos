package ma.foodplus.ordering.system.partner.service;

import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PartnerService {
    
    /**
     * Get a customer by ID
     * @param id the partner ID
     * @return the partner DTO
     */
    PartnerDTO getPartnerById(Long id);

    /**
     * Get all partners
     * @return list of all partners
     */
    List<PartnerDTO> getAllPartners();

    /**
     * Create a new partner
     * @param partnerDTO the partner to create
     * @return the created partner
     */
    PartnerDTO createPartner(PartnerDTO partnerDTO);

    /**
     * Update an existing customer
     * @param id the customer ID
     * @param partnerDTO the updated customer data
     * @return the updated customer
     */
    PartnerDTO updatePartner(Long id,PartnerDTO partnerDTO);

    /**
     * Delete a partner
     * @param id the partner ID
     */
    void deletePartner(Long id);

    /**
     * Get partners by product preference
     * @param productId the product ID
     * @return list of partners who prefer this product
     */
    List<PartnerDTO> getPartnerByProductPreference(Long productId);

    /**
     * Get VIP partners
     * @return list of VIP partners
     */
    List<PartnerDTO> getVipPartners();

    /**
     * Update customer loyalty points
     * @param id the customer ID
     * @param points the points to add/remove
     * @return the updated customer
     */
    PartnerDTO updateLoyaltyPoints(Long id, int points);

    PartnerDTO getPartnerByCtNum(String ctNum);
    PartnerDTO getPartnerByIce(String ice);
    List<PartnerDTO> getAllActivePartners();
    List<PartnerDTO> getPartnersByCategoryTarif(Long categoryTarifId);
    List<PartnerDTO> searchPartnersByDescription(String searchTerm);
    void activatePartner(Long id);
    void deactivatePartner(Long id);

    int getPartnerLoyaltyLevel(String entityId);

    Map<String, Object> getPartnerPurchaseHistory(String entityId);

    Set<String> getPartnerGroups(String entityId);

    /**
     * Check if a customer belongs to a specific group
     * @param partnerId the customer ID
     * @param groupId the group ID
     * @return true if the customer belongs to the group
     */
    boolean isPartnerInGroup(Long partnerId,Long groupId);

    /**
     * Get a customer's loyalty level
     * @param partnerId the customer ID
     * @return the customer's loyalty level (0-5)
     */
    int getPartnerLoyaltyLevel(Long partnerId);

    /**
     * Get a customer's total spent amount
     * @param partnerId the customer ID
     * @return the total amount spent by the customer
     */
    BigDecimal getPartnerTotalSpent(Long partnerId);

    // Search and filter operations
    Page<PartnerDTO> searchPartners(String searchTerm,Pageable pageable);
    List<PartnerDTO> getPartnersByType(PartnerDTO type);
    List<PartnerDTO> getPartnersByGroup(Long groupId);
    List<PartnerDTO> getPartnersByCreditRating(String creditRating);
    
    // B2B specific operations
    List<PartnerDTO> getPartnerWithExpiringContracts(int daysThreshold);
    List<PartnerDTO> getPartnerWithOverduePayments();
    List<PartnerDTO> getPartnerByAnnualTurnoverRange(BigDecimal min, BigDecimal max);
    List<PartnerDTO> getPartnerByBusinessActivity(String activity);
    
    // Business operations
    void updatePartnerCreditLimit(Long id, BigDecimal newLimit);
    void updatePartnerCreditScore(Long id, Integer newScore);
    void addPartnerToGroup(Long partnerId,Long groupId);
    void removePartnerFromGroup(Long partnerId,Long groupId);
    void updatePartnerLoyaltyPoints(Long id, Integer points);
    void updatePartnerVipStatus(Long id,boolean isVip);
    
    // Validation operations
    boolean validatePartnerContract(Long id);
    boolean validatePartnerCredit(Long id, BigDecimal amount);
    boolean isPartnerActive(Long id);
    
    // Statistics and reporting
    Map<String, Object> getPartnerStatistics();
    List<PartnerDTO> getTopPartnersBySpending(int limit);
    Map<String, Integer> getPartnerDistributionByType();
    Map<String, BigDecimal> getAverageOrderValueByPartnerType();

    Page<PartnerDTO> getAllPartners(Pageable pageable);

    // ========== Type-Specific Operations ==========
    
    /**
     * Create a B2B partner
     * @param b2bPartnerDTO the B2B partner data
     * @return the created B2B partner
     */
    PartnerDTO createB2BPartner(ma.foodplus.ordering.system.partner.dto.B2BPartnerDTO b2bPartnerDTO);
    
    /**
     * Create a B2C partner
     * @param b2cPartnerDTO the B2C partner data
     * @return the created B2C partner
     */
    PartnerDTO createB2CPartner(ma.foodplus.ordering.system.partner.dto.B2CPartnerDTO b2cPartnerDTO);
    
    /**
     * Update a B2B partner
     * @param id the partner ID
     * @param b2bPartnerDTO the updated B2B partner data
     * @return the updated B2B partner
     */
    PartnerDTO updateB2BPartner(Long id, ma.foodplus.ordering.system.partner.dto.B2BPartnerDTO b2bPartnerDTO);
    
    /**
     * Update a B2C partner
     * @param id the partner ID
     * @param b2cPartnerDTO the updated B2C partner data
     * @return the updated B2C partner
     */
    PartnerDTO updateB2CPartner(Long id, ma.foodplus.ordering.system.partner.dto.B2CPartnerDTO b2cPartnerDTO);
    
    /**
     * Get all B2B partners
     * @return list of B2B partners
     */
    List<PartnerDTO> getAllB2BPartners();
    
    /**
     * Get all B2C partners
     * @return list of B2C partners
     */
    List<PartnerDTO> getAllB2CPartners();
    
    /**
     * Get B2B partners with pagination
     * @param pageable pagination parameters
     * @return paginated B2B partners
     */
    Page<PartnerDTO> getB2BPartners(Pageable pageable);
    
    /**
     * Get B2C partners with pagination
     * @param pageable pagination parameters
     * @return paginated B2C partners
     */
    Page<PartnerDTO> getB2CPartners(Pageable pageable);

    // ========== B2B Contract Management ==========
    
    /**
     * Get partners with contracts expiring soon
     * @param daysThreshold days threshold for expiring contracts
     * @return list of partners with expiring contracts
     */
    List<PartnerDTO> getPartnersWithExpiringContracts(int daysThreshold);
    
    /**
     * Renew a partner's contract
     * @param partnerId the partner ID
     * @param newEndDate the new contract end date
     * @return the updated partner
     */
    PartnerDTO renewContract(Long partnerId, java.time.ZonedDateTime newEndDate);
    
    /**
     * Terminate a partner's contract
     * @param partnerId the partner ID
     * @param terminationDate the termination date
     * @param reason the termination reason
     * @return the updated partner
     */
    PartnerDTO terminateContract(Long partnerId, java.time.ZonedDateTime terminationDate, String reason);
    
    /**
     * Get contract status for a partner
     * @param partnerId the partner ID
     * @return contract status information
     */
    Map<String, Object> getContractStatus(Long partnerId);

    // ========== Credit Management ==========
    
    /**
     * Update partner's outstanding balance
     * @param partnerId the partner ID
     * @param amount the amount to add/subtract
     * @return the updated partner
     */
    PartnerDTO updateOutstandingBalance(Long partnerId, BigDecimal amount);
    
    /**
     * Get partner's credit summary
     * @param partnerId the partner ID
     * @return credit summary information
     */
    Map<String, Object> getCreditSummary(Long partnerId);
    
    /**
     * Process payment for a partner
     * @param partnerId the partner ID
     * @param amount the payment amount
     * @param paymentMethod the payment method
     * @return the updated partner
     */
    PartnerDTO processPayment(Long partnerId, BigDecimal amount, String paymentMethod);

    // ========== Validation Operations ==========
    
    /**
     * Validate if partner can place an order
     * @param partnerId the partner ID
     * @param orderAmount the order amount
     * @return validation result
     */
    Map<String, Object> validateOrderPlacement(Long partnerId, BigDecimal orderAmount);
    
    /**
     * Validate partner's eligibility for credit
     * @param partnerId the partner ID
     * @param requestedAmount the requested credit amount
     * @return validation result
     */
    Map<String, Object> validateCreditEligibility(Long partnerId, BigDecimal requestedAmount);
    
    /**
     * Get partner's validation status
     * @param partnerId the partner ID
     * @return comprehensive validation status
     */
    Map<String, Object> getValidationStatus(Long partnerId);

    // ========== Bulk Operations ==========
    
    /**
     * Bulk update partner status
     * @param partnerIds list of partner IDs
     * @param active the new active status
     * @return number of updated partners
     */
    int bulkUpdateStatus(List<Long> partnerIds, boolean active);
    
    /**
     * Bulk update credit limits
     * @param partnerIds list of partner IDs
     * @param newLimit the new credit limit
     * @return number of updated partners
     */
    int bulkUpdateCreditLimits(List<Long> partnerIds, BigDecimal newLimit);
    
    /**
     * Bulk add partners to group
     * @param partnerIds list of partner IDs
     * @param groupId the group ID
     * @return number of partners added
     */
    int bulkAddToGroup(List<Long> partnerIds, Long groupId);

    // ========== Audit and History ==========
    
    /**
     * Get partner's audit history
     * @param partnerId the partner ID
     * @return audit history
     */
    List<Map<String, Object>> getAuditHistory(Long partnerId);
    
    /**
     * Get partner's activity log
     * @param partnerId the partner ID
     * @param startDate start date for filtering
     * @param endDate end date for filtering
     * @return activity log
     */
    List<Map<String, Object>> getActivityLog(Long partnerId, java.time.ZonedDateTime startDate, java.time.ZonedDateTime endDate);

    // ========== Advanced Analytics ==========
    
    /**
     * Get partner performance metrics
     * @param partnerId the partner ID
     * @return performance metrics
     */
    Map<String, Object> getPerformanceMetrics(Long partnerId);
    
    /**
     * Get partner risk assessment
     * @param partnerId the partner ID
     * @return risk assessment
     */
    Map<String, Object> getRiskAssessment(Long partnerId);
    
    /**
     * Get partner growth trends
     * @param partnerId the partner ID
     * @param period the analysis period
     * @return growth trends
     */
    Map<String, Object> getGrowthTrends(Long partnerId, String period);
}