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
        
        // Base partner fields
        dto.setId(b2bPartner.getId());
        dto.setCtNum(b2bPartner.getCtNum());
        dto.setIce(b2bPartner.getIce());
        dto.setDescription(b2bPartner.getDescription());
        dto.setPartnerType(PartnerType.B2B);
        dto.setCategoryTarifId(b2bPartner.getCateTarif());
        
        // Company Info
        if (b2bPartner.getCompanyInfo() != null) {
            dto.setCompanyName(b2bPartner.getCompanyInfo().getCompanyName());
            dto.setLegalForm(b2bPartner.getCompanyInfo().getLegalForm());
            dto.setRegistrationNumber(b2bPartner.getCompanyInfo().getRegistrationNumber());
            dto.setTaxId(b2bPartner.getCompanyInfo().getTaxId());
            dto.setVatNumber(b2bPartner.getCompanyInfo().getVatNumber());
            dto.setBusinessActivity(b2bPartner.getCompanyInfo().getBusinessActivity());
            dto.setAnnualTurnover(b2bPartner.getCompanyInfo().getAnnualTurnover());
            dto.setNumberOfEmployees(b2bPartner.getCompanyInfo().getNumberOfEmployees());
        }
        
        // Contract Info
        if (b2bPartner.getContractInfo() != null) {
            dto.setContractNumber(b2bPartner.getContractInfo().getContractNumber());
            dto.setContractStartDate(b2bPartner.getContractInfo().getContractStartDate());
            dto.setContractEndDate(b2bPartner.getContractInfo().getContractEndDate());
            dto.setContractType(b2bPartner.getContractInfo().getContractType());
            dto.setContractTerms(b2bPartner.getContractInfo().getContractTerms());
            dto.setPaymentTerms(b2bPartner.getContractInfo().getPaymentTerms());
            dto.setDeliveryTerms(b2bPartner.getContractInfo().getDeliveryTerms());
            dto.setSpecialConditions(b2bPartner.getContractInfo().getSpecialConditions());
        }
        
        // Contact Info
        if (b2bPartner.getContactInfo() != null) {
            dto.setTelephone(b2bPartner.getContactInfo().getTelephone());
            dto.setEmail(b2bPartner.getContactInfo().getEmail());
            dto.setAddress(b2bPartner.getContactInfo().getAddress());
            dto.setCodePostal(b2bPartner.getContactInfo().getPostalCode());
            dto.setVille(b2bPartner.getContactInfo().getCity());
            dto.setCountry(b2bPartner.getContactInfo().getCountry());
        }
        
        // Credit Info
        if (b2bPartner.getCreditInfo() != null) {
            dto.setCreditLimit(b2bPartner.getCreditInfo().getCreditLimit());
            dto.setCreditRating(b2bPartner.getCreditInfo().getCreditRating());
            dto.setCreditScore(b2bPartner.getCreditInfo().getCreditScore());
            dto.setPaymentHistory(b2bPartner.getCreditInfo().getPaymentHistory());
            dto.setOutstandingBalance(b2bPartner.getCreditInfo().getOutstandingBalance());
            dto.setLastPaymentDate(b2bPartner.getCreditInfo().getLastPaymentDate());
            dto.setPaymentTermDays(b2bPartner.getCreditInfo().getPaymentTermDays());
            dto.setPreferredPaymentMethod(b2bPartner.getCreditInfo().getPreferredPaymentMethod());
            dto.setBankAccountInfo(b2bPartner.getCreditInfo().getBankAccountInfo());
        }
        
        // Loyalty Info
        if (b2bPartner.getLoyaltyInfo() != null) {
            dto.setVip(b2bPartner.getLoyaltyInfo().isVip());
            dto.setLoyaltyPoints(b2bPartner.getLoyaltyInfo().getLoyaltyPoints());
            dto.setLastOrderDate(b2bPartner.getLoyaltyInfo().getLastOrderDate());
            dto.setTotalOrders(b2bPartner.getLoyaltyInfo().getTotalOrders());
            dto.setTotalSpent(b2bPartner.getLoyaltyInfo().getTotalSpent());
            dto.setAverageOrderValue(b2bPartner.getLoyaltyInfo().getAverageOrderValue());
            dto.setCustomerSince(b2bPartner.getLoyaltyInfo().getPartnerSince());
        }
        
        // Delivery Preference
        if (b2bPartner.getDeliveryPreference() != null) {
            dto.setPreferredDeliveryTime(b2bPartner.getDeliveryPreference().getPreferredDeliveryTime());
            dto.setPreferredDeliveryDays(b2bPartner.getDeliveryPreference().getPreferredDeliveryDays());
            dto.setSpecialHandlingInstructions(b2bPartner.getDeliveryPreference().getSpecialHandlingInstructions());
        }
        
        // Audit Info
        if (b2bPartner.getAuditInfo() != null) {
            dto.setNotes(b2bPartner.getAuditInfo().getNotes());
            dto.setActive(b2bPartner.getAuditInfo().isActive());
            dto.setLastActivityDate(b2bPartner.getAuditInfo().getLastActivityDate());
            dto.setCreatedBy(b2bPartner.getAuditInfo().getCreatedBy());
            dto.setUpdatedBy(b2bPartner.getAuditInfo().getUpdatedBy());
            dto.setCreatedAt(b2bPartner.getAuditInfo().getCreatedAt());
            dto.setUpdatedAt(b2bPartner.getAuditInfo().getUpdatedAt());
        }
        
        return dto;
    }
    
    public B2BPartner toEntity(B2BPartnerDTO dto) {
        if (dto == null) return null;
        
        B2BPartner b2bPartner = new B2BPartner();
        
        // Base partner fields
        b2bPartner.setCtNum(dto.getCtNum());
        b2bPartner.setIce(dto.getIce());
        b2bPartner.setDescription(dto.getDescription());
        b2bPartner.setCateTarif(dto.getCategoryTarifId());
        
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
            b2bPartner.setCompanyInfo(companyInfo);
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
            b2bPartner.setContractInfo(contractInfo);
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
            b2bPartner.setContactInfo(contactInfo);
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
            b2bPartner.setCreditInfo(creditInfo);
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
            b2bPartner.setLoyaltyInfo(loyaltyInfo);
        }
        
        // Delivery Preference
        if (dto.getPreferredDeliveryTime() != null || dto.getPreferredDeliveryDays() != null) {
            ma.foodplus.ordering.system.partner.domain.DeliveryPreference deliveryPreference = new ma.foodplus.ordering.system.partner.domain.DeliveryPreference();
            deliveryPreference.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
            deliveryPreference.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
            deliveryPreference.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
            b2bPartner.setDeliveryPreference(deliveryPreference);
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
        b2bPartner.setAuditInfo(auditInfo);
        
        return b2bPartner;
    }
    
    public void updateEntityFromDTO(B2BPartnerDTO dto, B2BPartner b2bPartner) {
        if (dto == null || b2bPartner == null) return;
        
        // Base partner fields
        b2bPartner.setCtNum(dto.getCtNum());
        b2bPartner.setIce(dto.getIce());
        b2bPartner.setDescription(dto.getDescription());
        b2bPartner.setCateTarif(dto.getCategoryTarifId());
        
        // Company Info
        if (b2bPartner.getCompanyInfo() == null) {
            b2bPartner.setCompanyInfo(new ma.foodplus.ordering.system.partner.domain.CompanyInfo());
        }
        b2bPartner.getCompanyInfo().setCompanyName(dto.getCompanyName());
        b2bPartner.getCompanyInfo().setLegalForm(dto.getLegalForm());
        b2bPartner.getCompanyInfo().setRegistrationNumber(dto.getRegistrationNumber());
        b2bPartner.getCompanyInfo().setTaxId(dto.getTaxId());
        b2bPartner.getCompanyInfo().setVatNumber(dto.getVatNumber());
        b2bPartner.getCompanyInfo().setBusinessActivity(dto.getBusinessActivity());
        b2bPartner.getCompanyInfo().setAnnualTurnover(dto.getAnnualTurnover());
        b2bPartner.getCompanyInfo().setNumberOfEmployees(dto.getNumberOfEmployees());
        
        // Contract Info
        if (b2bPartner.getContractInfo() == null) {
            b2bPartner.setContractInfo(new ma.foodplus.ordering.system.partner.domain.ContractInfo());
        }
        b2bPartner.getContractInfo().setContractNumber(dto.getContractNumber());
        b2bPartner.getContractInfo().setContractStartDate(dto.getContractStartDate());
        b2bPartner.getContractInfo().setContractEndDate(dto.getContractEndDate());
        b2bPartner.getContractInfo().setContractType(dto.getContractType());
        b2bPartner.getContractInfo().setContractTerms(dto.getContractTerms());
        b2bPartner.getContractInfo().setPaymentTerms(dto.getPaymentTerms());
        b2bPartner.getContractInfo().setDeliveryTerms(dto.getDeliveryTerms());
        b2bPartner.getContractInfo().setSpecialConditions(dto.getSpecialConditions());
        
        // Contact Info
        if (b2bPartner.getContactInfo() == null) {
            b2bPartner.setContactInfo(new ma.foodplus.ordering.system.partner.domain.ContactInfo());
        }
        b2bPartner.getContactInfo().setTelephone(dto.getTelephone());
        b2bPartner.getContactInfo().setEmail(dto.getEmail());
        b2bPartner.getContactInfo().setAddress(dto.getAddress());
        b2bPartner.getContactInfo().setPostalCode(dto.getCodePostal());
        b2bPartner.getContactInfo().setCity(dto.getVille());
        b2bPartner.getContactInfo().setCountry(dto.getCountry());
        
        // Credit Info
        if (b2bPartner.getCreditInfo() == null) {
            b2bPartner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        }
        b2bPartner.getCreditInfo().setCreditLimit(dto.getCreditLimit());
        b2bPartner.getCreditInfo().setCreditRating(dto.getCreditRating());
        b2bPartner.getCreditInfo().setCreditScore(dto.getCreditScore());
        b2bPartner.getCreditInfo().setPaymentHistory(dto.getPaymentHistory());
        b2bPartner.getCreditInfo().setOutstandingBalance(dto.getOutstandingBalance());
        b2bPartner.getCreditInfo().setLastPaymentDate(dto.getLastPaymentDate());
        b2bPartner.getCreditInfo().setPaymentTermDays(dto.getPaymentTermDays());
        b2bPartner.getCreditInfo().setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        b2bPartner.getCreditInfo().setBankAccountInfo(dto.getBankAccountInfo());
        
        // Loyalty Info
        if (b2bPartner.getLoyaltyInfo() == null) {
            b2bPartner.setLoyaltyInfo(new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo());
        }
        b2bPartner.getLoyaltyInfo().setVip(dto.isVip());
        b2bPartner.getLoyaltyInfo().setLoyaltyPoints(dto.getLoyaltyPoints());
        b2bPartner.getLoyaltyInfo().setLastOrderDate(dto.getLastOrderDate());
        b2bPartner.getLoyaltyInfo().setTotalOrders(dto.getTotalOrders());
        b2bPartner.getLoyaltyInfo().setTotalSpent(dto.getTotalSpent());
        b2bPartner.getLoyaltyInfo().setAverageOrderValue(dto.getAverageOrderValue());
        b2bPartner.getLoyaltyInfo().setPartnerSince(dto.getCustomerSince());
        
        // Delivery Preference
        if (b2bPartner.getDeliveryPreference() == null) {
            b2bPartner.setDeliveryPreference(new ma.foodplus.ordering.system.partner.domain.DeliveryPreference());
        }
        b2bPartner.getDeliveryPreference().setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        b2bPartner.getDeliveryPreference().setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        b2bPartner.getDeliveryPreference().setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Audit Info
        if (b2bPartner.getAuditInfo() == null) {
            b2bPartner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        b2bPartner.getAuditInfo().setNotes(dto.getNotes());
        b2bPartner.getAuditInfo().setActive(dto.isActive());
        b2bPartner.getAuditInfo().setLastActivityDate(dto.getLastActivityDate());
        b2bPartner.getAuditInfo().setCreatedBy(dto.getCreatedBy());
        b2bPartner.getAuditInfo().setUpdatedBy(dto.getUpdatedBy());
        b2bPartner.getAuditInfo().setCreatedAt(dto.getCreatedAt());
        b2bPartner.getAuditInfo().setUpdatedAt(dto.getUpdatedAt());
    }
    
    public List<B2BPartnerDTO> toDTOList(List<B2BPartner> b2bPartners) {
        if (b2bPartners == null) return null;
        List<B2BPartnerDTO> list = new ArrayList<>();
        for (B2BPartner p : b2bPartners) {
            list.add(toDTO(p));
        }
        return list;
    }
    
    // ========== Compatibility with Generic PartnerDTO ==========
    
    /**
     * Maps B2BPartner to generic PartnerDTO for backward compatibility.
     */
    public PartnerDTO toPartnerDTO(B2BPartner b2bPartner) {
        if (b2bPartner == null) return null;
        
        PartnerDTO dto = new PartnerDTO();
        
        // Base partner fields
        dto.setId(b2bPartner.getId());
        dto.setCtNum(b2bPartner.getCtNum());
        dto.setIce(b2bPartner.getIce());
        dto.setDescription(b2bPartner.getDescription());
        dto.setPartnerType(PartnerType.B2B);
        dto.setCategoryTarifId(b2bPartner.getCateTarif());
        
        // Company Info (flattened)
        if (b2bPartner.getCompanyInfo() != null) {
            dto.setCompanyName(b2bPartner.getCompanyInfo().getCompanyName());
            dto.setLegalForm(b2bPartner.getCompanyInfo().getLegalForm());
            dto.setRegistrationNumber(b2bPartner.getCompanyInfo().getRegistrationNumber());
            dto.setTaxId(b2bPartner.getCompanyInfo().getTaxId());
            dto.setVatNumber(b2bPartner.getCompanyInfo().getVatNumber());
            dto.setBusinessActivity(b2bPartner.getCompanyInfo().getBusinessActivity());
            dto.setAnnualTurnover(b2bPartner.getCompanyInfo().getAnnualTurnover());
            dto.setNumberOfEmployees(b2bPartner.getCompanyInfo().getNumberOfEmployees());
        }
        
        // Contract Info (flattened)
        if (b2bPartner.getContractInfo() != null) {
            dto.setContractNumber(b2bPartner.getContractInfo().getContractNumber());
            dto.setContractStartDate(b2bPartner.getContractInfo().getContractStartDate());
            dto.setContractEndDate(b2bPartner.getContractInfo().getContractEndDate());
            dto.setContractType(b2bPartner.getContractInfo().getContractType());
            dto.setContractTerms(b2bPartner.getContractInfo().getContractTerms());
            dto.setPaymentTerms(b2bPartner.getContractInfo().getPaymentTerms());
            dto.setDeliveryTerms(b2bPartner.getContractInfo().getDeliveryTerms());
            dto.setSpecialConditions(b2bPartner.getContractInfo().getSpecialConditions());
        }
        
        // Contact Info
        if (b2bPartner.getContactInfo() != null) {
            dto.setTelephone(b2bPartner.getContactInfo().getTelephone());
            dto.setEmail(b2bPartner.getContactInfo().getEmail());
            dto.setAddress(b2bPartner.getContactInfo().getAddress());
            dto.setCodePostal(b2bPartner.getContactInfo().getPostalCode());
            dto.setVille(b2bPartner.getContactInfo().getCity());
            dto.setCountry(b2bPartner.getContactInfo().getCountry());
        }
        
        // Credit Info
        if (b2bPartner.getCreditInfo() != null) {
            dto.setCreditLimit(b2bPartner.getCreditInfo().getCreditLimit());
            dto.setCreditRating(b2bPartner.getCreditInfo().getCreditRating());
            dto.setCreditScore(b2bPartner.getCreditInfo().getCreditScore());
            dto.setPaymentHistory(b2bPartner.getCreditInfo().getPaymentHistory());
            dto.setOutstandingBalance(b2bPartner.getCreditInfo().getOutstandingBalance());
            dto.setLastPaymentDate(b2bPartner.getCreditInfo().getLastPaymentDate());
            dto.setPaymentTermDays(b2bPartner.getCreditInfo().getPaymentTermDays());
            dto.setPreferredPaymentMethod(b2bPartner.getCreditInfo().getPreferredPaymentMethod());
            dto.setBankAccountInfo(b2bPartner.getCreditInfo().getBankAccountInfo());
        }
        
        // Loyalty Info
        if (b2bPartner.getLoyaltyInfo() != null) {
            dto.setVip(b2bPartner.getLoyaltyInfo().isVip());
            dto.setLoyaltyPoints(b2bPartner.getLoyaltyInfo().getLoyaltyPoints());
            dto.setLastOrderDate(b2bPartner.getLoyaltyInfo().getLastOrderDate());
            dto.setTotalOrders(b2bPartner.getLoyaltyInfo().getTotalOrders());
            dto.setTotalSpent(b2bPartner.getLoyaltyInfo().getTotalSpent());
            dto.setAverageOrderValue(b2bPartner.getLoyaltyInfo().getAverageOrderValue());
            dto.setCustomerSince(b2bPartner.getLoyaltyInfo().getPartnerSince());
        }
        
        // Delivery Preference
        if (b2bPartner.getDeliveryPreference() != null) {
            dto.setPreferredDeliveryTime(b2bPartner.getDeliveryPreference().getPreferredDeliveryTime());
            dto.setPreferredDeliveryDays(b2bPartner.getDeliveryPreference().getPreferredDeliveryDays());
            dto.setSpecialHandlingInstructions(b2bPartner.getDeliveryPreference().getSpecialHandlingInstructions());
        }
        
        // Audit Info
        if (b2bPartner.getAuditInfo() != null) {
            dto.setNotes(b2bPartner.getAuditInfo().getNotes());
            dto.setActive(b2bPartner.getAuditInfo().isActive());
            dto.setLastActivityDate(b2bPartner.getAuditInfo().getLastActivityDate());
            dto.setCreatedBy(b2bPartner.getAuditInfo().getCreatedBy());
            dto.setUpdatedBy(b2bPartner.getAuditInfo().getUpdatedBy());
            dto.setCreatedAt(b2bPartner.getAuditInfo().getCreatedAt());
            dto.setUpdatedAt(b2bPartner.getAuditInfo().getUpdatedAt());
        }
        
        return dto;
    }
} 