package ma.foodplus.ordering.system.partner.mapper;

import ma.foodplus.ordering.system.partner.domain.B2BPartner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.dto.B2BPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manual mapper for B2B Partner entities and DTOs.
 * 
 * <p>This mapper handles the conversion between B2BPartner entities and B2BPartnerDTO objects,
 * including all B2B-specific fields such as company information and contract details.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Component
public class B2BPartnerMapper {
    
    // ========== B2B-Specific Mappings ==========
    
    public B2BPartnerDTO toDTO(B2BPartner b2bPartner) {
        if (b2bPartner == null) return null;
        
        B2BPartnerDTO dto = new B2BPartnerDTO();
        
        // Basic partner information
        dto.setId(b2bPartner.getId());
        dto.setCtNum(b2bPartner.getCtNum());
        dto.setIce(b2bPartner.getIce());
        dto.setDescription(b2bPartner.getDescription());
        dto.setPartnerType(b2bPartner.getPartnerType());
        dto.setCategoryTarifId(b2bPartner.getCateTarif());
        
        // Company Information (Individual columns)
        dto.setCompanyName(b2bPartner.getCompanyName());
        dto.setLegalForm(b2bPartner.getLegalForm());
        dto.setRegistrationNumber(b2bPartner.getRegistrationNumber());
        dto.setTaxId(b2bPartner.getTaxId());
        dto.setVatNumber(b2bPartner.getVatNumber());
        dto.setBusinessActivity(b2bPartner.getBusinessActivity());
        dto.setAnnualTurnover(b2bPartner.getAnnualTurnover());
        dto.setNumberOfEmployees(b2bPartner.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        dto.setContractNumber(b2bPartner.getContractNumber());
        dto.setContractStartDate(b2bPartner.getContractStartDate());
        dto.setContractEndDate(b2bPartner.getContractEndDate());
        dto.setContractType(b2bPartner.getContractType());
        dto.setContractTerms(b2bPartner.getContractTerms());
        dto.setPaymentTerms(b2bPartner.getPaymentTerms());
        dto.setDeliveryTerms(b2bPartner.getDeliveryTerms());
        dto.setSpecialConditions(b2bPartner.getSpecialConditions());
        
        // Contact Information (Individual columns)
        dto.setTelephone(b2bPartner.getTelephone());
        dto.setEmail(b2bPartner.getEmail());
        dto.setAddress(b2bPartner.getAddress());
        dto.setCodePostal(b2bPartner.getPostalCode());
        dto.setVille(b2bPartner.getCity());
        dto.setCountry(b2bPartner.getCountry());
        
        // Credit Information (Individual columns)
        dto.setCreditLimit(b2bPartner.getCreditLimit());
        dto.setOutstandingBalance(b2bPartner.getOutstandingBalance());
        dto.setCreditRating(b2bPartner.getCreditRating());
        dto.setCreditScore(b2bPartner.getCreditScore());
        dto.setPaymentTermDays(b2bPartner.getPaymentTermDays());
        dto.setPreferredPaymentMethod(b2bPartner.getPreferredPaymentMethod());
        dto.setBankAccountInfo(b2bPartner.getBankAccountInfo());
        dto.setLastPaymentDate(b2bPartner.getLastPaymentDate());
        dto.setPaymentHistory(b2bPartner.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        dto.setVip(b2bPartner.getIsVip() != null && b2bPartner.getIsVip());
        dto.setLoyaltyPoints(b2bPartner.getLoyaltyPoints());
        dto.setLastOrderDate(b2bPartner.getLastOrderDate());
        dto.setTotalOrders(b2bPartner.getTotalOrders());
        dto.setTotalSpent(b2bPartner.getTotalSpent());
        dto.setAverageOrderValue(b2bPartner.getAverageOrderValue());
        dto.setCustomerSince(b2bPartner.getPartnerSince());
        
        // Delivery Preference (Individual columns)
        dto.setPreferredDeliveryTime(b2bPartner.getPreferredDeliveryTime());
        dto.setPreferredDeliveryDays(b2bPartner.getPreferredDeliveryDays());
        dto.setSpecialHandlingInstructions(b2bPartner.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        dto.setNotes(b2bPartner.getNotes());
        dto.setActive(b2bPartner.getIsActive() != null && b2bPartner.getIsActive());
        dto.setLastActivityDate(b2bPartner.getLastActivityDate());
        dto.setCreatedBy(b2bPartner.getCreatedBy());
        dto.setUpdatedBy(b2bPartner.getUpdatedBy());
        dto.setCreatedAt(b2bPartner.getCreatedAt());
        dto.setUpdatedAt(b2bPartner.getUpdatedAt());
        
        return dto;
    }
    
    public B2BPartner toEntity(B2BPartnerDTO dto) {
        if (dto == null) return null;
        
        B2BPartner b2bPartner = new B2BPartner();
        
        // Basic partner information
        b2bPartner.setId(dto.getId());
        b2bPartner.setCtNum(dto.getCtNum());
        b2bPartner.setIce(dto.getIce());
        b2bPartner.setDescription(dto.getDescription());
        b2bPartner.setCateTarif(dto.getCategoryTarifId());
        
        // Company Information (Individual columns)
        b2bPartner.setCompanyName(dto.getCompanyName());
        b2bPartner.setLegalForm(dto.getLegalForm());
        b2bPartner.setRegistrationNumber(dto.getRegistrationNumber());
        b2bPartner.setTaxId(dto.getTaxId());
        b2bPartner.setVatNumber(dto.getVatNumber());
        b2bPartner.setBusinessActivity(dto.getBusinessActivity());
        b2bPartner.setAnnualTurnover(dto.getAnnualTurnover());
        b2bPartner.setNumberOfEmployees(dto.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        b2bPartner.setContractNumber(dto.getContractNumber());
        b2bPartner.setContractStartDate(dto.getContractStartDate());
        b2bPartner.setContractEndDate(dto.getContractEndDate());
        b2bPartner.setContractType(dto.getContractType());
        b2bPartner.setContractTerms(dto.getContractTerms());
        b2bPartner.setPaymentTerms(dto.getPaymentTerms());
        b2bPartner.setDeliveryTerms(dto.getDeliveryTerms());
        b2bPartner.setSpecialConditions(dto.getSpecialConditions());
        
        // Contact Information (Individual columns)
        b2bPartner.setTelephone(dto.getTelephone());
        b2bPartner.setEmail(dto.getEmail());
        b2bPartner.setAddress(dto.getAddress());
        b2bPartner.setCity(dto.getVille());
        b2bPartner.setCountry(dto.getCountry());
        b2bPartner.setPostalCode(dto.getCodePostal());
        
        // Credit Information (Individual columns)
        b2bPartner.setCreditLimit(dto.getCreditLimit());
        b2bPartner.setOutstandingBalance(dto.getOutstandingBalance());
        b2bPartner.setCreditRating(dto.getCreditRating());
        b2bPartner.setCreditScore(dto.getCreditScore());
        b2bPartner.setPaymentTermDays(dto.getPaymentTermDays());
        b2bPartner.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        b2bPartner.setBankAccountInfo(dto.getBankAccountInfo());
        b2bPartner.setLastPaymentDate(dto.getLastPaymentDate());
        b2bPartner.setPaymentHistory(dto.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        b2bPartner.setIsVip(dto.isVip());
        b2bPartner.setLoyaltyPoints(dto.getLoyaltyPoints());
        b2bPartner.setLastOrderDate(dto.getLastOrderDate());
        b2bPartner.setTotalOrders(dto.getTotalOrders());
        b2bPartner.setTotalSpent(dto.getTotalSpent());
        b2bPartner.setAverageOrderValue(dto.getAverageOrderValue());
        b2bPartner.setPartnerSince(dto.getCustomerSince());
        
        // Delivery Preference (Individual columns)
        b2bPartner.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        b2bPartner.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        b2bPartner.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        b2bPartner.setNotes(dto.getNotes());
        b2bPartner.setIsActive(dto.isActive());
        b2bPartner.setLastActivityDate(dto.getLastActivityDate());
        b2bPartner.setCreatedBy(dto.getCreatedBy());
        b2bPartner.setUpdatedBy(dto.getUpdatedBy());
        b2bPartner.setCreatedAt(dto.getCreatedAt());
        b2bPartner.setUpdatedAt(dto.getUpdatedAt());
        
        return b2bPartner;
    }
    
    public void updateEntityFromDTO(B2BPartnerDTO dto, B2BPartner b2bPartner) {
        if (dto == null || b2bPartner == null) return;
        
        // Update basic partner information
        b2bPartner.setCtNum(dto.getCtNum());
        b2bPartner.setIce(dto.getIce());
        b2bPartner.setDescription(dto.getDescription());
        b2bPartner.setCateTarif(dto.getCategoryTarifId());
        
        // Update Company Information (Individual columns)
        b2bPartner.setCompanyName(dto.getCompanyName());
        b2bPartner.setLegalForm(dto.getLegalForm());
        b2bPartner.setRegistrationNumber(dto.getRegistrationNumber());
        b2bPartner.setTaxId(dto.getTaxId());
        b2bPartner.setVatNumber(dto.getVatNumber());
        b2bPartner.setBusinessActivity(dto.getBusinessActivity());
        b2bPartner.setAnnualTurnover(dto.getAnnualTurnover());
        b2bPartner.setNumberOfEmployees(dto.getNumberOfEmployees());
        
        // Update Contract Information (Individual columns)
        b2bPartner.setContractNumber(dto.getContractNumber());
        b2bPartner.setContractStartDate(dto.getContractStartDate());
        b2bPartner.setContractEndDate(dto.getContractEndDate());
        b2bPartner.setContractType(dto.getContractType());
        b2bPartner.setContractTerms(dto.getContractTerms());
        b2bPartner.setPaymentTerms(dto.getPaymentTerms());
        b2bPartner.setDeliveryTerms(dto.getDeliveryTerms());
        b2bPartner.setSpecialConditions(dto.getSpecialConditions());
        
        // Update Contact Information (Individual columns)
        b2bPartner.setTelephone(dto.getTelephone());
        b2bPartner.setEmail(dto.getEmail());
        b2bPartner.setAddress(dto.getAddress());
        b2bPartner.setCity(dto.getVille());
        b2bPartner.setCountry(dto.getCountry());
        b2bPartner.setPostalCode(dto.getCodePostal());
        
        // Update Credit Information (Individual columns)
        b2bPartner.setCreditLimit(dto.getCreditLimit());
        b2bPartner.setOutstandingBalance(dto.getOutstandingBalance());
        b2bPartner.setCreditRating(dto.getCreditRating());
        b2bPartner.setCreditScore(dto.getCreditScore());
        b2bPartner.setPaymentTermDays(dto.getPaymentTermDays());
        b2bPartner.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        b2bPartner.setBankAccountInfo(dto.getBankAccountInfo());
        b2bPartner.setLastPaymentDate(dto.getLastPaymentDate());
        b2bPartner.setPaymentHistory(dto.getPaymentHistory());
        
        // Update Loyalty Information (Individual columns)
        b2bPartner.setIsVip(dto.isVip());
        b2bPartner.setLoyaltyPoints(dto.getLoyaltyPoints());
        b2bPartner.setLastOrderDate(dto.getLastOrderDate());
        b2bPartner.setTotalOrders(dto.getTotalOrders());
        b2bPartner.setTotalSpent(dto.getTotalSpent());
        b2bPartner.setAverageOrderValue(dto.getAverageOrderValue());
        b2bPartner.setPartnerSince(dto.getCustomerSince());
        
        // Update Delivery Preference (Individual columns)
        b2bPartner.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        b2bPartner.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        b2bPartner.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Update Audit Information (Individual columns)
        b2bPartner.setNotes(dto.getNotes());
        b2bPartner.setIsActive(dto.isActive());
        b2bPartner.setLastActivityDate(dto.getLastActivityDate());
        b2bPartner.setCreatedBy(dto.getCreatedBy());
        b2bPartner.setUpdatedBy(dto.getUpdatedBy());
        b2bPartner.setCreatedAt(dto.getCreatedAt());
        b2bPartner.setUpdatedAt(dto.getUpdatedAt());
    }
    
    public List<B2BPartnerDTO> toDTOList(List<B2BPartner> b2bPartners) {
        if (b2bPartners == null) return null;
        List<B2BPartnerDTO> list = new ArrayList<>();
        for (B2BPartner p : b2bPartners) list.add(toDTO(p));
        return list;
    }
    
    // ========== Compatibility with Generic PartnerDTO ==========
    
    /**
     * Maps B2BPartner to generic PartnerDTO for backward compatibility.
     */
    public PartnerDTO toPartnerDTO(B2BPartner b2bPartner) {
        if (b2bPartner == null) return null;
        
        PartnerDTO dto = new PartnerDTO();
        
        // Basic partner information
        dto.setId(b2bPartner.getId());
        dto.setCtNum(b2bPartner.getCtNum());
        dto.setIce(b2bPartner.getIce());
        dto.setDescription(b2bPartner.getDescription());
        dto.setPartnerType(b2bPartner.getPartnerType());
        dto.setCategoryTarifId(b2bPartner.getCateTarif());
        
        // Company Information (Individual columns)
        dto.setCompanyName(b2bPartner.getCompanyName());
        dto.setLegalForm(b2bPartner.getLegalForm());
        dto.setRegistrationNumber(b2bPartner.getRegistrationNumber());
        dto.setTaxId(b2bPartner.getTaxId());
        dto.setVatNumber(b2bPartner.getVatNumber());
        dto.setBusinessActivity(b2bPartner.getBusinessActivity());
        dto.setAnnualTurnover(b2bPartner.getAnnualTurnover());
        dto.setNumberOfEmployees(b2bPartner.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        dto.setContractNumber(b2bPartner.getContractNumber());
        dto.setContractStartDate(b2bPartner.getContractStartDate());
        dto.setContractEndDate(b2bPartner.getContractEndDate());
        dto.setContractType(b2bPartner.getContractType());
        dto.setContractTerms(b2bPartner.getContractTerms());
        dto.setPaymentTerms(b2bPartner.getPaymentTerms());
        dto.setDeliveryTerms(b2bPartner.getDeliveryTerms());
        dto.setSpecialConditions(b2bPartner.getSpecialConditions());
        
        // Contact Information (Individual columns)
        dto.setTelephone(b2bPartner.getTelephone());
        dto.setEmail(b2bPartner.getEmail());
        dto.setAddress(b2bPartner.getAddress());
        dto.setCodePostal(b2bPartner.getPostalCode());
        dto.setVille(b2bPartner.getCity());
        dto.setCountry(b2bPartner.getCountry());
        
        // Credit Information (Individual columns)
        dto.setCreditLimit(b2bPartner.getCreditLimit());
        dto.setOutstandingBalance(b2bPartner.getOutstandingBalance());
        dto.setCreditRating(b2bPartner.getCreditRating());
        dto.setCreditScore(b2bPartner.getCreditScore());
        dto.setPaymentTermDays(b2bPartner.getPaymentTermDays());
        dto.setPreferredPaymentMethod(b2bPartner.getPreferredPaymentMethod());
        dto.setBankAccountInfo(b2bPartner.getBankAccountInfo());
        dto.setLastPaymentDate(b2bPartner.getLastPaymentDate());
        dto.setPaymentHistory(b2bPartner.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        dto.setVip(b2bPartner.getIsVip() != null && b2bPartner.getIsVip());
        dto.setLoyaltyPoints(b2bPartner.getLoyaltyPoints());
        dto.setLastOrderDate(b2bPartner.getLastOrderDate());
        dto.setTotalOrders(b2bPartner.getTotalOrders());
        dto.setTotalSpent(b2bPartner.getTotalSpent());
        dto.setAverageOrderValue(b2bPartner.getAverageOrderValue());
        dto.setCustomerSince(b2bPartner.getPartnerSince());
        
        // Delivery Preference (Individual columns)
        dto.setPreferredDeliveryTime(b2bPartner.getPreferredDeliveryTime());
        dto.setPreferredDeliveryDays(b2bPartner.getPreferredDeliveryDays());
        dto.setSpecialHandlingInstructions(b2bPartner.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        dto.setNotes(b2bPartner.getNotes());
        dto.setActive(b2bPartner.getIsActive() != null && b2bPartner.getIsActive());
        dto.setLastActivityDate(b2bPartner.getLastActivityDate());
        dto.setCreatedBy(b2bPartner.getCreatedBy());
        dto.setUpdatedBy(b2bPartner.getUpdatedBy());
        dto.setCreatedAt(b2bPartner.getCreatedAt());
        dto.setUpdatedAt(b2bPartner.getUpdatedAt());
        
        return dto;
    }
} 