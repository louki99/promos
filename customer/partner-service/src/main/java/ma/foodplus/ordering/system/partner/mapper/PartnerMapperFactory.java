package ma.foodplus.ordering.system.partner.mapper;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.partner.domain.B2BPartner;
import ma.foodplus.ordering.system.partner.domain.B2CPartner;
import ma.foodplus.ordering.system.partner.domain.Partner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.dto.B2BPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.B2CPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory mapper that delegates to type-specific mappers based on partner type.
 * 
 * <p>This factory provides a unified interface for mapping partners while delegating
 * to the appropriate type-specific mapper (B2B or B2C) based on the partner type.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Component
@RequiredArgsConstructor
public class PartnerMapperFactory {
    
    private final B2BPartnerMapper b2bPartnerMapper;
    private final B2CPartnerMapper b2cPartnerMapper;
    
    /**
     * Maps a Partner entity to the appropriate DTO based on its type.
     * 
     * @param partner the partner entity
     * @return the appropriate DTO (B2BPartnerDTO, B2CPartnerDTO, or PartnerDTO)
     */
    public Object toDTO(Partner partner) {
        if (partner == null) {
            return null;
        }
        
        if (partner instanceof B2BPartner) {
            return b2bPartnerMapper.toDTO((B2BPartner) partner);
        } else if (partner instanceof B2CPartner) {
            return b2cPartnerMapper.toDTO((B2CPartner) partner);
        } else {
            // Fallback to generic PartnerDTO
            return toGenericDTO(partner);
        }
    }
    
    /**
     * Maps a Partner entity to generic PartnerDTO.
     * 
     * @param partner the partner entity
     * @return the generic PartnerDTO
     */
    public PartnerDTO toGenericDTO(Partner partner) {
        if (partner == null) {
            return null;
        }
        
        if (partner instanceof B2BPartner) {
            return b2bPartnerMapper.toPartnerDTO((B2BPartner) partner);
        } else if (partner instanceof B2CPartner) {
            return b2cPartnerMapper.toPartnerDTO((B2CPartner) partner);
        } else {
            throw new IllegalArgumentException("Unknown partner type: " + partner.getClass());
        }
    }
    
    /**
     * Maps a PartnerDTO to the appropriate entity based on its type.
     * 
     * @param partnerDTO the partner DTO
     * @return the appropriate entity (B2BPartner or B2CPartner)
     */
    public Partner toEntity(PartnerDTO partnerDTO) {
        if (partnerDTO == null) {
            return null;
        }
        
        if (PartnerType.B2B.equals(partnerDTO.getPartnerType())) {
            // Convert PartnerDTO to B2BPartnerDTO
            B2BPartnerDTO b2bDTO = new B2BPartnerDTO();
            copyCommonFields(partnerDTO, b2bDTO);
            copyB2BFields(partnerDTO, b2bDTO);
            return b2bPartnerMapper.toEntity(b2bDTO);
        } else if (PartnerType.B2C.equals(partnerDTO.getPartnerType())) {
            // Convert PartnerDTO to B2CPartnerDTO
            B2CPartnerDTO b2cDTO = new B2CPartnerDTO();
            copyCommonFields(partnerDTO, b2cDTO);
            copyB2CFields(partnerDTO, b2cDTO);
            return b2cPartnerMapper.toEntity(b2cDTO);
        } else {
            throw new IllegalArgumentException("Unknown partner type: " + partnerDTO.getPartnerType());
        }
    }
    
    /**
     * Maps a B2BPartnerDTO to B2BPartner entity.
     * 
     * @param b2bPartnerDTO the B2B partner DTO
     * @return the B2B partner entity
     */
    public B2BPartner toB2BEntity(B2BPartnerDTO b2bPartnerDTO) {
        return b2bPartnerMapper.toEntity(b2bPartnerDTO);
    }
    
    /**
     * Maps a B2CPartnerDTO to B2CPartner entity.
     * 
     * @param b2cPartnerDTO the B2C partner DTO
     * @return the B2C partner entity
     */
    public B2CPartner toB2CEntity(B2CPartnerDTO b2cPartnerDTO) {
        return b2cPartnerMapper.toEntity(b2cPartnerDTO);
    }
    
    /**
     * Maps a list of partners to DTOs.
     * 
     * @param partners the list of partners
     * @return the list of DTOs
     */
    public List<PartnerDTO> toDTOList(List<Partner> partners) {
        if (partners == null) {
            return null;
        }
        
        return partners.stream()
                .map(this::toGenericDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Maps a list of B2B partners to B2B DTOs.
     * 
     * @param b2bPartners the list of B2B partners
     * @return the list of B2B DTOs
     */
    public List<B2BPartnerDTO> toB2BDTOList(List<B2BPartner> b2bPartners) {
        return b2bPartnerMapper.toDTOList(b2bPartners);
    }
    
    /**
     * Maps a list of B2C partners to B2C DTOs.
     * 
     * @param b2cPartners the list of B2C partners
     * @return the list of B2C DTOs
     */
    public List<B2CPartnerDTO> toB2CDTOList(List<B2CPartner> b2cPartners) {
        return b2cPartnerMapper.toDTOList(b2cPartners);
    }
    
    // ========== Private Helper Methods ==========
    
    /**
     * Copies common fields from PartnerDTO to B2BPartnerDTO.
     */
    private void copyCommonFields(PartnerDTO source, B2BPartnerDTO target) {
        target.setId(source.getId());
        target.setCtNum(source.getCtNum());
        target.setIce(source.getIce());
        target.setDescription(source.getDescription());
        target.setPartnerType(source.getPartnerType());
        target.setTelephone(source.getTelephone());
        target.setEmail(source.getEmail());
        target.setAddress(source.getAddress());
        target.setCodePostal(source.getCodePostal());
        target.setVille(source.getVille());
        target.setCountry(source.getCountry());
        target.setCategoryTarifId(source.getCategoryTarifId());
        target.setCreditLimit(source.getCreditLimit());
        target.setCurrentCredit(source.getCurrentCredit());
        target.setPaymentTermDays(source.getPaymentTermDays());
        target.setCreditRating(source.getCreditRating());
        target.setCreditScore(source.getCreditScore());
        target.setPaymentHistory(source.getPaymentHistory());
        target.setOutstandingBalance(source.getOutstandingBalance());
        target.setLastPaymentDate(source.getLastPaymentDate());
        target.setPreferredPaymentMethod(source.getPreferredPaymentMethod());
        target.setBankAccountInfo(source.getBankAccountInfo());
        target.setVip(source.isVip());
        target.setLoyaltyPoints(source.getLoyaltyPoints());
        target.setLastOrderDate(source.getLastOrderDate());
        target.setTotalOrders(source.getTotalOrders());
        target.setTotalSpent(source.getTotalSpent());
        target.setAverageOrderValue(source.getAverageOrderValue());
        target.setCustomerSince(source.getCustomerSince());
        target.setPreferredDeliveryTime(source.getPreferredDeliveryTime());
        target.setPreferredDeliveryDays(source.getPreferredDeliveryDays());
        target.setSpecialHandlingInstructions(source.getSpecialHandlingInstructions());
        target.setNotes(source.getNotes());
        target.setActive(source.isActive());
        target.setLastActivityDate(source.getLastActivityDate());
        target.setCreatedBy(source.getCreatedBy());
        target.setUpdatedBy(source.getUpdatedBy());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }
    
    /**
     * Copies common fields from PartnerDTO to B2CPartnerDTO.
     */
    private void copyCommonFields(PartnerDTO source, B2CPartnerDTO target) {
        target.setId(source.getId());
        target.setCtNum(source.getCtNum());
        target.setIce(source.getIce());
        target.setDescription(source.getDescription());
        target.setPartnerType(source.getPartnerType());
        target.setTelephone(source.getTelephone());
        target.setEmail(source.getEmail());
        target.setAddress(source.getAddress());
        target.setCodePostal(source.getCodePostal());
        target.setVille(source.getVille());
        target.setCountry(source.getCountry());
        target.setCategoryTarifId(source.getCategoryTarifId());
        target.setCreditLimit(source.getCreditLimit());
        target.setCurrentCredit(source.getCurrentCredit());
        target.setPaymentTermDays(source.getPaymentTermDays());
        target.setCreditRating(source.getCreditRating());
        target.setCreditScore(source.getCreditScore());
        target.setPaymentHistory(source.getPaymentHistory());
        target.setOutstandingBalance(source.getOutstandingBalance());
        target.setLastPaymentDate(source.getLastPaymentDate());
        target.setPreferredPaymentMethod(source.getPreferredPaymentMethod());
        target.setBankAccountInfo(source.getBankAccountInfo());
        target.setVip(source.isVip());
        target.setLoyaltyPoints(source.getLoyaltyPoints());
        target.setLastOrderDate(source.getLastOrderDate());
        target.setTotalOrders(source.getTotalOrders());
        target.setTotalSpent(source.getTotalSpent());
        target.setAverageOrderValue(source.getAverageOrderValue());
        target.setCustomerSince(source.getCustomerSince());
        target.setPreferredDeliveryTime(source.getPreferredDeliveryTime());
        target.setPreferredDeliveryDays(source.getPreferredDeliveryDays());
        target.setSpecialHandlingInstructions(source.getSpecialHandlingInstructions());
        target.setNotes(source.getNotes());
        target.setActive(source.isActive());
        target.setLastActivityDate(source.getLastActivityDate());
        target.setCreatedBy(source.getCreatedBy());
        target.setUpdatedBy(source.getUpdatedBy());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }
    
    /**
     * Copies B2B-specific fields from PartnerDTO to B2BPartnerDTO.
     */
    private void copyB2BFields(PartnerDTO source, B2BPartnerDTO target) {
        target.setCompanyName(source.getCompanyName());
        target.setLegalForm(source.getLegalForm());
        target.setRegistrationNumber(source.getRegistrationNumber());
        target.setTaxId(source.getTaxId());
        target.setVatNumber(source.getVatNumber());
        target.setBusinessActivity(source.getBusinessActivity());
        target.setAnnualTurnover(source.getAnnualTurnover());
        target.setNumberOfEmployees(source.getNumberOfEmployees());
        target.setContractNumber(source.getContractNumber());
        target.setContractStartDate(source.getContractStartDate());
        target.setContractEndDate(source.getContractEndDate());
        target.setContractType(source.getContractType());
        target.setContractTerms(source.getContractTerms());
        target.setPaymentTerms(source.getPaymentTerms());
        target.setDeliveryTerms(source.getDeliveryTerms());
        target.setSpecialConditions(source.getSpecialConditions());
    }
    
    /**
     * Copies B2C-specific fields from PartnerDTO to B2CPartnerDTO.
     */
    private void copyB2CFields(PartnerDTO source, B2CPartnerDTO target) {
        target.setPersonalIdNumber(source.getPersonalIdNumber());
        target.setDateOfBirth(source.getDateOfBirth());
        target.setPreferredLanguage(source.getPreferredLanguage());
        target.setMarketingConsent(source.getMarketingConsent());
    }
} 