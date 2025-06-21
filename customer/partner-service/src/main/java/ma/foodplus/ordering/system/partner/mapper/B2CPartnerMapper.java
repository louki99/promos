package ma.foodplus.ordering.system.partner.mapper;

import ma.foodplus.ordering.system.partner.domain.B2CPartner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.dto.B2CPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Manual mapper for B2C Partner entities and DTOs.
 * 
 * <p>This mapper handles the conversion between B2CPartner entities and B2CPartnerDTO objects,
 * including all B2C-specific fields such as personal information and marketing preferences.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Component
public class B2CPartnerMapper {
    
    public B2CPartnerDTO toDTO(B2CPartner b2cPartner) {
        if (b2cPartner == null) return null;
        
        B2CPartnerDTO dto = new B2CPartnerDTO();
        
        // Base partner fields
        dto.setId(b2cPartner.getId());
        dto.setCtNum(b2cPartner.getCtNum());
        dto.setIce(b2cPartner.getIce());
        dto.setDescription(b2cPartner.getDescription());
        dto.setPartnerType(PartnerType.B2C);
        dto.setCategoryTarifId(b2cPartner.getCateTarif());
        
        // B2C-specific fields
        dto.setPersonalIdNumber(b2cPartner.getPersonalIdNumber());
        dto.setDateOfBirth(b2cPartner.getDateOfBirth());
        dto.setPreferredLanguage(b2cPartner.getPreferredLanguage());
        dto.setMarketingConsent(b2cPartner.getMarketingConsent());
        
        // Contact Info
        if (b2cPartner.getContactInfo() != null) {
            dto.setTelephone(b2cPartner.getContactInfo().getTelephone());
            dto.setEmail(b2cPartner.getContactInfo().getEmail());
            dto.setAddress(b2cPartner.getContactInfo().getAddress());
            dto.setCodePostal(b2cPartner.getContactInfo().getPostalCode());
            dto.setVille(b2cPartner.getContactInfo().getCity());
            dto.setCountry(b2cPartner.getContactInfo().getCountry());
        }
        
        // Credit Info
        if (b2cPartner.getCreditInfo() != null) {
            dto.setCreditLimit(b2cPartner.getCreditInfo().getCreditLimit());
            dto.setCreditRating(b2cPartner.getCreditInfo().getCreditRating());
            dto.setCreditScore(b2cPartner.getCreditInfo().getCreditScore());
            dto.setPaymentHistory(b2cPartner.getCreditInfo().getPaymentHistory());
            dto.setOutstandingBalance(b2cPartner.getCreditInfo().getOutstandingBalance());
            dto.setLastPaymentDate(b2cPartner.getCreditInfo().getLastPaymentDate());
            dto.setPaymentTermDays(b2cPartner.getCreditInfo().getPaymentTermDays());
            dto.setPreferredPaymentMethod(b2cPartner.getCreditInfo().getPreferredPaymentMethod());
            dto.setBankAccountInfo(b2cPartner.getCreditInfo().getBankAccountInfo());
        }
        
        // Loyalty Info
        if (b2cPartner.getLoyaltyInfo() != null) {
            dto.setVip(b2cPartner.getLoyaltyInfo().isVip());
            dto.setLoyaltyPoints(b2cPartner.getLoyaltyInfo().getLoyaltyPoints());
            dto.setLastOrderDate(b2cPartner.getLoyaltyInfo().getLastOrderDate());
            dto.setTotalOrders(b2cPartner.getLoyaltyInfo().getTotalOrders());
            dto.setTotalSpent(b2cPartner.getLoyaltyInfo().getTotalSpent());
            dto.setAverageOrderValue(b2cPartner.getLoyaltyInfo().getAverageOrderValue());
            dto.setCustomerSince(b2cPartner.getLoyaltyInfo().getPartnerSince());
        }
        
        // Delivery Preference
        if (b2cPartner.getDeliveryPreference() != null) {
            dto.setPreferredDeliveryTime(b2cPartner.getDeliveryPreference().getPreferredDeliveryTime());
            dto.setPreferredDeliveryDays(b2cPartner.getDeliveryPreference().getPreferredDeliveryDays());
            dto.setSpecialHandlingInstructions(b2cPartner.getDeliveryPreference().getSpecialHandlingInstructions());
        }
        
        // Audit Info
        if (b2cPartner.getAuditInfo() != null) {
            dto.setNotes(b2cPartner.getAuditInfo().getNotes());
            dto.setActive(b2cPartner.getAuditInfo().isActive());
            dto.setLastActivityDate(b2cPartner.getAuditInfo().getLastActivityDate());
            dto.setCreatedBy(b2cPartner.getAuditInfo().getCreatedBy());
            dto.setUpdatedBy(b2cPartner.getAuditInfo().getUpdatedBy());
            dto.setCreatedAt(b2cPartner.getAuditInfo().getCreatedAt());
            dto.setUpdatedAt(b2cPartner.getAuditInfo().getUpdatedAt());
        }
        
        return dto;
    }
    
    public B2CPartner toEntity(B2CPartnerDTO dto) {
        if (dto == null) return null;
        
        B2CPartner b2cPartner = new B2CPartner();
        
        // Base partner fields
        b2cPartner.setCtNum(dto.getCtNum());
        b2cPartner.setIce(dto.getIce());
        b2cPartner.setDescription(dto.getDescription());
        b2cPartner.setCateTarif(dto.getCategoryTarifId());
        
        // B2C-specific fields
        b2cPartner.setPersonalIdNumber(dto.getPersonalIdNumber());
        b2cPartner.setDateOfBirth(dto.getDateOfBirth());
        b2cPartner.setPreferredLanguage(dto.getPreferredLanguage());
        b2cPartner.setMarketingConsent(dto.getMarketingConsent());
        
        // Contact Info
        if (dto.getTelephone() != null || dto.getEmail() != null) {
            ma.foodplus.ordering.system.partner.domain.ContactInfo contactInfo = new ma.foodplus.ordering.system.partner.domain.ContactInfo();
            contactInfo.setTelephone(dto.getTelephone());
            contactInfo.setEmail(dto.getEmail());
            contactInfo.setAddress(dto.getAddress());
            contactInfo.setPostalCode(dto.getCodePostal());
            contactInfo.setCity(dto.getVille());
            contactInfo.setCountry(dto.getCountry());
            b2cPartner.setContactInfo(contactInfo);
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
            b2cPartner.setCreditInfo(creditInfo);
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
            b2cPartner.setLoyaltyInfo(loyaltyInfo);
        }
        
        // Delivery Preference
        if (dto.getPreferredDeliveryTime() != null || dto.getPreferredDeliveryDays() != null) {
            ma.foodplus.ordering.system.partner.domain.DeliveryPreference deliveryPreference = new ma.foodplus.ordering.system.partner.domain.DeliveryPreference();
            deliveryPreference.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
            deliveryPreference.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
            deliveryPreference.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
            b2cPartner.setDeliveryPreference(deliveryPreference);
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
        b2cPartner.setAuditInfo(auditInfo);
        
        return b2cPartner;
    }
    
    public void updateEntityFromDTO(B2CPartnerDTO dto, B2CPartner b2cPartner) {
        if (dto == null || b2cPartner == null) return;
        
        // Base partner fields
        b2cPartner.setCtNum(dto.getCtNum());
        b2cPartner.setIce(dto.getIce());
        b2cPartner.setDescription(dto.getDescription());
        b2cPartner.setCateTarif(dto.getCategoryTarifId());
        
        // B2C-specific fields
        b2cPartner.setPersonalIdNumber(dto.getPersonalIdNumber());
        b2cPartner.setDateOfBirth(dto.getDateOfBirth());
        b2cPartner.setPreferredLanguage(dto.getPreferredLanguage());
        b2cPartner.setMarketingConsent(dto.getMarketingConsent());
        
        // Contact Info
        if (b2cPartner.getContactInfo() == null) {
            b2cPartner.setContactInfo(new ma.foodplus.ordering.system.partner.domain.ContactInfo());
        }
        b2cPartner.getContactInfo().setTelephone(dto.getTelephone());
        b2cPartner.getContactInfo().setEmail(dto.getEmail());
        b2cPartner.getContactInfo().setAddress(dto.getAddress());
        b2cPartner.getContactInfo().setPostalCode(dto.getCodePostal());
        b2cPartner.getContactInfo().setCity(dto.getVille());
        b2cPartner.getContactInfo().setCountry(dto.getCountry());
        
        // Credit Info
        if (b2cPartner.getCreditInfo() == null) {
            b2cPartner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        }
        b2cPartner.getCreditInfo().setCreditLimit(dto.getCreditLimit());
        b2cPartner.getCreditInfo().setCreditRating(dto.getCreditRating());
        b2cPartner.getCreditInfo().setCreditScore(dto.getCreditScore());
        b2cPartner.getCreditInfo().setPaymentHistory(dto.getPaymentHistory());
        b2cPartner.getCreditInfo().setOutstandingBalance(dto.getOutstandingBalance());
        b2cPartner.getCreditInfo().setLastPaymentDate(dto.getLastPaymentDate());
        b2cPartner.getCreditInfo().setPaymentTermDays(dto.getPaymentTermDays());
        b2cPartner.getCreditInfo().setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        b2cPartner.getCreditInfo().setBankAccountInfo(dto.getBankAccountInfo());
        
        // Loyalty Info
        if (b2cPartner.getLoyaltyInfo() == null) {
            b2cPartner.setLoyaltyInfo(new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo());
        }
        b2cPartner.getLoyaltyInfo().setVip(dto.isVip());
        b2cPartner.getLoyaltyInfo().setLoyaltyPoints(dto.getLoyaltyPoints());
        b2cPartner.getLoyaltyInfo().setLastOrderDate(dto.getLastOrderDate());
        b2cPartner.getLoyaltyInfo().setTotalOrders(dto.getTotalOrders());
        b2cPartner.getLoyaltyInfo().setTotalSpent(dto.getTotalSpent());
        b2cPartner.getLoyaltyInfo().setAverageOrderValue(dto.getAverageOrderValue());
        b2cPartner.getLoyaltyInfo().setPartnerSince(dto.getCustomerSince());
        
        // Delivery Preference
        if (b2cPartner.getDeliveryPreference() == null) {
            b2cPartner.setDeliveryPreference(new ma.foodplus.ordering.system.partner.domain.DeliveryPreference());
        }
        b2cPartner.getDeliveryPreference().setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        b2cPartner.getDeliveryPreference().setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        b2cPartner.getDeliveryPreference().setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Audit Info
        if (b2cPartner.getAuditInfo() == null) {
            b2cPartner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        }
        b2cPartner.getAuditInfo().setNotes(dto.getNotes());
        b2cPartner.getAuditInfo().setActive(dto.isActive());
        b2cPartner.getAuditInfo().setLastActivityDate(dto.getLastActivityDate());
        b2cPartner.getAuditInfo().setCreatedBy(dto.getCreatedBy());
        b2cPartner.getAuditInfo().setUpdatedBy(dto.getUpdatedBy());
        b2cPartner.getAuditInfo().setCreatedAt(dto.getCreatedAt());
        b2cPartner.getAuditInfo().setUpdatedAt(dto.getUpdatedAt());
    }
    
    public List<B2CPartnerDTO> toDTOList(List<B2CPartner> b2cPartners) {
        if (b2cPartners == null) return null;
        List<B2CPartnerDTO> list = new ArrayList<>();
        for (B2CPartner p : b2cPartners) {
            list.add(toDTO(p));
        }
        return list;
    }
    
    // ========== Compatibility with Generic PartnerDTO ==========
    
    /**
     * Maps B2CPartner to generic PartnerDTO for backward compatibility.
     */
    public PartnerDTO toPartnerDTO(B2CPartner b2cPartner) {
        if (b2cPartner == null) return null;
        
        PartnerDTO dto = new PartnerDTO();
        
        // Base partner fields
        dto.setId(b2cPartner.getId());
        dto.setCtNum(b2cPartner.getCtNum());
        dto.setIce(b2cPartner.getIce());
        dto.setDescription(b2cPartner.getDescription());
        dto.setPartnerType(PartnerType.B2C);
        dto.setCategoryTarifId(b2cPartner.getCateTarif());
        
        // B2C-specific fields (flattened)
        dto.setPersonalIdNumber(b2cPartner.getPersonalIdNumber());
        dto.setDateOfBirth(b2cPartner.getDateOfBirth());
        dto.setPreferredLanguage(b2cPartner.getPreferredLanguage());
        dto.setMarketingConsent(b2cPartner.getMarketingConsent());
        
        // Contact Info
        if (b2cPartner.getContactInfo() != null) {
            dto.setTelephone(b2cPartner.getContactInfo().getTelephone());
            dto.setEmail(b2cPartner.getContactInfo().getEmail());
            dto.setAddress(b2cPartner.getContactInfo().getAddress());
            dto.setCodePostal(b2cPartner.getContactInfo().getPostalCode());
            dto.setVille(b2cPartner.getContactInfo().getCity());
            dto.setCountry(b2cPartner.getContactInfo().getCountry());
        }
        
        // Credit Info
        if (b2cPartner.getCreditInfo() != null) {
            dto.setCreditLimit(b2cPartner.getCreditInfo().getCreditLimit());
            dto.setCreditRating(b2cPartner.getCreditInfo().getCreditRating());
            dto.setCreditScore(b2cPartner.getCreditInfo().getCreditScore());
            dto.setPaymentHistory(b2cPartner.getCreditInfo().getPaymentHistory());
            dto.setOutstandingBalance(b2cPartner.getCreditInfo().getOutstandingBalance());
            dto.setLastPaymentDate(b2cPartner.getCreditInfo().getLastPaymentDate());
            dto.setPaymentTermDays(b2cPartner.getCreditInfo().getPaymentTermDays());
            dto.setPreferredPaymentMethod(b2cPartner.getCreditInfo().getPreferredPaymentMethod());
            dto.setBankAccountInfo(b2cPartner.getCreditInfo().getBankAccountInfo());
        }
        
        // Loyalty Info
        if (b2cPartner.getLoyaltyInfo() != null) {
            dto.setVip(b2cPartner.getLoyaltyInfo().isVip());
            dto.setLoyaltyPoints(b2cPartner.getLoyaltyInfo().getLoyaltyPoints());
            dto.setLastOrderDate(b2cPartner.getLoyaltyInfo().getLastOrderDate());
            dto.setTotalOrders(b2cPartner.getLoyaltyInfo().getTotalOrders());
            dto.setTotalSpent(b2cPartner.getLoyaltyInfo().getTotalSpent());
            dto.setAverageOrderValue(b2cPartner.getLoyaltyInfo().getAverageOrderValue());
            dto.setCustomerSince(b2cPartner.getLoyaltyInfo().getPartnerSince());
        }
        
        // Delivery Preference
        if (b2cPartner.getDeliveryPreference() != null) {
            dto.setPreferredDeliveryTime(b2cPartner.getDeliveryPreference().getPreferredDeliveryTime());
            dto.setPreferredDeliveryDays(b2cPartner.getDeliveryPreference().getPreferredDeliveryDays());
            dto.setSpecialHandlingInstructions(b2cPartner.getDeliveryPreference().getSpecialHandlingInstructions());
        }
        
        // Audit Info
        if (b2cPartner.getAuditInfo() != null) {
            dto.setNotes(b2cPartner.getAuditInfo().getNotes());
            dto.setActive(b2cPartner.getAuditInfo().isActive());
            dto.setLastActivityDate(b2cPartner.getAuditInfo().getLastActivityDate());
            dto.setCreatedBy(b2cPartner.getAuditInfo().getCreatedBy());
            dto.setUpdatedBy(b2cPartner.getAuditInfo().getUpdatedBy());
            dto.setCreatedAt(b2cPartner.getAuditInfo().getCreatedAt());
            dto.setUpdatedAt(b2cPartner.getAuditInfo().getUpdatedAt());
        }
        
        return dto;
    }
} 