package ma.foodplus.ordering.system.partner.mapper;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.partner.domain.B2BPartner;
import ma.foodplus.ordering.system.partner.domain.B2CPartner;
import ma.foodplus.ordering.system.partner.domain.Partner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory-based mapper for Partner entities and DTOs.
 * 
 * <p>This mapper uses a factory pattern to delegate to specific mappers based on partner type,
 * ensuring proper handling of type-specific fields and business logic.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Component
@RequiredArgsConstructor
public class PartnerMapper {

    private final PartnerMapperFactory partnerMapperFactory;

    /**
     * Maps a Partner entity to PartnerDTO using the appropriate specific mapper.
     */
    public PartnerDTO toDTO(Partner partner) {
        if (partner == null) return null;
        
        return partnerMapperFactory.toGenericDTO(partner);
    }

    /**
     * Maps a PartnerDTO to Partner entity using the appropriate specific mapper.
     */
    public Partner toEntity(PartnerDTO partnerDTO) {
        if (partnerDTO == null) return null;
        
        return partnerMapperFactory.toEntity(partnerDTO);
    }

    /**
     * Updates a Partner entity from PartnerDTO using the appropriate specific mapper.
     */
    public void updateEntityFromDTO(PartnerDTO partnerDTO, Partner partner) {
        if (partnerDTO == null || partner == null) return;
        
        // Update basic partner information
        partner.setCtNum(partnerDTO.getCtNum());
        partner.setIce(partnerDTO.getIce());
        partner.setDescription(partnerDTO.getDescription());
        partner.setCateTarif(partnerDTO.getCategoryTarifId());
        
        // Update Contact Information (Individual columns)
        partner.setTelephone(partnerDTO.getTelephone());
        partner.setEmail(partnerDTO.getEmail());
        partner.setAddress(partnerDTO.getAddress());
        partner.setPostalCode(partnerDTO.getCodePostal());
        partner.setCity(partnerDTO.getVille());
        partner.setCountry(partnerDTO.getCountry());
        
        // Update Credit Information (Individual columns)
        partner.setCreditLimit(partnerDTO.getCreditLimit());
        partner.setOutstandingBalance(partnerDTO.getOutstandingBalance());
        partner.setCreditRating(partnerDTO.getCreditRating());
        partner.setCreditScore(partnerDTO.getCreditScore());
        partner.setPaymentTermDays(partnerDTO.getPaymentTermDays());
        partner.setPreferredPaymentMethod(partnerDTO.getPreferredPaymentMethod());
        partner.setBankAccountInfo(partnerDTO.getBankAccountInfo());
        partner.setLastPaymentDate(partnerDTO.getLastPaymentDate());
        partner.setPaymentHistory(partnerDTO.getPaymentHistory());
        
        // Update Loyalty Information (Individual columns)
        partner.setIsVip(partnerDTO.isVip());
        partner.setLoyaltyPoints(partnerDTO.getLoyaltyPoints());
        partner.setLastOrderDate(partnerDTO.getLastOrderDate());
        partner.setTotalOrders(partnerDTO.getTotalOrders());
        partner.setTotalSpent(partnerDTO.getTotalSpent());
        partner.setAverageOrderValue(partnerDTO.getAverageOrderValue());
        partner.setPartnerSince(partnerDTO.getCustomerSince());
        
        // Update Delivery Preference (Individual columns)
        partner.setPreferredDeliveryTime(partnerDTO.getPreferredDeliveryTime());
        partner.setPreferredDeliveryDays(partnerDTO.getPreferredDeliveryDays());
        partner.setSpecialHandlingInstructions(partnerDTO.getSpecialHandlingInstructions());
        
        // Update Audit Information (Individual columns)
        partner.setNotes(partnerDTO.getNotes());
        partner.setIsActive(partnerDTO.isActive());
        partner.setLastActivityDate(partnerDTO.getLastActivityDate());
        partner.setCreatedBy(partnerDTO.getCreatedBy());
        partner.setUpdatedBy(partnerDTO.getUpdatedBy());
        partner.setCreatedAt(partnerDTO.getCreatedAt());
        partner.setUpdatedAt(partnerDTO.getUpdatedAt());
        
        // Update category tariff
        partner.setCateTarif(partnerDTO.getCategoryTarifId());
        
        // Update type-specific fields
        if (partner instanceof B2BPartner) {
            updateB2BFields(partnerDTO, (B2BPartner) partner);
        } else if (partner instanceof B2CPartner) {
            updateB2CFields(partnerDTO, (B2CPartner) partner);
        }
    }

    /**
     * Maps a list of Partner entities to PartnerDTO list.
     */
    public List<PartnerDTO> toDTOList(List<Partner> partners) {
        if (partners == null) return null;
        List<PartnerDTO> list = new ArrayList<>();
        for (Partner p : partners) list.add(toDTO(p));
        return list;
    }

    /**
     * Updates B2B-specific fields from PartnerDTO.
     */
    private void updateB2BFields(PartnerDTO partnerDTO, B2BPartner b2bPartner) {
        // Update Company Information (Individual columns)
        b2bPartner.setCompanyName(partnerDTO.getCompanyName());
        b2bPartner.setLegalForm(partnerDTO.getLegalForm());
        b2bPartner.setRegistrationNumber(partnerDTO.getRegistrationNumber());
        b2bPartner.setTaxId(partnerDTO.getTaxId());
        b2bPartner.setVatNumber(partnerDTO.getVatNumber());
        b2bPartner.setBusinessActivity(partnerDTO.getBusinessActivity());
        b2bPartner.setAnnualTurnover(partnerDTO.getAnnualTurnover());
        b2bPartner.setNumberOfEmployees(partnerDTO.getNumberOfEmployees());
        
        // Update Contract Information (Individual columns)
        b2bPartner.setContractNumber(partnerDTO.getContractNumber());
        b2bPartner.setContractStartDate(partnerDTO.getContractStartDate());
        b2bPartner.setContractEndDate(partnerDTO.getContractEndDate());
        b2bPartner.setContractType(partnerDTO.getContractType());
        b2bPartner.setContractTerms(partnerDTO.getContractTerms());
        b2bPartner.setPaymentTerms(partnerDTO.getPaymentTerms());
        b2bPartner.setDeliveryTerms(partnerDTO.getDeliveryTerms());
        b2bPartner.setSpecialConditions(partnerDTO.getSpecialConditions());
    }

    /**
     * Updates B2C-specific fields from PartnerDTO.
     */
    private void updateB2CFields(PartnerDTO partnerDTO, B2CPartner b2cPartner) {
        // B2C-specific fields are handled by the B2CPartnerMapper
        // This method is kept for consistency but B2C partners don't have additional fields
        // that need to be updated from the generic PartnerDTO
    }
} 