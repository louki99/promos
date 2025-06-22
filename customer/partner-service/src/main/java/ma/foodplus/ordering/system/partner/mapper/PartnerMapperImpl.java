package ma.foodplus.ordering.system.partner.mapper;

import ma.foodplus.ordering.system.partner.domain.*;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class PartnerMapperImpl {
    
    /**
     * Creates a partner entity based on the partner type.
     * 
     * @param partnerType the type of partner to create
     * @return the appropriate partner entity
     */
    private Partner createPartnerByType(PartnerType partnerType) {
        if (partnerType == null) {
            return new B2BPartner(); // Default fallback
        }
        
        switch (partnerType) {
            case B2B:
                return new B2BPartner();
            case B2C:
                return new B2CPartner();
            case SUPPLIER:
                return new SupplierPartner();
            default:
                return new B2BPartner(); // Default fallback
        }
    }
    
    public PartnerDTO toDTO(Partner partner) {
        if (partner == null) return null;
        PartnerDTO dto = new PartnerDTO();
        dto.setId(partner.getId());
        dto.setCtNum(partner.getCtNum());
        dto.setIce(partner.getIce());
        dto.setDescription(partner.getDescription());
        dto.setPartnerType(partner.getPartnerType());
        dto.setCategoryTarifId(partner.getCateTarif());
        
        // Contact Information (Individual columns)
        dto.setTelephone(partner.getTelephone());
        dto.setEmail(partner.getEmail());
        dto.setAddress(partner.getAddress());
        dto.setCodePostal(partner.getPostalCode());
        dto.setVille(partner.getCity());
        dto.setCountry(partner.getCountry());
        
        // Credit Information (Individual columns)
        dto.setCreditLimit(partner.getCreditLimit());
        dto.setOutstandingBalance(partner.getOutstandingBalance());
        dto.setCreditRating(partner.getCreditRating());
        dto.setCreditScore(partner.getCreditScore());
        dto.setPaymentTermDays(partner.getPaymentTermDays());
        dto.setPreferredPaymentMethod(partner.getPreferredPaymentMethod());
        dto.setBankAccountInfo(partner.getBankAccountInfo());
        dto.setLastPaymentDate(partner.getLastPaymentDate());
        dto.setPaymentHistory(partner.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        dto.setVip(partner.getIsVip() != null && partner.getIsVip());
        dto.setLoyaltyPoints(partner.getLoyaltyPoints());
        dto.setLastOrderDate(partner.getLastOrderDate());
        dto.setTotalOrders(partner.getTotalOrders());
        dto.setTotalSpent(partner.getTotalSpent());
        dto.setAverageOrderValue(partner.getAverageOrderValue());
        dto.setCustomerSince(partner.getPartnerSince());
        
        // Delivery Preference (Individual columns)
        dto.setPreferredDeliveryTime(partner.getPreferredDeliveryTime());
        dto.setPreferredDeliveryDays(partner.getPreferredDeliveryDays());
        dto.setSpecialHandlingInstructions(partner.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        dto.setNotes(partner.getNotes());
        dto.setActive(partner.getIsActive() != null && partner.getIsActive());
        dto.setLastActivityDate(partner.getLastActivityDate());
        dto.setCreatedBy(partner.getCreatedBy());
        dto.setUpdatedBy(partner.getUpdatedBy());
        dto.setCreatedAt(partner.getCreatedAt());
        dto.setUpdatedAt(partner.getUpdatedAt());
        
        // Company Information (Individual columns)
        dto.setCompanyName(partner.getCompanyName());
        dto.setLegalForm(partner.getLegalForm());
        dto.setRegistrationNumber(partner.getRegistrationNumber());
        dto.setTaxId(partner.getTaxId());
        dto.setVatNumber(partner.getVatNumber());
        dto.setBusinessActivity(partner.getBusinessActivity());
        dto.setAnnualTurnover(partner.getAnnualTurnover());
        dto.setNumberOfEmployees(partner.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        dto.setContractNumber(partner.getContractNumber());
        dto.setContractStartDate(partner.getContractStartDate());
        dto.setContractEndDate(partner.getContractEndDate());
        dto.setContractType(partner.getContractType());
        dto.setContractTerms(partner.getContractTerms());
        dto.setPaymentTerms(partner.getPaymentTerms());
        dto.setDeliveryTerms(partner.getDeliveryTerms());
        dto.setSpecialConditions(partner.getSpecialConditions());
        
        // Handle B2C-specific fields
        if (partner instanceof B2CPartner) {
            B2CPartner b2cPartner = (B2CPartner) partner;
            dto.setPersonalIdNumber(b2cPartner.getPersonalIdNumber());
            dto.setDateOfBirth(b2cPartner.getDateOfBirth());
            dto.setPreferredLanguage(b2cPartner.getPreferredLanguage());
            dto.setMarketingConsent(b2cPartner.getMarketingConsent());
        }
        
        // Note: Supplier-specific fields are handled by SupplierPartnerMapper
        // This generic mapper only handles common fields and B2B/B2C specific fields
        
        return dto;
    }

    public Partner toEntity(PartnerDTO dto) {
        if (dto == null) return null;
        
        // Create the appropriate partner type
        Partner partner = createPartnerByType(dto.getPartnerType());
        
        partner.setId(dto.getId());
        partner.setCtNum(dto.getCtNum());
        partner.setIce(dto.getIce());
        partner.setDescription(dto.getDescription());
        partner.setCateTarif(dto.getCategoryTarifId());
        
        // Contact Information (Individual columns)
        partner.setTelephone(dto.getTelephone());
        partner.setEmail(dto.getEmail());
        partner.setAddress(dto.getAddress());
        partner.setPostalCode(dto.getCodePostal());
        partner.setCity(dto.getVille());
        partner.setCountry(dto.getCountry());
        
        // Credit Information (Individual columns)
        partner.setCreditLimit(dto.getCreditLimit());
        partner.setOutstandingBalance(dto.getOutstandingBalance());
        partner.setCreditRating(dto.getCreditRating());
        partner.setCreditScore(dto.getCreditScore());
        partner.setPaymentTermDays(dto.getPaymentTermDays());
        partner.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        partner.setBankAccountInfo(dto.getBankAccountInfo());
        partner.setLastPaymentDate(dto.getLastPaymentDate());
        partner.setPaymentHistory(dto.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        partner.setIsVip(dto.isVip());
        partner.setLoyaltyPoints(dto.getLoyaltyPoints());
        partner.setLastOrderDate(dto.getLastOrderDate());
        partner.setTotalOrders(dto.getTotalOrders());
        partner.setTotalSpent(dto.getTotalSpent());
        partner.setAverageOrderValue(dto.getAverageOrderValue());
        partner.setPartnerSince(dto.getCustomerSince());
        
        // Delivery Preference (Individual columns)
        partner.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        partner.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        partner.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        partner.setNotes(dto.getNotes());
        partner.setIsActive(dto.isActive());
        partner.setLastActivityDate(dto.getLastActivityDate());
        partner.setCreatedBy(dto.getCreatedBy());
        partner.setUpdatedBy(dto.getUpdatedBy());
        partner.setCreatedAt(dto.getCreatedAt());
        partner.setUpdatedAt(dto.getUpdatedAt());
        
        // Company Information (Individual columns)
        partner.setCompanyName(dto.getCompanyName());
        partner.setLegalForm(dto.getLegalForm());
        partner.setRegistrationNumber(dto.getRegistrationNumber());
        partner.setTaxId(dto.getTaxId());
        partner.setVatNumber(dto.getVatNumber());
        partner.setBusinessActivity(dto.getBusinessActivity());
        partner.setAnnualTurnover(dto.getAnnualTurnover());
        partner.setNumberOfEmployees(dto.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        partner.setContractNumber(dto.getContractNumber());
        partner.setContractStartDate(dto.getContractStartDate());
        partner.setContractEndDate(dto.getContractEndDate());
        partner.setContractType(dto.getContractType());
        partner.setContractTerms(dto.getContractTerms());
        partner.setPaymentTerms(dto.getPaymentTerms());
        partner.setDeliveryTerms(dto.getDeliveryTerms());
        partner.setSpecialConditions(dto.getSpecialConditions());
        
        // Handle B2C-specific fields
        if (partner instanceof B2CPartner) {
            B2CPartner b2cPartner = (B2CPartner) partner;
            b2cPartner.setPersonalIdNumber(dto.getPersonalIdNumber());
            b2cPartner.setDateOfBirth(dto.getDateOfBirth());
            b2cPartner.setPreferredLanguage(dto.getPreferredLanguage());
            b2cPartner.setMarketingConsent(dto.getMarketingConsent());
        }
        
        // Note: Supplier-specific fields should be handled by SupplierPartnerMapper
        // This generic mapper only handles common fields and B2B/B2C specific fields
        
        return partner;
    }

    public void updateEntityFromDTO(PartnerDTO dto, Partner partner) {
        if (dto == null || partner == null) return;
        
        partner.setCtNum(dto.getCtNum());
        partner.setIce(dto.getIce());
        partner.setDescription(dto.getDescription());
        partner.setCateTarif(dto.getCategoryTarifId());
        
        // Contact Information (Individual columns)
        partner.setTelephone(dto.getTelephone());
        partner.setEmail(dto.getEmail());
        partner.setAddress(dto.getAddress());
        partner.setPostalCode(dto.getCodePostal());
        partner.setCity(dto.getVille());
        partner.setCountry(dto.getCountry());
        
        // Credit Information (Individual columns)
        partner.setCreditLimit(dto.getCreditLimit());
        partner.setOutstandingBalance(dto.getOutstandingBalance());
        partner.setCreditRating(dto.getCreditRating());
        partner.setCreditScore(dto.getCreditScore());
        partner.setPaymentTermDays(dto.getPaymentTermDays());
        partner.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        partner.setBankAccountInfo(dto.getBankAccountInfo());
        partner.setLastPaymentDate(dto.getLastPaymentDate());
        partner.setPaymentHistory(dto.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        partner.setIsVip(dto.isVip());
        partner.setLoyaltyPoints(dto.getLoyaltyPoints());
        partner.setLastOrderDate(dto.getLastOrderDate());
        partner.setTotalOrders(dto.getTotalOrders());
        partner.setTotalSpent(dto.getTotalSpent());
        partner.setAverageOrderValue(dto.getAverageOrderValue());
        partner.setPartnerSince(dto.getCustomerSince());
        
        // Delivery Preference (Individual columns)
        partner.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        partner.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        partner.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        partner.setNotes(dto.getNotes());
        partner.setIsActive(dto.isActive());
        partner.setLastActivityDate(dto.getLastActivityDate());
        partner.setCreatedBy(dto.getCreatedBy());
        partner.setUpdatedBy(dto.getUpdatedBy());
        partner.setCreatedAt(dto.getCreatedAt());
        partner.setUpdatedAt(dto.getUpdatedAt());
        
        // Company Information (Individual columns)
        partner.setCompanyName(dto.getCompanyName());
        partner.setLegalForm(dto.getLegalForm());
        partner.setRegistrationNumber(dto.getRegistrationNumber());
        partner.setTaxId(dto.getTaxId());
        partner.setVatNumber(dto.getVatNumber());
        partner.setBusinessActivity(dto.getBusinessActivity());
        partner.setAnnualTurnover(dto.getAnnualTurnover());
        partner.setNumberOfEmployees(dto.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        partner.setContractNumber(dto.getContractNumber());
        partner.setContractStartDate(dto.getContractStartDate());
        partner.setContractEndDate(dto.getContractEndDate());
        partner.setContractType(dto.getContractType());
        partner.setContractTerms(dto.getContractTerms());
        partner.setPaymentTerms(dto.getPaymentTerms());
        partner.setDeliveryTerms(dto.getDeliveryTerms());
        partner.setSpecialConditions(dto.getSpecialConditions());
        
        // Handle B2C-specific fields
        if (partner instanceof B2CPartner) {
            B2CPartner b2cPartner = (B2CPartner) partner;
            b2cPartner.setPersonalIdNumber(dto.getPersonalIdNumber());
            b2cPartner.setDateOfBirth(dto.getDateOfBirth());
            b2cPartner.setPreferredLanguage(dto.getPreferredLanguage());
            b2cPartner.setMarketingConsent(dto.getMarketingConsent());
        }
        
        // Note: Supplier-specific fields should be handled by SupplierPartnerMapper
        // This generic mapper only handles common fields and B2B/B2C specific fields
    }

    public List<PartnerDTO> toDTOList(List<Partner> partners) {
        if (partners == null) return null;
        List<PartnerDTO> list = new ArrayList<>();
        for (Partner p : partners) list.add(toDTO(p));
        return list;
    }

    public List<Partner> toEntityList(List<PartnerDTO> dtos) {
        if (dtos == null) return null;
        List<Partner> list = new ArrayList<>();
        for (PartnerDTO dto : dtos) list.add(toEntity(dto));
        return list;
    }
} 