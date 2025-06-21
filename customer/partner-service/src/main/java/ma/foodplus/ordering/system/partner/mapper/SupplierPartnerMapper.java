package ma.foodplus.ordering.system.partner.mapper;

import ma.foodplus.ordering.system.partner.domain.SupplierPartner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.dto.SupplierPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Manual mapper for Supplier Partner entities and DTOs.
 * 
 * <p>This mapper handles the conversion between SupplierPartner entities and SupplierPartnerDTO objects,
 * including all supplier-specific fields such as supplier information and performance metrics.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Component
public class SupplierPartnerMapper {
    
    public SupplierPartnerDTO toDTO(SupplierPartner supplierPartner) {
        if (supplierPartner == null) return null;
        
        SupplierPartnerDTO dto = new SupplierPartnerDTO();
        
        // Base partner fields
        dto.setId(supplierPartner.getId());
        dto.setCtNum(supplierPartner.getCtNum());
        dto.setIce(supplierPartner.getIce());
        dto.setDescription(supplierPartner.getDescription());
        dto.setPartnerType(PartnerType.SUPPLIER);
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
        
        // Company Info (from embedded object)
        if (supplierPartner.getCompanyInfo() != null) {
            dto.setCompanyName(supplierPartner.getCompanyInfo().getCompanyName());
            dto.setLegalForm(supplierPartner.getCompanyInfo().getLegalForm());
            dto.setRegistrationNumber(supplierPartner.getCompanyInfo().getRegistrationNumber());
            dto.setTaxId(supplierPartner.getCompanyInfo().getTaxId());
            dto.setVatNumber(supplierPartner.getCompanyInfo().getVatNumber());
            dto.setBusinessActivity(supplierPartner.getCompanyInfo().getBusinessActivity());
            dto.setAnnualTurnover(supplierPartner.getCompanyInfo().getAnnualTurnover());
            dto.setNumberOfEmployees(supplierPartner.getCompanyInfo().getNumberOfEmployees());
        }
        
        // Contract Info (from embedded object)
        if (supplierPartner.getContractInfo() != null) {
            dto.setContractNumber(supplierPartner.getContractInfo().getContractNumber());
            dto.setContractStartDate(supplierPartner.getContractInfo().getContractStartDate());
            dto.setContractEndDate(supplierPartner.getContractInfo().getContractEndDate());
            dto.setContractType(supplierPartner.getContractInfo().getContractType());
            dto.setContractTerms(supplierPartner.getContractInfo().getContractTerms());
            dto.setPaymentTerms(supplierPartner.getContractInfo().getPaymentTerms());
            dto.setDeliveryTerms(supplierPartner.getContractInfo().getDeliveryTerms());
            dto.setSpecialConditions(supplierPartner.getContractInfo().getSpecialConditions());
        }
        
        // Contact Info
        if (supplierPartner.getContactInfo() != null) {
            dto.setTelephone(supplierPartner.getContactInfo().getTelephone());
            dto.setEmail(supplierPartner.getContactInfo().getEmail());
            dto.setAddress(supplierPartner.getContactInfo().getAddress());
            dto.setCodePostal(supplierPartner.getContactInfo().getPostalCode());
            dto.setVille(supplierPartner.getContactInfo().getCity());
            dto.setCountry(supplierPartner.getContactInfo().getCountry());
        }
        
        // Credit Info
        if (supplierPartner.getCreditInfo() != null) {
            dto.setCreditLimit(supplierPartner.getCreditInfo().getCreditLimit());
            dto.setCreditRating(supplierPartner.getCreditInfo().getCreditRating());
            dto.setCreditScore(supplierPartner.getCreditInfo().getCreditScore());
            dto.setPaymentHistory(supplierPartner.getCreditInfo().getPaymentHistory());
            dto.setOutstandingBalance(supplierPartner.getCreditInfo().getOutstandingBalance());
            dto.setLastPaymentDate(supplierPartner.getCreditInfo().getLastPaymentDate());
            dto.setPaymentTermDays(supplierPartner.getCreditInfo().getPaymentTermDays());
            dto.setPreferredPaymentMethod(supplierPartner.getCreditInfo().getPreferredPaymentMethod());
            dto.setBankAccountInfo(supplierPartner.getCreditInfo().getBankAccountInfo());
        }
        
        // Loyalty Info
        if (supplierPartner.getLoyaltyInfo() != null) {
            dto.setVip(supplierPartner.getLoyaltyInfo().isVip());
            dto.setLoyaltyPoints(supplierPartner.getLoyaltyInfo().getLoyaltyPoints());
            dto.setLastOrderDate(supplierPartner.getLoyaltyInfo().getLastOrderDate());
            dto.setTotalOrders(supplierPartner.getLoyaltyInfo().getTotalOrders());
            dto.setTotalSpent(supplierPartner.getLoyaltyInfo().getTotalSpent());
            dto.setAverageOrderValue(supplierPartner.getLoyaltyInfo().getAverageOrderValue());
            dto.setCustomerSince(supplierPartner.getLoyaltyInfo().getPartnerSince());
        }
        
        // Delivery Preference
        if (supplierPartner.getDeliveryPreference() != null) {
            dto.setPreferredDeliveryTime(supplierPartner.getDeliveryPreference().getPreferredDeliveryTime());
            dto.setPreferredDeliveryDays(supplierPartner.getDeliveryPreference().getPreferredDeliveryDays());
            dto.setSpecialHandlingInstructions(supplierPartner.getDeliveryPreference().getSpecialHandlingInstructions());
        }
        
        // Audit Info
        if (supplierPartner.getAuditInfo() != null) {
            dto.setNotes(supplierPartner.getAuditInfo().getNotes());
            dto.setActive(supplierPartner.getAuditInfo().isActive());
            dto.setLastActivityDate(supplierPartner.getAuditInfo().getLastActivityDate());
            dto.setCreatedBy(supplierPartner.getAuditInfo().getCreatedBy());
            dto.setUpdatedBy(supplierPartner.getAuditInfo().getUpdatedBy());
            dto.setCreatedAt(supplierPartner.getAuditInfo().getCreatedAt());
            dto.setUpdatedAt(supplierPartner.getAuditInfo().getUpdatedAt());
        }
        
        return dto;
    }
    
    public SupplierPartner toEntity(SupplierPartnerDTO dto) {
        if (dto == null) return null;
        
        SupplierPartner supplierPartner = new SupplierPartner();
        
        // Base partner fields
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
        
        // Company Info
        if (dto.getCompanyName() != null || dto.getLegalForm() != null) {
            ma.foodplus.ordering.system.partner.domain.CompanyInfo companyInfo = new ma.foodplus.ordering.system.partner.domain.CompanyInfo();
            companyInfo.setCompanyName(dto.getCompanyName());
            companyInfo.setLegalForm(dto.getLegalForm());
            companyInfo.setRegistrationNumber(dto.getRegistrationNumber());
            companyInfo.setTaxId(dto.getTaxId());
            companyInfo.setVatNumber(dto.getVatNumber());
            companyInfo.setBusinessActivity(dto.getBusinessActivity());
            companyInfo.setAnnualTurnover(dto.getAnnualTurnover());
            companyInfo.setNumberOfEmployees(dto.getNumberOfEmployees());
            supplierPartner.setCompanyInfo(companyInfo);
        }
        
        // Contract Info
        if (dto.getContractNumber() != null || dto.getContractStartDate() != null) {
            ma.foodplus.ordering.system.partner.domain.ContractInfo contractInfo = new ma.foodplus.ordering.system.partner.domain.ContractInfo();
            contractInfo.setContractNumber(dto.getContractNumber());
            contractInfo.setContractStartDate(dto.getContractStartDate());
            contractInfo.setContractEndDate(dto.getContractEndDate());
            contractInfo.setContractType(dto.getContractType());
            contractInfo.setContractTerms(dto.getContractTerms());
            contractInfo.setPaymentTerms(dto.getPaymentTerms());
            contractInfo.setDeliveryTerms(dto.getDeliveryTerms());
            contractInfo.setSpecialConditions(dto.getSpecialConditions());
            supplierPartner.setContractInfo(contractInfo);
        }
        
        // Contact Info
        if (dto.getTelephone() != null || dto.getEmail() != null) {
            ma.foodplus.ordering.system.partner.domain.ContactInfo contactInfo = new ma.foodplus.ordering.system.partner.domain.ContactInfo();
            contactInfo.setTelephone(dto.getTelephone());
            contactInfo.setEmail(dto.getEmail());
            contactInfo.setAddress(dto.getAddress());
            contactInfo.setPostalCode(dto.getCodePostal());
            contactInfo.setCity(dto.getVille());
            contactInfo.setCountry(dto.getCountry());
            supplierPartner.setContactInfo(contactInfo);
        }
        
        // Credit Info
        if (dto.getCreditLimit() != null || dto.getOutstandingBalance() != null) {
            ma.foodplus.ordering.system.partner.domain.CreditInfo creditInfo = new ma.foodplus.ordering.system.partner.domain.CreditInfo();
            creditInfo.setCreditLimit(dto.getCreditLimit());
            creditInfo.setCreditRating(dto.getCreditRating());
            creditInfo.setCreditScore(dto.getCreditScore());
            creditInfo.setPaymentHistory(dto.getPaymentHistory());
            creditInfo.setOutstandingBalance(dto.getOutstandingBalance());
            creditInfo.setLastPaymentDate(dto.getLastPaymentDate());
            creditInfo.setPaymentTermDays(dto.getPaymentTermDays());
            creditInfo.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
            creditInfo.setBankAccountInfo(dto.getBankAccountInfo());
            supplierPartner.setCreditInfo(creditInfo);
        }
        
        // Loyalty Info
        if (dto.getLoyaltyPoints() != null || dto.isVip()) {
            ma.foodplus.ordering.system.partner.domain.LoyaltyInfo loyaltyInfo = new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo();
            loyaltyInfo.setVip(dto.isVip());
            loyaltyInfo.setLoyaltyPoints(dto.getLoyaltyPoints());
            loyaltyInfo.setLastOrderDate(dto.getLastOrderDate());
            loyaltyInfo.setTotalOrders(dto.getTotalOrders());
            loyaltyInfo.setTotalSpent(dto.getTotalSpent());
            loyaltyInfo.setAverageOrderValue(dto.getAverageOrderValue());
            loyaltyInfo.setPartnerSince(dto.getCustomerSince());
            supplierPartner.setLoyaltyInfo(loyaltyInfo);
        }
        
        // Delivery Preference
        if (dto.getPreferredDeliveryTime() != null || dto.getPreferredDeliveryDays() != null) {
            ma.foodplus.ordering.system.partner.domain.DeliveryPreference deliveryPreference = new ma.foodplus.ordering.system.partner.domain.DeliveryPreference();
            deliveryPreference.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
            deliveryPreference.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
            deliveryPreference.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
            supplierPartner.setDeliveryPreference(deliveryPreference);
        }
        
        // Audit Info
        ma.foodplus.ordering.system.partner.domain.AuditInfo auditInfo = new ma.foodplus.ordering.system.partner.domain.AuditInfo();
        auditInfo.setNotes(dto.getNotes());
        auditInfo.setActive(dto.isActive());
        auditInfo.setLastActivityDate(dto.getLastActivityDate());
        auditInfo.setCreatedBy(dto.getCreatedBy());
        auditInfo.setUpdatedBy(dto.getUpdatedBy());
        auditInfo.setCreatedAt(dto.getCreatedAt());
        auditInfo.setUpdatedAt(dto.getUpdatedAt());
        supplierPartner.setAuditInfo(auditInfo);
        
        return supplierPartner;
    }
    
    public void updateEntityFromDTO(SupplierPartnerDTO dto, SupplierPartner supplierPartner) {
        if (dto == null || supplierPartner == null) return;
        
        // Base partner fields
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
        
        // Company Info
        if (supplierPartner.getCompanyInfo() == null) {
            supplierPartner.setCompanyInfo(new ma.foodplus.ordering.system.partner.domain.CompanyInfo());
        }
        supplierPartner.getCompanyInfo().setCompanyName(dto.getCompanyName());
        supplierPartner.getCompanyInfo().setLegalForm(dto.getLegalForm());
        supplierPartner.getCompanyInfo().setRegistrationNumber(dto.getRegistrationNumber());
        supplierPartner.getCompanyInfo().setTaxId(dto.getTaxId());
        supplierPartner.getCompanyInfo().setVatNumber(dto.getVatNumber());
        supplierPartner.getCompanyInfo().setBusinessActivity(dto.getBusinessActivity());
        supplierPartner.getCompanyInfo().setAnnualTurnover(dto.getAnnualTurnover());
        supplierPartner.getCompanyInfo().setNumberOfEmployees(dto.getNumberOfEmployees());
        
        // Contract Info
        if (supplierPartner.getContractInfo() == null) {
            supplierPartner.setContractInfo(new ma.foodplus.ordering.system.partner.domain.ContractInfo());
        }
        supplierPartner.getContractInfo().setContractNumber(dto.getContractNumber());
        supplierPartner.getContractInfo().setContractStartDate(dto.getContractStartDate());
        supplierPartner.getContractInfo().setContractEndDate(dto.getContractEndDate());
        supplierPartner.getContractInfo().setContractType(dto.getContractType());
        supplierPartner.getContractInfo().setContractTerms(dto.getContractTerms());
        supplierPartner.getContractInfo().setPaymentTerms(dto.getPaymentTerms());
        supplierPartner.getContractInfo().setDeliveryTerms(dto.getDeliveryTerms());
        supplierPartner.getContractInfo().setSpecialConditions(dto.getSpecialConditions());
        
        // Contact Info
        if (supplierPartner.getContactInfo() == null) {
            supplierPartner.setContactInfo(new ma.foodplus.ordering.system.partner.domain.ContactInfo());
        }
        supplierPartner.getContactInfo().setTelephone(dto.getTelephone());
        supplierPartner.getContactInfo().setEmail(dto.getEmail());
        supplierPartner.getContactInfo().setAddress(dto.getAddress());
        supplierPartner.getContactInfo().setPostalCode(dto.getCodePostal());
        supplierPartner.getContactInfo().setCity(dto.getVille());
        supplierPartner.getContactInfo().setCountry(dto.getCountry());
        
        // Credit Info
        if (supplierPartner.getCreditInfo() == null) {
            supplierPartner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        }
        supplierPartner.getCreditInfo().setCreditLimit(dto.getCreditLimit());
        supplierPartner.getCreditInfo().setCreditRating(dto.getCreditRating());
        supplierPartner.getCreditInfo().setCreditScore(dto.getCreditScore());
        supplierPartner.getCreditInfo().setPaymentHistory(dto.getPaymentHistory());
        supplierPartner.getCreditInfo().setOutstandingBalance(dto.getOutstandingBalance());
        supplierPartner.getCreditInfo().setLastPaymentDate(dto.getLastPaymentDate());
        supplierPartner.getCreditInfo().setPaymentTermDays(dto.getPaymentTermDays());
        supplierPartner.getCreditInfo().setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        supplierPartner.getCreditInfo().setBankAccountInfo(dto.getBankAccountInfo());
        
        // Loyalty Info
        if (supplierPartner.getLoyaltyInfo() == null) {
            supplierPartner.setLoyaltyInfo(new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo());
        }
        supplierPartner.getLoyaltyInfo().setVip(dto.isVip());
        supplierPartner.getLoyaltyInfo().setLoyaltyPoints(dto.getLoyaltyPoints());
        supplierPartner.getLoyaltyInfo().setLastOrderDate(dto.getLastOrderDate());
        supplierPartner.getLoyaltyInfo().setTotalOrders(dto.getTotalOrders());
        supplierPartner.getLoyaltyInfo().setTotalSpent(dto.getTotalSpent());
        supplierPartner.getLoyaltyInfo().setAverageOrderValue(dto.getAverageOrderValue());
        supplierPartner.getLoyaltyInfo().setPartnerSince(dto.getCustomerSince());
        
        // Delivery Preference
        if (supplierPartner.getDeliveryPreference() == null) {
            supplierPartner.setDeliveryPreference(new ma.foodplus.ordering.system.partner.domain.DeliveryPreference());
        }
        supplierPartner.getDeliveryPreference().setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        supplierPartner.getDeliveryPreference().setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        supplierPartner.getDeliveryPreference().setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Audit Info
        if (supplierPartner.getAuditInfo() == null) {
            supplierPartner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        supplierPartner.getAuditInfo().setNotes(dto.getNotes());
        supplierPartner.getAuditInfo().setActive(dto.isActive());
        supplierPartner.getAuditInfo().setLastActivityDate(dto.getLastActivityDate());
        supplierPartner.getAuditInfo().setCreatedBy(dto.getCreatedBy());
        supplierPartner.getAuditInfo().setUpdatedBy(dto.getUpdatedBy());
        supplierPartner.getAuditInfo().setCreatedAt(dto.getCreatedAt());
        supplierPartner.getAuditInfo().setUpdatedAt(dto.getUpdatedAt());
    }
    
    public List<SupplierPartnerDTO> toDTOList(List<SupplierPartner> supplierPartners) {
        if (supplierPartners == null) return null;
        List<SupplierPartnerDTO> list = new ArrayList<>();
        for (SupplierPartner p : supplierPartners) {
            list.add(toDTO(p));
        }
        return list;
    }
    
    // ========== Compatibility with Generic PartnerDTO ==========
    
    /**
     * Maps SupplierPartner to generic PartnerDTO for backward compatibility.
     */
    public PartnerDTO toPartnerDTO(SupplierPartner supplierPartner) {
        if (supplierPartner == null) return null;
        
        PartnerDTO dto = new PartnerDTO();
        
        // Base partner fields
        dto.setId(supplierPartner.getId());
        dto.setCtNum(supplierPartner.getCtNum());
        dto.setIce(supplierPartner.getIce());
        dto.setDescription(supplierPartner.getDescription());
        dto.setPartnerType(PartnerType.SUPPLIER);
        dto.setCategoryTarifId(supplierPartner.getCateTarif());
        
        // Company Info (flattened)
        if (supplierPartner.getCompanyInfo() != null) {
            dto.setCompanyName(supplierPartner.getCompanyInfo().getCompanyName());
            dto.setLegalForm(supplierPartner.getCompanyInfo().getLegalForm());
            dto.setRegistrationNumber(supplierPartner.getCompanyInfo().getRegistrationNumber());
            dto.setTaxId(supplierPartner.getCompanyInfo().getTaxId());
            dto.setVatNumber(supplierPartner.getCompanyInfo().getVatNumber());
            dto.setBusinessActivity(supplierPartner.getCompanyInfo().getBusinessActivity());
            dto.setAnnualTurnover(supplierPartner.getCompanyInfo().getAnnualTurnover());
            dto.setNumberOfEmployees(supplierPartner.getCompanyInfo().getNumberOfEmployees());
        }
        
        // Contract Info (flattened)
        if (supplierPartner.getContractInfo() != null) {
            dto.setContractNumber(supplierPartner.getContractInfo().getContractNumber());
            dto.setContractStartDate(supplierPartner.getContractInfo().getContractStartDate());
            dto.setContractEndDate(supplierPartner.getContractInfo().getContractEndDate());
            dto.setContractType(supplierPartner.getContractInfo().getContractType());
            dto.setContractTerms(supplierPartner.getContractInfo().getContractTerms());
            dto.setPaymentTerms(supplierPartner.getContractInfo().getPaymentTerms());
            dto.setDeliveryTerms(supplierPartner.getContractInfo().getDeliveryTerms());
            dto.setSpecialConditions(supplierPartner.getContractInfo().getSpecialConditions());
        }
        
        // Contact Info
        if (supplierPartner.getContactInfo() != null) {
            dto.setTelephone(supplierPartner.getContactInfo().getTelephone());
            dto.setEmail(supplierPartner.getContactInfo().getEmail());
            dto.setAddress(supplierPartner.getContactInfo().getAddress());
            dto.setCodePostal(supplierPartner.getContactInfo().getPostalCode());
            dto.setVille(supplierPartner.getContactInfo().getCity());
            dto.setCountry(supplierPartner.getContactInfo().getCountry());
        }
        
        // Credit Info
        if (supplierPartner.getCreditInfo() != null) {
            dto.setCreditLimit(supplierPartner.getCreditInfo().getCreditLimit());
            dto.setCreditRating(supplierPartner.getCreditInfo().getCreditRating());
            dto.setCreditScore(supplierPartner.getCreditInfo().getCreditScore());
            dto.setPaymentHistory(supplierPartner.getCreditInfo().getPaymentHistory());
            dto.setOutstandingBalance(supplierPartner.getCreditInfo().getOutstandingBalance());
            dto.setLastPaymentDate(supplierPartner.getCreditInfo().getLastPaymentDate());
            dto.setPaymentTermDays(supplierPartner.getCreditInfo().getPaymentTermDays());
            dto.setPreferredPaymentMethod(supplierPartner.getCreditInfo().getPreferredPaymentMethod());
            dto.setBankAccountInfo(supplierPartner.getCreditInfo().getBankAccountInfo());
        }
        
        // Loyalty Info
        if (supplierPartner.getLoyaltyInfo() != null) {
            dto.setVip(supplierPartner.getLoyaltyInfo().isVip());
            dto.setLoyaltyPoints(supplierPartner.getLoyaltyInfo().getLoyaltyPoints());
            dto.setLastOrderDate(supplierPartner.getLoyaltyInfo().getLastOrderDate());
            dto.setTotalOrders(supplierPartner.getLoyaltyInfo().getTotalOrders());
            dto.setTotalSpent(supplierPartner.getLoyaltyInfo().getTotalSpent());
            dto.setAverageOrderValue(supplierPartner.getLoyaltyInfo().getAverageOrderValue());
            dto.setCustomerSince(supplierPartner.getLoyaltyInfo().getPartnerSince());
        }
        
        // Delivery Preference
        if (supplierPartner.getDeliveryPreference() != null) {
            dto.setPreferredDeliveryTime(supplierPartner.getDeliveryPreference().getPreferredDeliveryTime());
            dto.setPreferredDeliveryDays(supplierPartner.getDeliveryPreference().getPreferredDeliveryDays());
            dto.setSpecialHandlingInstructions(supplierPartner.getDeliveryPreference().getSpecialHandlingInstructions());
        }
        
        // Audit Info
        if (supplierPartner.getAuditInfo() != null) {
            dto.setNotes(supplierPartner.getAuditInfo().getNotes());
            dto.setActive(supplierPartner.getAuditInfo().isActive());
            dto.setLastActivityDate(supplierPartner.getAuditInfo().getLastActivityDate());
            dto.setCreatedBy(supplierPartner.getAuditInfo().getCreatedBy());
            dto.setUpdatedBy(supplierPartner.getAuditInfo().getUpdatedBy());
            dto.setCreatedAt(supplierPartner.getAuditInfo().getCreatedAt());
            dto.setUpdatedAt(supplierPartner.getAuditInfo().getUpdatedAt());
        }
        
        return dto;
    }
} 