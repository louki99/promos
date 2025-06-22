package ma.foodplus.ordering.system.partner.mapper;

import ma.foodplus.ordering.system.partner.domain.SupplierPartner;
import ma.foodplus.ordering.system.partner.dto.SupplierPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manual mapper for Supplier Partner entities and DTOs.
 * 
 * <p>This mapper handles the conversion between SupplierPartner entities and SupplierPartnerDTO objects,
 * including all supplier-specific fields such as performance scores, audit information, and risk assessment.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Component
public class SupplierPartnerMapper {
    
    // ========== Supplier-Specific Mappings ==========
    
    public SupplierPartnerDTO toDTO(SupplierPartner supplierPartner) {
        if (supplierPartner == null) return null;
        
        SupplierPartnerDTO dto = new SupplierPartnerDTO();
        
        // Basic partner information
        dto.setId(supplierPartner.getId());
        dto.setCtNum(supplierPartner.getCtNum());
        dto.setIce(supplierPartner.getIce());
        dto.setDescription(supplierPartner.getDescription());
        dto.setPartnerType(supplierPartner.getPartnerType());
        dto.setCategoryTarifId(supplierPartner.getCateTarif());
        
        // Supplier-specific fields
        dto.setSupplierCode(supplierPartner.getSupplierCode());
        dto.setSupplierCategory(supplierPartner.getSupplierCategory());
        dto.setSupplierRating(supplierPartner.getSupplierRating());
        dto.setDeliveryPerformanceScore(supplierPartner.getDeliveryPerformanceScore());
        dto.setQualityScore(supplierPartner.getQualityScore());
        dto.setPriceCompetitivenessScore(supplierPartner.getPriceCompetitivenessScore());
        dto.setPaymentTermsDays(supplierPartner.getPaymentTermsDays());
        dto.setMinimumOrderAmount(supplierPartner.getMinimumOrderAmount());
        dto.setLeadTimeDays(supplierPartner.getLeadTimeDays());
        dto.setCertificationIso(supplierPartner.getCertificationIso());
        dto.setSupplierSince(supplierPartner.getSupplierSince());
        dto.setLastAuditDate(supplierPartner.getLastAuditDate());
        dto.setNextAuditDate(supplierPartner.getNextAuditDate());
        dto.setSupplierStatus(supplierPartner.getSupplierStatus());
        dto.setRiskLevel(supplierPartner.getRiskLevel());
        dto.setSupplierNotes(supplierPartner.getSupplierNotes());
        
        // Company Information (Individual columns)
        dto.setCompanyName(supplierPartner.getCompanyName());
        dto.setLegalForm(supplierPartner.getLegalForm());
        dto.setRegistrationNumber(supplierPartner.getRegistrationNumber());
        dto.setTaxId(supplierPartner.getTaxId());
        dto.setVatNumber(supplierPartner.getVatNumber());
        dto.setBusinessActivity(supplierPartner.getBusinessActivity());
        dto.setAnnualTurnover(supplierPartner.getAnnualTurnover());
        dto.setNumberOfEmployees(supplierPartner.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        dto.setContractNumber(supplierPartner.getContractNumber());
        dto.setContractStartDate(supplierPartner.getContractStartDate());
        dto.setContractEndDate(supplierPartner.getContractEndDate());
        dto.setContractType(supplierPartner.getContractType());
        dto.setContractTerms(supplierPartner.getContractTerms());
        dto.setPaymentTerms(supplierPartner.getPaymentTerms());
        dto.setDeliveryTerms(supplierPartner.getDeliveryTerms());
        dto.setSpecialConditions(supplierPartner.getSpecialConditions());
        
        // Contact Information (Individual columns)
        dto.setTelephone(supplierPartner.getTelephone());
        dto.setEmail(supplierPartner.getEmail());
        dto.setAddress(supplierPartner.getAddress());
        dto.setCodePostal(supplierPartner.getPostalCode());
        dto.setVille(supplierPartner.getCity());
        dto.setCountry(supplierPartner.getCountry());
        
        // Credit Information (Individual columns)
        dto.setCreditLimit(supplierPartner.getCreditLimit());
        dto.setOutstandingBalance(supplierPartner.getOutstandingBalance());
        dto.setCreditRating(supplierPartner.getCreditRating());
        dto.setCreditScore(supplierPartner.getCreditScore());
        dto.setPaymentTermDays(supplierPartner.getPaymentTermDays());
        dto.setPreferredPaymentMethod(supplierPartner.getPreferredPaymentMethod());
        dto.setBankAccountInfo(supplierPartner.getBankAccountInfo());
        dto.setLastPaymentDate(supplierPartner.getLastPaymentDate());
        dto.setPaymentHistory(supplierPartner.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        dto.setVip(supplierPartner.getIsVip() != null && supplierPartner.getIsVip());
        dto.setLoyaltyPoints(supplierPartner.getLoyaltyPoints());
        dto.setLastOrderDate(supplierPartner.getLastOrderDate());
        dto.setTotalOrders(supplierPartner.getTotalOrders());
        dto.setTotalSpent(supplierPartner.getTotalSpent());
        dto.setAverageOrderValue(supplierPartner.getAverageOrderValue());
        dto.setCustomerSince(supplierPartner.getPartnerSince());
        
        // Delivery Preference (Individual columns)
        dto.setPreferredDeliveryTime(supplierPartner.getPreferredDeliveryTime());
        dto.setPreferredDeliveryDays(supplierPartner.getPreferredDeliveryDays());
        dto.setSpecialHandlingInstructions(supplierPartner.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        dto.setNotes(supplierPartner.getNotes());
        dto.setActive(supplierPartner.getIsActive() != null && supplierPartner.getIsActive());
        dto.setLastActivityDate(supplierPartner.getLastActivityDate());
        dto.setCreatedBy(supplierPartner.getCreatedBy());
        dto.setUpdatedBy(supplierPartner.getUpdatedBy());
        dto.setCreatedAt(supplierPartner.getCreatedAt());
        dto.setUpdatedAt(supplierPartner.getUpdatedAt());
        
        return dto;
    }

    public SupplierPartner toEntity(SupplierPartnerDTO dto) {
        if (dto == null) return null;
        
        SupplierPartner supplierPartner = new SupplierPartner();
        
        // Basic partner information
        supplierPartner.setId(dto.getId());
        supplierPartner.setCtNum(dto.getCtNum());
        supplierPartner.setIce(dto.getIce());
        supplierPartner.setDescription(dto.getDescription());
        supplierPartner.setCateTarif(dto.getCategoryTarifId());
        
        // Supplier-specific fields
        supplierPartner.setSupplierCode(dto.getSupplierCode());
        supplierPartner.setSupplierCategory(dto.getSupplierCategory());
        supplierPartner.setSupplierRating(dto.getSupplierRating());
        supplierPartner.setDeliveryPerformanceScore(dto.getDeliveryPerformanceScore());
        supplierPartner.setQualityScore(dto.getQualityScore());
        supplierPartner.setPriceCompetitivenessScore(dto.getPriceCompetitivenessScore());
        supplierPartner.setPaymentTermsDays(dto.getPaymentTermsDays());
        supplierPartner.setMinimumOrderAmount(dto.getMinimumOrderAmount());
        supplierPartner.setLeadTimeDays(dto.getLeadTimeDays());
        supplierPartner.setCertificationIso(dto.getCertificationIso());
        supplierPartner.setSupplierSince(dto.getSupplierSince());
        supplierPartner.setLastAuditDate(dto.getLastAuditDate());
        supplierPartner.setNextAuditDate(dto.getNextAuditDate());
        supplierPartner.setSupplierStatus(dto.getSupplierStatus());
        supplierPartner.setRiskLevel(dto.getRiskLevel());
        supplierPartner.setSupplierNotes(dto.getSupplierNotes());
        
        // Company Information (Individual columns)
        supplierPartner.setCompanyName(dto.getCompanyName());
        supplierPartner.setLegalForm(dto.getLegalForm());
        supplierPartner.setRegistrationNumber(dto.getRegistrationNumber());
        supplierPartner.setTaxId(dto.getTaxId());
        supplierPartner.setVatNumber(dto.getVatNumber());
        supplierPartner.setBusinessActivity(dto.getBusinessActivity());
        supplierPartner.setAnnualTurnover(dto.getAnnualTurnover());
        supplierPartner.setNumberOfEmployees(dto.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        supplierPartner.setContractNumber(dto.getContractNumber());
        supplierPartner.setContractStartDate(dto.getContractStartDate());
        supplierPartner.setContractEndDate(dto.getContractEndDate());
        supplierPartner.setContractType(dto.getContractType());
        supplierPartner.setContractTerms(dto.getContractTerms());
        supplierPartner.setPaymentTerms(dto.getPaymentTerms());
        supplierPartner.setDeliveryTerms(dto.getDeliveryTerms());
        supplierPartner.setSpecialConditions(dto.getSpecialConditions());
        
        // Contact Information (Individual columns)
        supplierPartner.setTelephone(dto.getTelephone());
        supplierPartner.setEmail(dto.getEmail());
        supplierPartner.setAddress(dto.getAddress());
        supplierPartner.setCity(dto.getVille());
        supplierPartner.setCountry(dto.getCountry());
        supplierPartner.setPostalCode(dto.getCodePostal());
        
        // Credit Information (Individual columns)
        supplierPartner.setCreditLimit(dto.getCreditLimit());
        supplierPartner.setOutstandingBalance(dto.getOutstandingBalance());
        supplierPartner.setCreditRating(dto.getCreditRating());
        supplierPartner.setCreditScore(dto.getCreditScore());
        supplierPartner.setPaymentTermDays(dto.getPaymentTermDays());
        supplierPartner.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        supplierPartner.setBankAccountInfo(dto.getBankAccountInfo());
        supplierPartner.setLastPaymentDate(dto.getLastPaymentDate());
        supplierPartner.setPaymentHistory(dto.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        supplierPartner.setIsVip(dto.isVip());
        supplierPartner.setLoyaltyPoints(dto.getLoyaltyPoints());
        supplierPartner.setLastOrderDate(dto.getLastOrderDate());
        supplierPartner.setTotalOrders(dto.getTotalOrders());
        supplierPartner.setTotalSpent(dto.getTotalSpent());
        supplierPartner.setAverageOrderValue(dto.getAverageOrderValue());
        supplierPartner.setPartnerSince(dto.getCustomerSince());
        
        // Delivery Preference (Individual columns)
        supplierPartner.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        supplierPartner.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        supplierPartner.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        supplierPartner.setNotes(dto.getNotes());
        supplierPartner.setIsActive(dto.isActive());
        supplierPartner.setLastActivityDate(dto.getLastActivityDate());
        supplierPartner.setCreatedBy(dto.getCreatedBy());
        supplierPartner.setUpdatedBy(dto.getUpdatedBy());
        supplierPartner.setCreatedAt(dto.getCreatedAt());
        supplierPartner.setUpdatedAt(dto.getUpdatedAt());
        
        return supplierPartner;
    }

    public void updateEntityFromDTO(SupplierPartnerDTO dto, SupplierPartner supplierPartner) {
        if (dto == null || supplierPartner == null) return;
        
        // Update basic partner information
        supplierPartner.setCtNum(dto.getCtNum());
        supplierPartner.setIce(dto.getIce());
        supplierPartner.setDescription(dto.getDescription());
        supplierPartner.setCateTarif(dto.getCategoryTarifId());
        
        // Update supplier-specific fields
        supplierPartner.setSupplierCode(dto.getSupplierCode());
        supplierPartner.setSupplierCategory(dto.getSupplierCategory());
        supplierPartner.setSupplierRating(dto.getSupplierRating());
        supplierPartner.setDeliveryPerformanceScore(dto.getDeliveryPerformanceScore());
        supplierPartner.setQualityScore(dto.getQualityScore());
        supplierPartner.setPriceCompetitivenessScore(dto.getPriceCompetitivenessScore());
        supplierPartner.setPaymentTermsDays(dto.getPaymentTermsDays());
        supplierPartner.setMinimumOrderAmount(dto.getMinimumOrderAmount());
        supplierPartner.setLeadTimeDays(dto.getLeadTimeDays());
        supplierPartner.setCertificationIso(dto.getCertificationIso());
        supplierPartner.setSupplierSince(dto.getSupplierSince());
        supplierPartner.setLastAuditDate(dto.getLastAuditDate());
        supplierPartner.setNextAuditDate(dto.getNextAuditDate());
        supplierPartner.setSupplierStatus(dto.getSupplierStatus());
        supplierPartner.setRiskLevel(dto.getRiskLevel());
        supplierPartner.setSupplierNotes(dto.getSupplierNotes());
        
        // Update Company Information (Individual columns)
        supplierPartner.setCompanyName(dto.getCompanyName());
        supplierPartner.setLegalForm(dto.getLegalForm());
        supplierPartner.setRegistrationNumber(dto.getRegistrationNumber());
        supplierPartner.setTaxId(dto.getTaxId());
        supplierPartner.setVatNumber(dto.getVatNumber());
        supplierPartner.setBusinessActivity(dto.getBusinessActivity());
        supplierPartner.setAnnualTurnover(dto.getAnnualTurnover());
        supplierPartner.setNumberOfEmployees(dto.getNumberOfEmployees());
        
        // Update Contract Information (Individual columns)
        supplierPartner.setContractNumber(dto.getContractNumber());
        supplierPartner.setContractStartDate(dto.getContractStartDate());
        supplierPartner.setContractEndDate(dto.getContractEndDate());
        supplierPartner.setContractType(dto.getContractType());
        supplierPartner.setContractTerms(dto.getContractTerms());
        supplierPartner.setPaymentTerms(dto.getPaymentTerms());
        supplierPartner.setDeliveryTerms(dto.getDeliveryTerms());
        supplierPartner.setSpecialConditions(dto.getSpecialConditions());
        
        // Update Contact Information (Individual columns)
        supplierPartner.setTelephone(dto.getTelephone());
        supplierPartner.setEmail(dto.getEmail());
        supplierPartner.setAddress(dto.getAddress());
        supplierPartner.setCity(dto.getVille());
        supplierPartner.setCountry(dto.getCountry());
        supplierPartner.setPostalCode(dto.getCodePostal());
        
        // Update Credit Information (Individual columns)
        supplierPartner.setCreditLimit(dto.getCreditLimit());
        supplierPartner.setOutstandingBalance(dto.getOutstandingBalance());
        supplierPartner.setCreditRating(dto.getCreditRating());
        supplierPartner.setCreditScore(dto.getCreditScore());
        supplierPartner.setPaymentTermDays(dto.getPaymentTermDays());
        supplierPartner.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        supplierPartner.setBankAccountInfo(dto.getBankAccountInfo());
        supplierPartner.setLastPaymentDate(dto.getLastPaymentDate());
        supplierPartner.setPaymentHistory(dto.getPaymentHistory());
        
        // Update Loyalty Information (Individual columns)
        supplierPartner.setIsVip(dto.isVip());
        supplierPartner.setLoyaltyPoints(dto.getLoyaltyPoints());
        supplierPartner.setLastOrderDate(dto.getLastOrderDate());
        supplierPartner.setTotalOrders(dto.getTotalOrders());
        supplierPartner.setTotalSpent(dto.getTotalSpent());
        supplierPartner.setAverageOrderValue(dto.getAverageOrderValue());
        supplierPartner.setPartnerSince(dto.getCustomerSince());
        
        // Update Delivery Preference (Individual columns)
        supplierPartner.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        supplierPartner.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        supplierPartner.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Update Audit Information (Individual columns)
        supplierPartner.setNotes(dto.getNotes());
        supplierPartner.setIsActive(dto.isActive());
        supplierPartner.setLastActivityDate(dto.getLastActivityDate());
        supplierPartner.setCreatedBy(dto.getCreatedBy());
        supplierPartner.setUpdatedBy(dto.getUpdatedBy());
        supplierPartner.setCreatedAt(dto.getCreatedAt());
        supplierPartner.setUpdatedAt(dto.getUpdatedAt());
    }

    public List<SupplierPartnerDTO> toDTOList(List<SupplierPartner> supplierPartners) {
        if (supplierPartners == null) return null;
        List<SupplierPartnerDTO> list = new ArrayList<>();
        for (SupplierPartner sp : supplierPartners) list.add(toDTO(sp));
        return list;
    }

    public PartnerDTO toPartnerDTO(SupplierPartner supplierPartner) {
        if (supplierPartner == null) return null;
        
        PartnerDTO dto = new PartnerDTO();
        
        // Basic partner information
        dto.setId(supplierPartner.getId());
        dto.setCtNum(supplierPartner.getCtNum());
        dto.setIce(supplierPartner.getIce());
        dto.setDescription(supplierPartner.getDescription());
        dto.setPartnerType(supplierPartner.getPartnerType());
        dto.setCategoryTarifId(supplierPartner.getCateTarif());
        
        // Company Information (Individual columns)
        dto.setCompanyName(supplierPartner.getCompanyName());
        dto.setLegalForm(supplierPartner.getLegalForm());
        dto.setRegistrationNumber(supplierPartner.getRegistrationNumber());
        dto.setTaxId(supplierPartner.getTaxId());
        dto.setVatNumber(supplierPartner.getVatNumber());
        dto.setBusinessActivity(supplierPartner.getBusinessActivity());
        dto.setAnnualTurnover(supplierPartner.getAnnualTurnover());
        dto.setNumberOfEmployees(supplierPartner.getNumberOfEmployees());
        
        // Contract Information (Individual columns)
        dto.setContractNumber(supplierPartner.getContractNumber());
        dto.setContractStartDate(supplierPartner.getContractStartDate());
        dto.setContractEndDate(supplierPartner.getContractEndDate());
        dto.setContractType(supplierPartner.getContractType());
        dto.setContractTerms(supplierPartner.getContractTerms());
        dto.setPaymentTerms(supplierPartner.getPaymentTerms());
        dto.setDeliveryTerms(supplierPartner.getDeliveryTerms());
        dto.setSpecialConditions(supplierPartner.getSpecialConditions());
        
        // Contact Information (Individual columns)
        dto.setTelephone(supplierPartner.getTelephone());
        dto.setEmail(supplierPartner.getEmail());
        dto.setAddress(supplierPartner.getAddress());
        dto.setCodePostal(supplierPartner.getPostalCode());
        dto.setVille(supplierPartner.getCity());
        dto.setCountry(supplierPartner.getCountry());
        
        // Credit Information (Individual columns)
        dto.setCreditLimit(supplierPartner.getCreditLimit());
        dto.setOutstandingBalance(supplierPartner.getOutstandingBalance());
        dto.setCreditRating(supplierPartner.getCreditRating());
        dto.setCreditScore(supplierPartner.getCreditScore());
        dto.setPaymentTermDays(supplierPartner.getPaymentTermDays());
        dto.setPreferredPaymentMethod(supplierPartner.getPreferredPaymentMethod());
        dto.setBankAccountInfo(supplierPartner.getBankAccountInfo());
        dto.setLastPaymentDate(supplierPartner.getLastPaymentDate());
        dto.setPaymentHistory(supplierPartner.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        dto.setVip(supplierPartner.getIsVip() != null && supplierPartner.getIsVip());
        dto.setLoyaltyPoints(supplierPartner.getLoyaltyPoints());
        dto.setLastOrderDate(supplierPartner.getLastOrderDate());
        dto.setTotalOrders(supplierPartner.getTotalOrders());
        dto.setTotalSpent(supplierPartner.getTotalSpent());
        dto.setAverageOrderValue(supplierPartner.getAverageOrderValue());
        dto.setCustomerSince(supplierPartner.getPartnerSince());
        
        // Delivery Preference (Individual columns)
        dto.setPreferredDeliveryTime(supplierPartner.getPreferredDeliveryTime());
        dto.setPreferredDeliveryDays(supplierPartner.getPreferredDeliveryDays());
        dto.setSpecialHandlingInstructions(supplierPartner.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        dto.setNotes(supplierPartner.getNotes());
        dto.setActive(supplierPartner.getIsActive() != null && supplierPartner.getIsActive());
        dto.setLastActivityDate(supplierPartner.getLastActivityDate());
        dto.setCreatedBy(supplierPartner.getCreatedBy());
        dto.setUpdatedBy(supplierPartner.getUpdatedBy());
        dto.setCreatedAt(supplierPartner.getCreatedAt());
        dto.setUpdatedAt(supplierPartner.getUpdatedAt());
        
        return dto;
    }
} 