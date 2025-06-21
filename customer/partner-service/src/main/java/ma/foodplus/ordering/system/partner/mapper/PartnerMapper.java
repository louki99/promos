package ma.foodplus.ordering.system.partner.mapper;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.partner.domain.Partner;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Main Partner Mapper that delegates to type-specific mappers.
 * 
 * <p>This mapper provides backward compatibility with the existing PartnerMapper interface
 * while delegating to the appropriate type-specific mappers (B2B or B2C) based on the partner type.</p>
 * 
 * <p>For new code, it's recommended to use the type-specific mappers directly:
 * - B2BPartnerMapper for B2B partners
 * - B2CPartnerMapper for B2C partners
 * - PartnerMapperFactory for unified mapping operations</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Component
@RequiredArgsConstructor
public class PartnerMapper {
    
    private final PartnerMapperFactory partnerMapperFactory;
    
    /**
     * Maps a Partner entity to PartnerDTO.
     * 
     * @param partner the partner entity
     * @return the partner DTO
     */
    public PartnerDTO toDTO(Partner partner) {
        return partnerMapperFactory.toGenericDTO(partner);
    }
    
    /**
     * Maps a PartnerDTO to Partner entity.
     * 
     * @param partnerDTO the partner DTO
     * @return the partner entity
     */
    public Partner toEntity(PartnerDTO partnerDTO) {
        return partnerMapperFactory.toEntity(partnerDTO);
    }
    
    /**
     * Updates an existing Partner entity from PartnerDTO.
     * 
     * @param partnerDTO the partner DTO
     * @param partner the partner entity to update
     */
    public void updateEntityFromDTO(PartnerDTO partnerDTO, Partner partner) {
        if (partnerDTO == null || partner == null) {
            return;
        }
        
        // Update common fields
        partner.setCtNum(partnerDTO.getCtNum());
        partner.setIce(partnerDTO.getIce());
        partner.setDescription(partnerDTO.getDescription());
        
        // Update contact info
        if (partner.getContactInfo() == null) {
            partner.setContactInfo(new ma.foodplus.ordering.system.partner.domain.ContactInfo());
        }
        partner.getContactInfo().setTelephone(partnerDTO.getTelephone());
        partner.getContactInfo().setEmail(partnerDTO.getEmail());
        partner.getContactInfo().setAddress(partnerDTO.getAddress());
        partner.getContactInfo().setPostalCode(partnerDTO.getCodePostal());
        partner.getContactInfo().setCity(partnerDTO.getVille());
        partner.getContactInfo().setCountry(partnerDTO.getCountry());
        
        // Update credit info
        if (partner.getCreditInfo() == null) {
            partner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        }
        partner.getCreditInfo().setCreditLimit(partnerDTO.getCreditLimit());
        partner.getCreditInfo().setCreditRating(partnerDTO.getCreditRating());
        partner.getCreditInfo().setCreditScore(partnerDTO.getCreditScore());
        partner.getCreditInfo().setPaymentHistory(partnerDTO.getPaymentHistory());
        partner.getCreditInfo().setOutstandingBalance(partnerDTO.getOutstandingBalance());
        partner.getCreditInfo().setLastPaymentDate(partnerDTO.getLastPaymentDate());
        partner.getCreditInfo().setPaymentTermDays(partnerDTO.getPaymentTermDays());
        partner.getCreditInfo().setPreferredPaymentMethod(partnerDTO.getPreferredPaymentMethod());
        partner.getCreditInfo().setBankAccountInfo(partnerDTO.getBankAccountInfo());
        
        // Update loyalty info
        if (partner.getLoyaltyInfo() == null) {
            partner.setLoyaltyInfo(new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo());
        }
        partner.getLoyaltyInfo().setVip(partnerDTO.isVip());
        partner.getLoyaltyInfo().setLoyaltyPoints(partnerDTO.getLoyaltyPoints());
        partner.getLoyaltyInfo().setLastOrderDate(partnerDTO.getLastOrderDate());
        partner.getLoyaltyInfo().setTotalOrders(partnerDTO.getTotalOrders());
        partner.getLoyaltyInfo().setTotalSpent(partnerDTO.getTotalSpent());
        partner.getLoyaltyInfo().setAverageOrderValue(partnerDTO.getAverageOrderValue());
        partner.getLoyaltyInfo().setPartnerSince(partnerDTO.getCustomerSince());
        
        // Update delivery preference
        if (partner.getDeliveryPreference() == null) {
            partner.setDeliveryPreference(new ma.foodplus.ordering.system.partner.domain.DeliveryPreference());
        }
        partner.getDeliveryPreference().setPreferredDeliveryTime(partnerDTO.getPreferredDeliveryTime());
        partner.getDeliveryPreference().setPreferredDeliveryDays(partnerDTO.getPreferredDeliveryDays());
        partner.getDeliveryPreference().setSpecialHandlingInstructions(partnerDTO.getSpecialHandlingInstructions());
        
        // Update audit info
        if (partner.getAuditInfo() == null) {
            partner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        partner.getAuditInfo().setNotes(partnerDTO.getNotes());
        partner.getAuditInfo().setActive(partnerDTO.isActive());
        partner.getAuditInfo().setLastActivityDate(partnerDTO.getLastActivityDate());
        partner.getAuditInfo().setCreatedBy(partnerDTO.getCreatedBy());
        partner.getAuditInfo().setUpdatedBy(partnerDTO.getUpdatedBy());
        partner.getAuditInfo().setCreatedAt(partnerDTO.getCreatedAt());
        partner.getAuditInfo().setUpdatedAt(partnerDTO.getUpdatedAt());
        
        // Update category tariff
        partner.setCateTarif(partnerDTO.getCategoryTarifId());
        
        // Type-specific updates
        if (partner instanceof ma.foodplus.ordering.system.partner.domain.B2BPartner) {
            updateB2BFields(partnerDTO, (ma.foodplus.ordering.system.partner.domain.B2BPartner) partner);
        } else if (partner instanceof ma.foodplus.ordering.system.partner.domain.B2CPartner) {
            updateB2CFields(partnerDTO, (ma.foodplus.ordering.system.partner.domain.B2CPartner) partner);
        }
    }
    
    /**
     * Maps a list of partners to DTOs.
     * 
     * @param partners the list of partners
     * @return the list of partner DTOs
     */
    public List<PartnerDTO> toDTOList(List<Partner> partners) {
        return partnerMapperFactory.toDTOList(partners);
    }
    
    // ========== Private Helper Methods ==========
    
    /**
     * Updates B2B-specific fields.
     */
    private void updateB2BFields(PartnerDTO partnerDTO, ma.foodplus.ordering.system.partner.domain.B2BPartner b2bPartner) {
        // Update company info
        if (b2bPartner.getCompanyInfo() == null) {
            b2bPartner.setCompanyInfo(new ma.foodplus.ordering.system.partner.domain.CompanyInfo());
        }
        b2bPartner.getCompanyInfo().setCompanyName(partnerDTO.getCompanyName());
        b2bPartner.getCompanyInfo().setLegalForm(partnerDTO.getLegalForm());
        b2bPartner.getCompanyInfo().setRegistrationNumber(partnerDTO.getRegistrationNumber());
        b2bPartner.getCompanyInfo().setTaxId(partnerDTO.getTaxId());
        b2bPartner.getCompanyInfo().setVatNumber(partnerDTO.getVatNumber());
        b2bPartner.getCompanyInfo().setBusinessActivity(partnerDTO.getBusinessActivity());
        b2bPartner.getCompanyInfo().setAnnualTurnover(partnerDTO.getAnnualTurnover());
        b2bPartner.getCompanyInfo().setNumberOfEmployees(partnerDTO.getNumberOfEmployees());
        
        // Update contract info
        if (b2bPartner.getContractInfo() == null) {
            b2bPartner.setContractInfo(new ma.foodplus.ordering.system.partner.domain.ContractInfo());
        }
        b2bPartner.getContractInfo().setContractNumber(partnerDTO.getContractNumber());
        b2bPartner.getContractInfo().setContractStartDate(partnerDTO.getContractStartDate());
        b2bPartner.getContractInfo().setContractEndDate(partnerDTO.getContractEndDate());
        b2bPartner.getContractInfo().setContractType(partnerDTO.getContractType());
        b2bPartner.getContractInfo().setContractTerms(partnerDTO.getContractTerms());
        b2bPartner.getContractInfo().setPaymentTerms(partnerDTO.getPaymentTerms());
        b2bPartner.getContractInfo().setDeliveryTerms(partnerDTO.getDeliveryTerms());
        b2bPartner.getContractInfo().setSpecialConditions(partnerDTO.getSpecialConditions());
    }
    
    /**
     * Updates B2C-specific fields.
     */
    private void updateB2CFields(PartnerDTO partnerDTO, ma.foodplus.ordering.system.partner.domain.B2CPartner b2cPartner) {
        b2cPartner.setPersonalIdNumber(partnerDTO.getPersonalIdNumber());
        b2cPartner.setDateOfBirth(partnerDTO.getDateOfBirth());
        b2cPartner.setPreferredLanguage(partnerDTO.getPreferredLanguage());
        b2cPartner.setMarketingConsent(partnerDTO.getMarketingConsent());
    }
} 