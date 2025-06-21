package ma.foodplus.ordering.system.partner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.partner.domain.B2BPartner;
import ma.foodplus.ordering.system.partner.domain.B2CPartner;
import ma.foodplus.ordering.system.partner.domain.Partner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.dto.B2BPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.B2CPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import ma.foodplus.ordering.system.partner.exception.ErrorCode;
import ma.foodplus.ordering.system.partner.exception.PartnerException;
import ma.foodplus.ordering.system.partner.mapper.B2BPartnerMapper;
import ma.foodplus.ordering.system.partner.mapper.B2CPartnerMapper;
import ma.foodplus.ordering.system.partner.mapper.PartnerMapper;
import ma.foodplus.ordering.system.partner.repository.PartnerGroupRepository;
import ma.foodplus.ordering.system.partner.repository.PartnerRepository;
import ma.foodplus.ordering.system.partner.service.PartnerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Implementation of the Partner Service providing comprehensive partner management functionality.
 * 
 * <p>This service handles all partner-related operations including:</p>
 * <ul>
 *   <li>CRUD operations for partners</li>
 *   <li>Business logic validation</li>
 *   <li>Loyalty and credit management</li>
 *   <li>Group membership operations</li>
 *   <li>Statistics and reporting</li>
 * </ul>
 * 
 * <p>Note: Event publishing is handled by Debezium CDC, so this service focuses purely on business logic.</p>
 * 
 * @author FoodPlus Development Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerGroupRepository partnerGroupRepository;
    private final PartnerMapper partnerMapper;
    private final B2BPartnerMapper b2bPartnerMapper;
    private final B2CPartnerMapper b2cPartnerMapper;

    // ========== CRUD Operations ==========

    /**
     * Retrieves a partner by their unique identifier.
     * 
     * @param id the partner ID
     * @return the partner DTO
     * @throws PartnerException if partner is not found
     */
    @Override
    @Transactional(readOnly = true)
    public PartnerDTO getPartnerById(Long id) {
        log.debug("Fetching partner with ID: {}", id);
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        return partnerMapper.toDTO(partner);
    }

    /**
     * Retrieves all partners in the system.
     * 
     * @return list of all partners
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getAllPartners() {
        log.debug("Fetching all partners");
        List<Partner> partners = partnerRepository.findAll();
        return partnerMapper.toDTOList(partners);
    }

    /**
     * Creates a new partner with comprehensive validation.
     * 
     * @param partnerDTO the partner data to create
     * @return the created partner DTO
     * @throws PartnerException if validation fails or partner already exists
     */
    @Override
    public PartnerDTO createPartner(PartnerDTO partnerDTO) {
        log.info("Creating new partner with CT number: {}", partnerDTO.getCtNum());
        
        // Validate unique constraints
        validateUniqueConstraints(partnerDTO);
        
        // Validate business rules
        validateBusinessRules(partnerDTO);
        
        Partner partner = partnerMapper.toEntity(partnerDTO);
        
        // Set audit information
        if (partner.getAuditInfo() == null) {
            partner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        partner.getAuditInfo().setCreatedAt(ZonedDateTime.now());
        partner.getAuditInfo().setActive(true);
        
        Partner savedPartner = partnerRepository.save(partner);
        log.info("Partner created successfully with ID: {}", savedPartner.getId());
        
        return partnerMapper.toDTO(savedPartner);
    }

    /**
     * Updates an existing partner with comprehensive validation.
     * 
     * @param id the partner ID
     * @param partnerDTO the updated partner data
     * @return the updated partner DTO
     * @throws PartnerException if partner not found or validation fails
     */
    @Override
    public PartnerDTO updatePartner(Long id, PartnerDTO partnerDTO) {
        log.info("Updating partner with ID: {}", id);
        
        Partner existingPartner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        // Validate unique constraints (excluding current partner)
        validateUniqueConstraintsForUpdate(partnerDTO, id);
        
        // Validate business rules
        validateBusinessRules(partnerDTO);
        
        // Update audit information
        if (existingPartner.getAuditInfo() == null) {
            existingPartner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        existingPartner.getAuditInfo().setUpdatedAt(ZonedDateTime.now());
        existingPartner.getAuditInfo().setLastActivityDate(ZonedDateTime.now());
        
        partnerMapper.updateEntityFromDTO(partnerDTO, existingPartner);
        Partner updatedPartner = partnerRepository.save(existingPartner);
        
        log.info("Partner updated successfully with ID: {}", updatedPartner.getId());
        return partnerMapper.toDTO(updatedPartner);
    }

    /**
     * Soft deletes a partner by setting active status to false.
     * 
     * @param id the partner ID
     * @throws PartnerException if partner not found
     */
    @Override
    public void deletePartner(Long id) {
        log.info("Deleting partner with ID: {}", id);
        
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        // Soft delete - set active to false
        if (partner.getAuditInfo() == null) {
            partner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        partner.getAuditInfo().setActive(false);
        partner.getAuditInfo().setUpdatedAt(ZonedDateTime.now());
        
        partnerRepository.save(partner);
        log.info("Partner deleted successfully with ID: {}", id);
    }

    /**
     * Retrieves partners who prefer a specific product.
     * 
     * @param productId the product ID
     * @return list of partners who prefer this product
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnerByProductPreference(Long productId) {
        log.debug("Fetching partners by product preference for productId: {}", productId);
        // TODO: Implement real logic based on your domain model
        // For now, return all partners as placeholder
        List<Partner> partners = partnerRepository.findAll();
        return partnerMapper.toDTOList(partners);
    }

    // ========== Search and Filter Operations ==========

    /**
     * Retrieves a partner by their CT number.
     * 
     * @param ctNum the CT number
     * @return the partner DTO
     * @throws PartnerException if partner not found
     */
    @Override
    @Transactional(readOnly = true)
    public PartnerDTO getPartnerByCtNum(String ctNum) {
        log.debug("Fetching partner with CT number: {}", ctNum);
        Partner partner = partnerRepository.findByCtNum(ctNum)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with CT number: " + ctNum));
        return partnerMapper.toDTO(partner);
    }

    /**
     * Retrieves a partner by their ICE number.
     * 
     * @param ice the ICE number
     * @return the partner DTO
     * @throws PartnerException if partner not found
     */
    @Override
    @Transactional(readOnly = true)
    public PartnerDTO getPartnerByIce(String ice) {
        log.debug("Fetching partner with ICE: {}", ice);
        Partner partner = partnerRepository.findByIce(ice)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ICE: " + ice));
        return partnerMapper.toDTO(partner);
    }

    /**
     * Retrieves all active partners.
     * 
     * @return list of active partners
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getAllActivePartners() {
        log.debug("Fetching all active partners");
        List<Partner> partners = partnerRepository.findByActiveTrue();
        return partnerMapper.toDTOList(partners);
    }

    /**
     * Retrieves partners by category tariff ID.
     * 
     * @param categoryTarifId the category tariff ID
     * @return list of partners in the specified category
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnersByCategoryTarif(Long categoryTarifId) {
        log.debug("Fetching partners by category tariff ID: {}", categoryTarifId);
        List<Partner> partners = partnerRepository.findByCategoryTarifId(categoryTarifId);
        return partnerMapper.toDTOList(partners);
    }

    /**
     * Searches partners by description using full-text search.
     * 
     * @param searchTerm the search term
     * @return list of matching partners
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> searchPartnersByDescription(String searchTerm) {
        log.debug("Searching partners with term: {}", searchTerm);
        Page<Partner> partners = partnerRepository.searchPartners(searchTerm, Pageable.unpaged());
        return partnerMapper.toDTOList(partners.getContent());
    }

    /**
     * Activates a partner by setting active status to true.
     * 
     * @param id the partner ID
     * @throws PartnerException if partner not found
     */
    @Override
    public void activatePartner(Long id) {
        log.info("Activating partner with ID: {}", id);
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        if (partner.getAuditInfo() == null) {
            partner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        partner.getAuditInfo().setActive(true);
        partner.getAuditInfo().setUpdatedAt(ZonedDateTime.now());
        
        partnerRepository.save(partner);
        log.info("Partner activated successfully with ID: {}", id);
    }

    /**
     * Deactivates a partner by setting active status to false.
     * 
     * @param id the partner ID
     * @throws PartnerException if partner not found
     */
    @Override
    public void deactivatePartner(Long id) {
        log.info("Deactivating partner with ID: {}", id);
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        if (partner.getAuditInfo() == null) {
            partner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        partner.getAuditInfo().setActive(false);
        partner.getAuditInfo().setUpdatedAt(ZonedDateTime.now());
        
        partnerRepository.save(partner);
        log.info("Partner deactivated successfully with ID: {}", id);
    }

    // ========== Business Operations ==========

    /**
     * Retrieves all VIP partners.
     * 
     * @return list of VIP partners
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getVipPartners() {
        log.debug("Fetching VIP partners");
        List<Partner> partners = partnerRepository.findByIsVipTrue();
        return partnerMapper.toDTOList(partners);
    }

    /**
     * Updates a partner's loyalty points.
     * 
     * @param id the partner ID
     * @param points the points to add/remove
     * @return the updated partner DTO
     * @throws PartnerException if partner not found
     */
    @Override
    public PartnerDTO updateLoyaltyPoints(Long id, int points) {
        log.info("Updating loyalty points for partner ID: {} by {}", id, points);
        
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        partner.addLoyaltyPoints(points);
        Partner updatedPartner = partnerRepository.save(partner);
        
        log.info("Loyalty points updated successfully for partner ID: {}", id);
        return partnerMapper.toDTO(updatedPartner);
    }

    /**
     * Gets a partner's loyalty level based on their loyalty points.
     * 
     * @param entityId the partner entity ID
     * @return the loyalty level (0-5)
     */
    @Override
    @Transactional(readOnly = true)
    public int getPartnerLoyaltyLevel(String entityId) {
        log.debug("Getting loyalty level for partner: {}", entityId);
        // Implementation would depend on business logic for loyalty levels
        return 0; // Placeholder implementation
    }

    /**
     * Gets a partner's purchase history.
     * 
     * @param entityId the partner entity ID
     * @return purchase history data
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPartnerPurchaseHistory(String entityId) {
        log.debug("Getting purchase history for partner: {}", entityId);
        // Implementation would depend on integration with order service
        return new HashMap<>(); // Placeholder implementation
    }

    /**
     * Gets all groups a partner belongs to.
     * 
     * @param entityId the partner entity ID
     * @return set of group names
     */
    @Override
    @Transactional(readOnly = true)
    public Set<String> getPartnerGroups(String entityId) {
        log.debug("Getting groups for partner: {}", entityId);
        // Implementation would depend on group management
        return new HashSet<>(); // Placeholder implementation
    }

    /**
     * Checks if a partner belongs to a specific group.
     * 
     * @param partnerId the partner ID
     * @param groupId the group ID
     * @return true if the partner belongs to the group
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isPartnerInGroup(Long partnerId, Long groupId) {
        log.debug("Checking if partner {} is in group {}", partnerId, groupId);
        return partnerRepository.isPartnerInGroup(partnerId, groupId);
    }

    /**
     * Gets a partner's loyalty level (0-5) based on their loyalty points.
     * 
     * @param partnerId the partner ID
     * @return the loyalty level (0-5)
     * @throws PartnerException if partner not found
     */
    @Override
    @Transactional(readOnly = true)
    public int getPartnerLoyaltyLevel(Long partnerId) {
        log.debug("Getting loyalty level for partner ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        Integer loyaltyPoints = partner.getLoyaltyPoints();
        if (loyaltyPoints == null) return 0;
        
        // Simple loyalty level calculation (can be enhanced)
        if (loyaltyPoints >= 10000) return 5;
        if (loyaltyPoints >= 5000) return 4;
        if (loyaltyPoints >= 2000) return 3;
        if (loyaltyPoints >= 500) return 2;
        if (loyaltyPoints >= 100) return 1;
        return 0;
    }

    /**
     * Gets a partner's total spent amount.
     * 
     * @param partnerId the partner ID
     * @return the total amount spent
     * @throws PartnerException if partner not found
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPartnerTotalSpent(Long partnerId) {
        log.debug("Getting total spent for partner ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        return partner.getLoyaltyInfo() != null ? partner.getLoyaltyInfo().getTotalSpent() : BigDecimal.ZERO;
    }

    // ========== Search and Filter Operations ==========

    /**
     * Searches partners with pagination support.
     * 
     * @param searchTerm the search term
     * @param pageable pagination parameters
     * @return paginated list of partners
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PartnerDTO> searchPartners(String searchTerm, Pageable pageable) {
        log.debug("Searching partners with term: {} and page: {}", searchTerm, pageable.getPageNumber());
        Page<Partner> partners = partnerRepository.searchPartners(searchTerm, pageable);
        return partners.map(partnerMapper::toDTO);
    }

    /**
     * Gets partners by type.
     * 
     * @param type the partner type
     * @return list of partners of the specified type
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnersByType(PartnerDTO type) {
        log.debug("Fetching partners by type: {}", type.getPartnerType());
        List<Partner> partners = partnerRepository.findByPartnerType(type.getPartnerType());
        return partnerMapper.toDTOList(partners);
    }

    /**
     * Gets partners belonging to a specific group.
     * 
     * @param groupId the group ID
     * @return list of partners in the group
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnersByGroup(Long groupId) {
        log.debug("Fetching partners by group ID: {}", groupId);
        List<Partner> partners = partnerRepository.findByPartnerGroupsId(groupId);
        return partnerMapper.toDTOList(partners);
    }

    /**
     * Gets partners by credit rating.
     * 
     * @param creditRating the credit rating
     * @return list of partners with the specified credit rating
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnersByCreditRating(String creditRating) {
        log.debug("Fetching partners by credit rating: {}", creditRating);
        List<Partner> partners = partnerRepository.findByCreditRating(creditRating);
        return partnerMapper.toDTOList(partners);
    }

    // ========== B2B Specific Operations ==========

    /**
     * Gets partners with contracts expiring within the specified number of days.
     * 
     * @param daysThreshold the number of days threshold
     * @return list of partners with expiring contracts
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnerWithExpiringContracts(int daysThreshold) {
        log.debug("Fetching partners with contracts expiring in {} days", daysThreshold);
        ZonedDateTime thresholdDate = ZonedDateTime.now().plusDays(daysThreshold);
        List<Partner> partners = partnerRepository.findByContractEndDateBefore(thresholdDate);
        return partners.stream()
                .filter(partner -> partner instanceof B2BPartner)
                .map(partner -> b2bPartnerMapper.toPartnerDTO((B2BPartner) partner))
                .toList();
    }

    /**
     * Gets partners with overdue payments.
     * 
     * @return list of partners with outstanding balances
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnerWithOverduePayments() {
        log.debug("Fetching partners with overdue payments");
        List<Partner> partners = partnerRepository.findByOutstandingBalanceGreaterThan(BigDecimal.ZERO);
        return partnerMapper.toDTOList(partners);
    }

    /**
     * Gets partners by annual turnover range.
     * 
     * @param min minimum annual turnover
     * @param max maximum annual turnover
     * @return list of partners within the turnover range
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnerByAnnualTurnoverRange(BigDecimal min, BigDecimal max) {
        log.debug("Fetching partners by annual turnover range: {} - {}", min, max);
        List<Partner> partners = partnerRepository.findByAnnualTurnoverBetween(min, max);
        return partnerMapper.toDTOList(partners);
    }

    /**
     * Gets partners by business activity.
     * 
     * @param activity the business activity
     * @return list of partners with the specified business activity
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnerByBusinessActivity(String activity) {
        log.debug("Fetching partners by business activity: {}", activity);
        List<Partner> partners = partnerRepository.findByBusinessActivityContainingIgnoreCase(activity);
        return partnerMapper.toDTOList(partners);
    }

    // ========== Business Operations ==========

    /**
     * Updates a partner's credit limit.
     * 
     * @param id the partner ID
     * @param newLimit the new credit limit
     * @throws PartnerException if partner not found or limit is negative
     */
    @Override
    public void updatePartnerCreditLimit(Long id, BigDecimal newLimit) {
        log.info("Updating credit limit for partner ID: {} to {}", id, newLimit);
        
        if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new PartnerException(ErrorCode.VALIDATION_CREDIT_LIMIT_NEGATIVE, "Credit limit cannot be negative");
        }
        
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        if (partner.getCreditInfo() == null) {
            partner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        }
        partner.getCreditInfo().setCreditLimit(newLimit);
        
        partnerRepository.save(partner);
        log.info("Credit limit updated successfully for partner ID: {}", id);
    }

    /**
     * Updates a partner's credit score.
     * 
     * @param id the partner ID
     * @param newScore the new credit score
     * @throws PartnerException if partner not found
     */
    @Override
    public void updatePartnerCreditScore(Long id, Integer newScore) {
        log.info("Updating credit score for partner ID: {} to {}", id, newScore);
        
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        if (partner.getCreditInfo() == null) {
            partner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        }
        partner.getCreditInfo().setCreditScore(newScore);
        
        partnerRepository.save(partner);
        log.info("Credit score updated successfully for partner ID: {}", id);
    }

    /**
     * Adds a partner to a group.
     * 
     * @param partnerId the partner ID
     * @param groupId the group ID
     * @throws PartnerException if partner or group not found, or partner already in group
     */
    @Override
    public void addPartnerToGroup(Long partnerId, Long groupId) {
        log.info("Adding partner {} to group {}", partnerId, groupId);
        
        // Validate partner exists
        if (!partnerRepository.existsById(partnerId)) {
            throw new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId);
        }
        
        // Validate group exists
        if (!partnerGroupRepository.existsById(groupId)) {
            throw new PartnerException(ErrorCode.PARTNER_GROUP_NOT_FOUND, "Group not found with ID: " + groupId);
        }
        
        // Check if already in group
        if (partnerRepository.isPartnerInGroup(partnerId, groupId)) {
            throw new PartnerException(ErrorCode.PARTNER_ALREADY_IN_GROUP, "Partner is already in this group");
        }
        
        partnerRepository.addPartnerToGroup(partnerId, groupId);
        log.info("Partner {} added to group {} successfully", partnerId, groupId);
    }

    /**
     * Removes a partner from a group.
     * 
     * @param partnerId the partner ID
     * @param groupId the group ID
     * @throws PartnerException if partner not in group
     */
    @Override
    public void removePartnerFromGroup(Long partnerId, Long groupId) {
        log.info("Removing partner {} from group {}", partnerId, groupId);
        
        // Check if partner is in group
        if (!partnerRepository.isPartnerInGroup(partnerId, groupId)) {
            throw new PartnerException(ErrorCode.PARTNER_NOT_IN_GROUP, "Partner is not in this group");
        }
        
        partnerRepository.removePartnerFromGroup(partnerId, groupId);
        log.info("Partner {} removed from group {} successfully", partnerId, groupId);
    }

    /**
     * Updates a partner's loyalty points to a specific value.
     * 
     * @param id the partner ID
     * @param points the new loyalty points value
     * @throws PartnerException if partner not found or points are negative
     */
    @Override
    public void updatePartnerLoyaltyPoints(Long id, Integer points) {
        log.info("Updating loyalty points for partner ID: {} to {}", id, points);
        
        if (points < 0) {
            throw new PartnerException(ErrorCode.VALIDATION_LOYALTY_POINTS_NEGATIVE, "Loyalty points cannot be negative");
        }
        
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        if (partner.getLoyaltyInfo() == null) {
            partner.setLoyaltyInfo(new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo());
        }
        partner.getLoyaltyInfo().setLoyaltyPoints(points);
        
        partnerRepository.save(partner);
        log.info("Loyalty points updated successfully for partner ID: {}", id);
    }

    /**
     * Updates a partner's VIP status.
     * 
     * @param id the partner ID
     * @param isVip the new VIP status
     * @throws PartnerException if partner not found
     */
    @Override
    public void updatePartnerVipStatus(Long id, boolean isVip) {
        log.info("Updating VIP status for partner ID: {} to {}", id, isVip);
        
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        if (partner.getLoyaltyInfo() == null) {
            partner.setLoyaltyInfo(new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo());
        }
        partner.getLoyaltyInfo().setVip(isVip);
        
        partnerRepository.save(partner);
        log.info("VIP status updated successfully for partner ID: {}", id);
    }

    // ========== Validation Operations ==========

    /**
     * Validates if a partner has a valid contract.
     * 
     * @param id the partner ID
     * @return true if partner has a valid contract
     * @throws PartnerException if partner not found
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validatePartnerContract(Long id) {
        log.debug("Validating contract for partner ID: {}", id);
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        // For B2B partners, check if they have valid contract
        if (partner instanceof ma.foodplus.ordering.system.partner.domain.B2BPartner) {
            return ((ma.foodplus.ordering.system.partner.domain.B2BPartner) partner).hasValidContract();
        }
        
        // For B2C partners, no contract validation needed
        return true;
    }

    /**
     * Validates if a partner has sufficient credit for a transaction.
     * 
     * @param id the partner ID
     * @param amount the transaction amount
     * @return true if partner has sufficient credit
     * @throws PartnerException if partner not found
     */
    @Override
    @Transactional(readOnly = true)
    public boolean validatePartnerCredit(Long id, BigDecimal amount) {
        log.debug("Validating credit for partner ID: {} with amount: {}", id, amount);
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        if (partner.getCreditInfo() == null || partner.getCreditInfo().getCreditLimit() == null) {
            return false;
        }
        
        BigDecimal availableCredit = partner.getCreditInfo().getCreditLimit()
                .subtract(partner.getCreditInfo().getOutstandingBalance() != null ? 
                        partner.getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO);
        
        return availableCredit.compareTo(amount) >= 0;
    }

    /**
     * Checks if a partner is active.
     * 
     * @param id the partner ID
     * @return true if partner is active
     * @throws PartnerException if partner not found
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isPartnerActive(Long id) {
        log.debug("Checking if partner ID: {} is active", id);
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + id));
        
        return partner.isActive();
    }

    // ========== Statistics and Reporting ==========

    /**
     * Generates comprehensive partner statistics.
     * 
     * @return map containing various partner statistics
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPartnerStatistics() {
        log.debug("Generating partner statistics");
        Map<String, Object> statistics = new HashMap<>();
        
        // Basic counts
        statistics.put("totalPartners", partnerRepository.count());
        statistics.put("activePartners", partnerRepository.countByActiveTrue());
        statistics.put("vipPartners", partnerRepository.countByIsVipTrue());
        statistics.put("b2bPartners", partnerRepository.countByPartnerType(PartnerType.B2B));
        statistics.put("b2cPartners", partnerRepository.countByPartnerType(PartnerType.B2C));
        
        // Top spenders
        List<Partner> topSpenders = partnerRepository.findTopByOrderByTotalSpentDesc(10);
        statistics.put("topSpenders", partnerMapper.toDTOList(topSpenders));
        
        return statistics;
    }

    /**
     * Gets top partners by spending amount.
     * 
     * @param limit the number of top partners to retrieve
     * @return list of top spending partners
     */
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getTopPartnersBySpending(int limit) {
        log.debug("Fetching top {} partners by spending", limit);
        List<Partner> topSpenders = partnerRepository.findTopByOrderByTotalSpentDesc(limit);
        return partnerMapper.toDTOList(topSpenders);
    }

    /**
     * Gets partner distribution by type.
     * 
     * @return map of partner types to counts
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getPartnerDistributionByType() {
        log.debug("Generating partner distribution by type");
        Map<String, Integer> distribution = new HashMap<>();
        
        distribution.put("B2B", (int) partnerRepository.countByPartnerType(PartnerType.B2B));
        distribution.put("B2C", (int) partnerRepository.countByPartnerType(PartnerType.B2C));
        
        return distribution;
    }

    /**
     * Gets average order value by partner type.
     * 
     * @return map of partner types to average order values
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getAverageOrderValueByPartnerType() {
        log.debug("Calculating average order value by partner type");
        Map<String, BigDecimal> averages = new HashMap<>();
        
        // This would require more complex queries or integration with order service
        // For now, returning placeholder values
        averages.put("B2B", BigDecimal.valueOf(500.00));
        averages.put("B2C", BigDecimal.valueOf(150.00));
        
        return averages;
    }

    /**
     * Gets all partners with pagination support.
     * 
     * @param pageable pagination parameters
     * @return paginated list of partners
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PartnerDTO> getAllPartners(Pageable pageable) {
        log.debug("Fetching all partners with pagination: page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Partner> partners = partnerRepository.findAll(pageable);
        return partners.map(partnerMapper::toDTO);
    }

    // ========== Type-Specific Operations ==========

    @Override
    public PartnerDTO createB2BPartner(B2BPartnerDTO b2bPartnerDTO) {
        log.info("Creating new B2B partner with CT number: {}", b2bPartnerDTO.getCtNum());
        
        // Validate unique constraints
        validateUniqueConstraintsForB2B(b2bPartnerDTO);
        
        // Validate business rules
        validateBusinessRulesForB2B(b2bPartnerDTO);
        
        B2BPartner b2bPartner = b2bPartnerMapper.toEntity(b2bPartnerDTO);
        
        // Set audit information
        if (b2bPartner.getAuditInfo() == null) {
            b2bPartner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        b2bPartner.getAuditInfo().setCreatedAt(ZonedDateTime.now());
        b2bPartner.getAuditInfo().setActive(true);
        
        B2BPartner savedPartner = partnerRepository.save(b2bPartner);
        log.info("B2B partner created successfully with ID: {}", savedPartner.getId());
        
        return b2bPartnerMapper.toPartnerDTO(savedPartner);
    }

    @Override
    public PartnerDTO createB2CPartner(B2CPartnerDTO b2cPartnerDTO) {
        log.info("Creating new B2C partner with CT number: {}", b2cPartnerDTO.getCtNum());
        
        // Validate unique constraints
        validateUniqueConstraintsForB2C(b2cPartnerDTO);
        
        // Validate business rules
        validateBusinessRulesForB2C(b2cPartnerDTO);
        
        B2CPartner b2cPartner = b2cPartnerMapper.toEntity(b2cPartnerDTO);
        
        // Set audit information
        if (b2cPartner.getAuditInfo() == null) {
            b2cPartner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        b2cPartner.getAuditInfo().setCreatedAt(ZonedDateTime.now());
        b2cPartner.getAuditInfo().setActive(true);
        
        B2CPartner savedPartner = partnerRepository.save(b2cPartner);
        log.info("B2C partner created successfully with ID: {}", savedPartner.getId());
        
        return b2cPartnerMapper.toPartnerDTO(savedPartner);
    }

    @Override
    public PartnerDTO updateB2BPartner(Long id, B2BPartnerDTO b2bPartnerDTO) {
        log.info("Updating B2B partner with ID: {}", id);
        
        B2BPartner existingPartner = partnerRepository.findById(id)
                .filter(partner -> partner instanceof B2BPartner)
                .map(partner -> (B2BPartner) partner)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "B2B partner not found with ID: " + id));
        
        // Validate unique constraints (excluding current partner)
        validateUniqueConstraintsForB2BUpdate(b2bPartnerDTO, id);
        
        // Validate business rules
        validateBusinessRulesForB2B(b2bPartnerDTO);
        
        // Update audit information
        if (existingPartner.getAuditInfo() == null) {
            existingPartner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        existingPartner.getAuditInfo().setUpdatedAt(ZonedDateTime.now());
        existingPartner.getAuditInfo().setLastActivityDate(ZonedDateTime.now());
        
        b2bPartnerMapper.updateEntityFromDTO(b2bPartnerDTO, existingPartner);
        B2BPartner updatedPartner = partnerRepository.save(existingPartner);
        
        log.info("B2B partner updated successfully with ID: {}", updatedPartner.getId());
        return b2bPartnerMapper.toPartnerDTO(updatedPartner);
    }

    @Override
    public PartnerDTO updateB2CPartner(Long id, B2CPartnerDTO b2cPartnerDTO) {
        log.info("Updating B2C partner with ID: {}", id);
        
        B2CPartner existingPartner = partnerRepository.findById(id)
                .filter(partner -> partner instanceof B2CPartner)
                .map(partner -> (B2CPartner) partner)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "B2C partner not found with ID: " + id));
        
        // Validate unique constraints (excluding current partner)
        validateUniqueConstraintsForB2CUpdate(b2cPartnerDTO, id);
        
        // Validate business rules
        validateBusinessRulesForB2C(b2cPartnerDTO);
        
        // Update audit information
        if (existingPartner.getAuditInfo() == null) {
            existingPartner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        existingPartner.getAuditInfo().setUpdatedAt(ZonedDateTime.now());
        existingPartner.getAuditInfo().setLastActivityDate(ZonedDateTime.now());
        
        b2cPartnerMapper.updateEntityFromDTO(b2cPartnerDTO, existingPartner);
        B2CPartner updatedPartner = partnerRepository.save(existingPartner);
        
        log.info("B2C partner updated successfully with ID: {}", updatedPartner.getId());
        return b2cPartnerMapper.toPartnerDTO(updatedPartner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getAllB2BPartners() {
        log.debug("Fetching all B2B partners");
        List<Partner> allPartners = partnerRepository.findByPartnerType(PartnerType.B2B);
        return allPartners.stream()
                .filter(partner -> partner instanceof B2BPartner)
                .map(partner -> b2bPartnerMapper.toPartnerDTO((B2BPartner) partner))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getAllB2CPartners() {
        log.debug("Fetching all B2C partners");
        List<Partner> allPartners = partnerRepository.findByPartnerType(PartnerType.B2C);
        return allPartners.stream()
                .filter(partner -> partner instanceof B2CPartner)
                .map(partner -> b2cPartnerMapper.toPartnerDTO((B2CPartner) partner))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartnerDTO> getB2BPartners(Pageable pageable) {
        log.debug("Fetching B2B partners with pagination: page {}", pageable.getPageNumber());
        Page<Partner> allPartners = partnerRepository.findAll(pageable);
        List<PartnerDTO> b2bPartners = allPartners.getContent().stream()
                .filter(p -> PartnerType.B2B.equals(p.getPartnerType()) && p instanceof B2BPartner)
                .map(p -> b2bPartnerMapper.toPartnerDTO((B2BPartner) p))
                .toList();
        return new org.springframework.data.domain.PageImpl<>(b2bPartners, pageable, allPartners.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartnerDTO> getB2CPartners(Pageable pageable) {
        log.debug("Fetching B2C partners with pagination: page {}", pageable.getPageNumber());
        Page<Partner> allPartners = partnerRepository.findAll(pageable);
        List<PartnerDTO> b2cPartners = allPartners.getContent().stream()
                .filter(p -> PartnerType.B2C.equals(p.getPartnerType()) && p instanceof B2CPartner)
                .map(p -> b2cPartnerMapper.toPartnerDTO((B2CPartner) p))
                .toList();
        return new org.springframework.data.domain.PageImpl<>(b2cPartners, pageable, allPartners.getTotalElements());
    }

    // ========== Contract Management ==========

    @Override
    @Transactional(readOnly = true)
    public List<PartnerDTO> getPartnersWithExpiringContracts(int daysThreshold) {
        log.debug("Fetching partners with contracts expiring in {} days", daysThreshold);
        ZonedDateTime thresholdDate = ZonedDateTime.now().plusDays(daysThreshold);
        List<Partner> partners = partnerRepository.findByContractEndDateBefore(thresholdDate);
        return partners.stream()
                .filter(partner -> partner instanceof B2BPartner)
                .map(partner -> b2bPartnerMapper.toPartnerDTO((B2BPartner) partner))
                .toList();
    }

    @Override
    public PartnerDTO renewContract(Long partnerId, ZonedDateTime newEndDate) {
        log.info("Renewing contract for partner ID: {} with new end date: {}", partnerId, newEndDate);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        // Update contract end date for B2B partners
        if (partner instanceof B2BPartner) {
            ((B2BPartner) partner).setContractEndDate(newEndDate);
            Partner updatedPartner = partnerRepository.save(partner);
            log.info("Contract renewed successfully for partner ID: {}", partnerId);
            return b2bPartnerMapper.toPartnerDTO((B2BPartner) updatedPartner);
        } else {
            throw new PartnerException(ErrorCode.VALIDATION_CONTRACT_DATES, "Only B2B partners can have contracts");
        }
    }

    @Override
    public PartnerDTO terminateContract(Long partnerId, ZonedDateTime terminationDate, String reason) {
        log.info("Terminating contract for partner ID: {} with reason: {}", partnerId, reason);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        // Update contract end date for B2B partners
        if (partner instanceof B2BPartner) {
            ((B2BPartner) partner).setContractEndDate(terminationDate);
            Partner updatedPartner = partnerRepository.save(partner);
            log.info("Contract terminated successfully for partner ID: {}", partnerId);
            return b2bPartnerMapper.toPartnerDTO((B2BPartner) updatedPartner);
        } else {
            throw new PartnerException(ErrorCode.VALIDATION_CONTRACT_DATES, "Only B2B partners can have contracts");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getContractStatus(Long partnerId) {
        log.debug("Getting contract status for partner ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        Map<String, Object> status = new HashMap<>();
        
        if (partner instanceof ma.foodplus.ordering.system.partner.domain.B2BPartner) {
            ma.foodplus.ordering.system.partner.domain.B2BPartner b2bPartner = (ma.foodplus.ordering.system.partner.domain.B2BPartner) partner;
            status.put("contractNumber", b2bPartner.getContractNumber());
            status.put("endDate", b2bPartner.getContractEndDate());
            status.put("isActive", b2bPartner.hasValidContract());
        } else {
            status.put("contractNumber", null);
            status.put("endDate", null);
            status.put("isActive", true); // B2C partners don't have contracts
        }
        
        return status;
    }

    // ========== Credit Management ==========

    @Override
    public PartnerDTO updateOutstandingBalance(Long partnerId, BigDecimal amount) {
        log.info("Updating outstanding balance for partner ID: {} by {}", partnerId, amount);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        if (partner.getCreditInfo() == null) {
            partner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        }
        
        BigDecimal currentBalance = partner.getCreditInfo().getOutstandingBalance() != null ? 
                partner.getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO;
        partner.getCreditInfo().setOutstandingBalance(currentBalance.add(amount));
        
        Partner updatedPartner = partnerRepository.save(partner);
        log.info("Outstanding balance updated successfully for partner ID: {}", partnerId);
        
        // Return appropriate DTO based on partner type
        if (updatedPartner instanceof B2BPartner) {
            return b2bPartnerMapper.toPartnerDTO((B2BPartner) updatedPartner);
        } else if (updatedPartner instanceof B2CPartner) {
            return b2cPartnerMapper.toPartnerDTO((B2CPartner) updatedPartner);
        } else {
            return partnerMapper.toDTO(updatedPartner);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCreditSummary(Long partnerId) {
        log.debug("Getting credit summary for partner ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        Map<String, Object> summary = new HashMap<>();
        if (partner.getCreditInfo() != null) {
            summary.put("creditLimit", partner.getCreditInfo().getCreditLimit());
            summary.put("outstandingBalance", partner.getCreditInfo().getOutstandingBalance());
            summary.put("availableCredit", partner.getCreditInfo().getCreditLimit() != null ? 
                    partner.getCreditInfo().getCreditLimit().subtract(
                            partner.getCreditInfo().getOutstandingBalance() != null ? 
                                    partner.getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO) : BigDecimal.ZERO);
            summary.put("creditScore", partner.getCreditInfo().getCreditScore());
            summary.put("creditRating", partner.getCreditInfo().getCreditRating());
        }
        return summary;
    }

    @Override
    public PartnerDTO processPayment(Long partnerId, BigDecimal amount, String paymentMethod) {
        log.info("Processing payment for partner ID: {} amount: {} method: {}", partnerId, amount, paymentMethod);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        if (partner.getCreditInfo() == null) {
            partner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        }
        
        // Reduce outstanding balance
        BigDecimal currentBalance = partner.getCreditInfo().getOutstandingBalance() != null ? 
                partner.getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO;
        partner.getCreditInfo().setOutstandingBalance(currentBalance.subtract(amount));
        partner.getCreditInfo().setLastPaymentDate(ZonedDateTime.now());
        
        Partner updatedPartner = partnerRepository.save(partner);
        log.info("Payment processed successfully for partner ID: {}", partnerId);
        
        // Return appropriate DTO based on partner type
        if (updatedPartner instanceof B2BPartner) {
            return b2bPartnerMapper.toPartnerDTO((B2BPartner) updatedPartner);
        } else if (updatedPartner instanceof B2CPartner) {
            return b2cPartnerMapper.toPartnerDTO((B2CPartner) updatedPartner);
        } else {
            return partnerMapper.toDTO(updatedPartner);
        }
    }

    // ========== Validation Operations ==========

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> validateOrderPlacement(Long partnerId, BigDecimal orderAmount) {
        log.debug("Validating order placement for partner ID: {} amount: {}", partnerId, orderAmount);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        Map<String, Object> validation = new HashMap<>();
        validation.put("canPlaceOrder", partner.isActive());
        validation.put("reasons", new ArrayList<String>());
        
        // Check if partner is active
        if (!partner.isActive()) {
            validation.put("canPlaceOrder", false);
            ((List<String>) validation.get("reasons")).add("Partner is not active");
        }
        
        // Check credit availability
        if (partner.getCreditInfo() != null && partner.getCreditInfo().getCreditLimit() != null) {
            BigDecimal availableCredit = partner.getCreditInfo().getCreditLimit()
                    .subtract(partner.getCreditInfo().getOutstandingBalance() != null ? 
                            partner.getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO);
            
            if (availableCredit.compareTo(orderAmount) >= 0) {
                validation.put("canPlaceOrder", true);
            } else {
                ((List<String>) validation.get("reasons")).add("Insufficient credit");
            }
        }
        
        return validation;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> validateCreditEligibility(Long partnerId, BigDecimal requestedAmount) {
        log.debug("Validating credit eligibility for partner ID: {} amount: {}", partnerId, requestedAmount);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        Map<String, Object> validation = new HashMap<>();
        validation.put("eligible", partner.isActive() && partner.getCreditInfo() != null);
        return validation;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getValidationStatus(Long partnerId) {
        log.debug("Getting validation status for partner ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        Map<String, Object> status = new HashMap<>();
        status.put("isActive", partner.isActive());
        
        // Check contract validity based on partner type
        boolean hasValidContract = true;
        if (partner instanceof ma.foodplus.ordering.system.partner.domain.B2BPartner) {
            hasValidContract = ((ma.foodplus.ordering.system.partner.domain.B2BPartner) partner).hasValidContract();
        }
        status.put("hasValidContract", hasValidContract);
        
        status.put("hasCreditInfo", partner.getCreditInfo() != null);
        status.put("creditLimit", partner.getCreditInfo() != null ? partner.getCreditInfo().getCreditLimit() : null);
        status.put("outstandingBalance", partner.getCreditInfo() != null ? partner.getCreditInfo().getOutstandingBalance() : null);
        
        return status;
    }

    // ========== Bulk Operations ==========

    @Override
    public int bulkUpdateStatus(List<Long> partnerIds, boolean active) {
        log.info("Bulk updating status for {} partners to {}", partnerIds.size(), active);
        int updatedCount = 0;
        
        for (Long partnerId : partnerIds) {
            try {
                Partner partner = partnerRepository.findById(partnerId).orElse(null);
                if (partner != null) {
                    if (partner.getAuditInfo() == null) {
                        partner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
                    }
                    partner.getAuditInfo().setActive(active);
                    partner.getAuditInfo().setUpdatedAt(ZonedDateTime.now());
                    partnerRepository.save(partner);
                    updatedCount++;
                }
            } catch (Exception e) {
                log.warn("Failed to update partner {}: {}", partnerId, e.getMessage());
            }
        }
        
        log.info("Bulk status update completed: {} of {} partners updated", updatedCount, partnerIds.size());
        return updatedCount;
    }

    @Override
    public int bulkUpdateCreditLimits(List<Long> partnerIds, BigDecimal newLimit) {
        log.info("Bulk updating credit limits for {} partners to {}", partnerIds.size(), newLimit);
        int updatedCount = 0;
        
        for (Long partnerId : partnerIds) {
            try {
                Partner partner = partnerRepository.findById(partnerId).orElse(null);
                if (partner != null) {
                    if (partner.getCreditInfo() == null) {
                        partner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
                    }
                    partner.getCreditInfo().setCreditLimit(newLimit);
                    partnerRepository.save(partner);
                    updatedCount++;
                }
            } catch (Exception e) {
                log.warn("Failed to update credit limit for partner {}: {}", partnerId, e.getMessage());
            }
        }
        
        log.info("Bulk credit limit update completed: {} of {} partners updated", updatedCount, partnerIds.size());
        return updatedCount;
    }

    @Override
    public int bulkAddToGroup(List<Long> partnerIds, Long groupId) {
        log.info("Bulk adding {} partners to group {}", partnerIds.size(), groupId);
        int addedCount = 0;
        
        for (Long partnerId : partnerIds) {
            try {
                if (!partnerRepository.isPartnerInGroup(partnerId, groupId)) {
                    partnerRepository.addPartnerToGroup(partnerId, groupId);
                    addedCount++;
                }
            } catch (Exception e) {
                log.warn("Failed to add partner {} to group {}: {}", partnerId, groupId, e.getMessage());
            }
        }
        
        log.info("Bulk group addition completed: {} of {} partners added", addedCount, partnerIds.size());
        return addedCount;
    }

    // ========== Audit and History ==========

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAuditHistory(Long partnerId) {
        log.debug("Getting audit history for partner ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        List<Map<String, Object>> history = new ArrayList<>();
        
        // Add basic audit info
        if (partner.getAuditInfo() != null) {
            Map<String, Object> auditEntry = new HashMap<>();
            auditEntry.put("createdAt", partner.getAuditInfo().getCreatedAt());
            auditEntry.put("updatedAt", partner.getAuditInfo().getUpdatedAt());
            auditEntry.put("createdBy", partner.getAuditInfo().getCreatedBy());
            auditEntry.put("updatedBy", partner.getAuditInfo().getUpdatedBy());
            auditEntry.put("active", partner.getAuditInfo().isActive());
            history.add(auditEntry);
        }
        
        return history;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getActivityLog(Long partnerId, ZonedDateTime startDate, ZonedDateTime endDate) {
        log.debug("Getting activity log for partner ID: {} from {} to {}", partnerId, startDate, endDate);
        
        // This would typically query an activity log table
        // For now, return basic partner activity info
        List<Map<String, Object>> activityLog = new ArrayList<>();
        
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        if (partner != null && partner.getAuditInfo() != null) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("partnerId", partnerId);
            activity.put("lastActivityDate", partner.getAuditInfo().getLastActivityDate());
            activity.put("lastOrderDate", partner.getLoyaltyInfo() != null ? partner.getLoyaltyInfo().getLastOrderDate() : null);
            activityLog.add(activity);
        }
        
        return activityLog;
    }

    // ========== Advanced Analytics ==========

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPerformanceMetrics(Long partnerId) {
        log.debug("Getting performance metrics for partner ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        Map<String, Object> metrics = new HashMap<>();
        
        if (partner.getLoyaltyInfo() != null) {
            metrics.put("totalOrders", partner.getLoyaltyInfo().getTotalOrders());
            metrics.put("totalSpent", partner.getLoyaltyInfo().getTotalSpent());
            metrics.put("averageOrderValue", partner.getLoyaltyInfo().getAverageOrderValue());
            metrics.put("loyaltyPoints", partner.getLoyaltyInfo().getLoyaltyPoints());
            metrics.put("loyaltyLevel", getPartnerLoyaltyLevel(partnerId));
        }
        
        if (partner.getCreditInfo() != null) {
            metrics.put("creditLimit", partner.getCreditInfo().getCreditLimit());
            metrics.put("outstandingBalance", partner.getCreditInfo().getOutstandingBalance());
            metrics.put("creditScore", partner.getCreditInfo().getCreditScore());
        }
        
        return metrics;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getRiskAssessment(Long partnerId) {
        log.debug("Getting risk assessment for partner ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_NOT_FOUND, "Partner not found with ID: " + partnerId));
        
        Map<String, Object> riskAssessment = new HashMap<>();
        riskAssessment.put("riskLevel", "LOW");
        riskAssessment.put("riskScore", 25);
        riskAssessment.put("factors", List.of("Good payment history", "Active status"));
        
        // Basic risk assessment logic
        if (partner.getCreditInfo() != null && partner.getCreditInfo().getOutstandingBalance() != null) {
            BigDecimal outstandingBalance = partner.getCreditInfo().getOutstandingBalance();
            BigDecimal creditLimit = partner.getCreditInfo().getCreditLimit();
            
            if (creditLimit != null && creditLimit.compareTo(BigDecimal.ZERO) > 0) {
                double utilizationRate = outstandingBalance.divide(creditLimit, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                if (utilizationRate > 0.8) {
                    riskAssessment.put("riskLevel", "HIGH");
                    riskAssessment.put("riskScore", 75);
                } else if (utilizationRate > 0.5) {
                    riskAssessment.put("riskLevel", "MEDIUM");
                    riskAssessment.put("riskScore", 50);
                }
            }
        }
        
        return riskAssessment;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getGrowthTrends(Long partnerId, String period) {
        log.debug("Getting growth trends for partner ID: {} period: {}", partnerId, period);
        
        // This would typically query historical data
        // For now, return placeholder data
        Map<String, Object> trends = new HashMap<>();
        trends.put("period", period);
        trends.put("growthRate", 15.5);
        trends.put("trend", "INCREASING");
        trends.put("dataPoints", List.of(
            Map.of("date", "2024-01", "value", 1000.0),
            Map.of("date", "2024-02", "value", 1150.0),
            Map.of("date", "2024-03", "value", 1325.0)
        ));
        
        return trends;
    }

    // ========== Private Helper Methods ==========

    /**
     * Validates unique constraints for partner creation.
     * 
     * @param partnerDTO the partner DTO to validate
     * @throws PartnerException if constraints are violated
     */
    private void validateUniqueConstraints(PartnerDTO partnerDTO) {
        if (partnerRepository.existsByCtNum(partnerDTO.getCtNum())) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_CT_NUM, "Partner with CT number already exists: " + partnerDTO.getCtNum());
        }
        
        if (partnerRepository.existsByIce(partnerDTO.getIce())) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_ICE, "Partner with ICE already exists: " + partnerDTO.getIce());
        }
    }

    /**
     * Validates unique constraints for partner updates.
     * 
     * @param partnerDTO the partner DTO to validate
     * @param currentId the current partner ID
     * @throws PartnerException if constraints are violated
     */
    private void validateUniqueConstraintsForUpdate(PartnerDTO partnerDTO, Long currentId) {
        Optional<Partner> existingByCtNum = partnerRepository.findByCtNum(partnerDTO.getCtNum());
        if (existingByCtNum.isPresent() && !existingByCtNum.get().getId().equals(currentId)) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_CT_NUM, "Partner with CT number already exists: " + partnerDTO.getCtNum());
        }
        
        Optional<Partner> existingByIce = partnerRepository.findByIce(partnerDTO.getIce());
        if (existingByIce.isPresent() && !existingByIce.get().getId().equals(currentId)) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_ICE, "Partner with ICE already exists: " + partnerDTO.getIce());
        }
    }

    /**
     * Validates business rules for partner data.
     * 
     * @param partnerDTO the partner DTO to validate
     * @throws PartnerException if business rules are violated
     */
    private void validateBusinessRules(PartnerDTO partnerDTO) {
        // Validate contract dates for B2B partners
        if (partnerDTO.getPartnerType() == PartnerType.B2B) {
            if (partnerDTO.getContractStartDate() != null && partnerDTO.getContractEndDate() != null) {
                if (partnerDTO.getContractEndDate().isBefore(partnerDTO.getContractStartDate())) {
                    throw new PartnerException(ErrorCode.VALIDATION_CONTRACT_DATES, "Contract end date must be after start date");
                }
            }
        }
        
        // Validate credit limit
        if (partnerDTO.getCreditLimit() != null && partnerDTO.getCreditLimit().compareTo(BigDecimal.ZERO) < 0) {
            throw new PartnerException(ErrorCode.VALIDATION_CREDIT_LIMIT_NEGATIVE, "Credit limit cannot be negative");
        }
        
        // Validate loyalty points
        if (partnerDTO.getLoyaltyPoints() != null && partnerDTO.getLoyaltyPoints() < 0) {
            throw new PartnerException(ErrorCode.VALIDATION_LOYALTY_POINTS_NEGATIVE, "Loyalty points cannot be negative");
        }
    }

    /**
     * Validates unique constraints for B2B partner creation.
     * 
     * @param b2bPartnerDTO the B2B partner DTO to validate
     * @throws PartnerException if constraints are violated
     */
    private void validateUniqueConstraintsForB2B(B2BPartnerDTO b2bPartnerDTO) {
        if (partnerRepository.existsByCtNum(b2bPartnerDTO.getCtNum())) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_CT_NUM, "Partner with CT number already exists: " + b2bPartnerDTO.getCtNum());
        }
        
        if (partnerRepository.existsByIce(b2bPartnerDTO.getIce())) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_ICE, "Partner with ICE already exists: " + b2bPartnerDTO.getIce());
        }
    }

    /**
     * Validates business rules for B2B partner data.
     * 
     * @param b2bPartnerDTO the B2B partner DTO to validate
     * @throws PartnerException if business rules are violated
     */
    private void validateBusinessRulesForB2B(B2BPartnerDTO b2bPartnerDTO) {
        // Validate contract dates for B2B partners
        if (b2bPartnerDTO.getContractStartDate() != null && b2bPartnerDTO.getContractEndDate() != null) {
            if (b2bPartnerDTO.getContractEndDate().isBefore(b2bPartnerDTO.getContractStartDate())) {
                throw new PartnerException(ErrorCode.VALIDATION_CONTRACT_DATES, "Contract end date must be after start date");
            }
        }
        
        // Validate credit limit
        if (b2bPartnerDTO.getCreditLimit() != null && b2bPartnerDTO.getCreditLimit().compareTo(BigDecimal.ZERO) < 0) {
            throw new PartnerException(ErrorCode.VALIDATION_CREDIT_LIMIT_NEGATIVE, "Credit limit cannot be negative");
        }
        
        // Validate loyalty points
        if (b2bPartnerDTO.getLoyaltyPoints() != null && b2bPartnerDTO.getLoyaltyPoints() < 0) {
            throw new PartnerException(ErrorCode.VALIDATION_LOYALTY_POINTS_NEGATIVE, "Loyalty points cannot be negative");
        }
    }

    /**
     * Validates unique constraints for B2B partner updates.
     * 
     * @param b2bPartnerDTO the B2B partner DTO to validate
     * @param currentId the current partner ID
     * @throws PartnerException if constraints are violated
     */
    private void validateUniqueConstraintsForB2BUpdate(B2BPartnerDTO b2bPartnerDTO, Long currentId) {
        Optional<Partner> existingByCtNum = partnerRepository.findByCtNum(b2bPartnerDTO.getCtNum());
        if (existingByCtNum.isPresent() && !existingByCtNum.get().getId().equals(currentId)) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_CT_NUM, "Partner with CT number already exists: " + b2bPartnerDTO.getCtNum());
        }
        
        Optional<Partner> existingByIce = partnerRepository.findByIce(b2bPartnerDTO.getIce());
        if (existingByIce.isPresent() && !existingByIce.get().getId().equals(currentId)) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_ICE, "Partner with ICE already exists: " + b2bPartnerDTO.getIce());
        }
    }

    /**
     * Validates unique constraints for B2C partner creation.
     * 
     * @param b2cPartnerDTO the B2C partner DTO to validate
     * @throws PartnerException if constraints are violated
     */
    private void validateUniqueConstraintsForB2C(B2CPartnerDTO b2cPartnerDTO) {
        if (partnerRepository.existsByCtNum(b2cPartnerDTO.getCtNum())) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_CT_NUM, "Partner with CT number already exists: " + b2cPartnerDTO.getCtNum());
        }
        
        if (partnerRepository.existsByIce(b2cPartnerDTO.getIce())) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_ICE, "Partner with ICE already exists: " + b2cPartnerDTO.getIce());
        }
    }

    /**
     * Validates business rules for B2C partner data.
     * 
     * @param b2cPartnerDTO the B2C partner DTO to validate
     * @throws PartnerException if business rules are violated
     */
    private void validateBusinessRulesForB2C(B2CPartnerDTO b2cPartnerDTO) {
        // Validate credit limit
        if (b2cPartnerDTO.getCreditLimit() != null && b2cPartnerDTO.getCreditLimit().compareTo(BigDecimal.ZERO) < 0) {
            throw new PartnerException(ErrorCode.VALIDATION_CREDIT_LIMIT_NEGATIVE, "Credit limit cannot be negative");
        }
        
        // Validate loyalty points
        if (b2cPartnerDTO.getLoyaltyPoints() != null && b2cPartnerDTO.getLoyaltyPoints() < 0) {
            throw new PartnerException(ErrorCode.VALIDATION_LOYALTY_POINTS_NEGATIVE, "Loyalty points cannot be negative");
        }
    }

    /**
     * Validates unique constraints for B2C partner updates.
     * 
     * @param b2cPartnerDTO the B2C partner DTO to validate
     * @param currentId the current partner ID
     * @throws PartnerException if constraints are violated
     */
    private void validateUniqueConstraintsForB2CUpdate(B2CPartnerDTO b2cPartnerDTO, Long currentId) {
        Optional<Partner> existingByCtNum = partnerRepository.findByCtNum(b2cPartnerDTO.getCtNum());
        if (existingByCtNum.isPresent() && !existingByCtNum.get().getId().equals(currentId)) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_CT_NUM, "Partner with CT number already exists: " + b2cPartnerDTO.getCtNum());
        }
        
        Optional<Partner> existingByIce = partnerRepository.findByIce(b2cPartnerDTO.getIce());
        if (existingByIce.isPresent() && !existingByIce.get().getId().equals(currentId)) {
            throw new PartnerException(ErrorCode.PARTNER_DUPLICATE_ICE, "Partner with ICE already exists: " + b2cPartnerDTO.getIce());
        }
    }
} 