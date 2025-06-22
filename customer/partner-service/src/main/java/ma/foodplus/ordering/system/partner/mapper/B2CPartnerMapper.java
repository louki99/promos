package ma.foodplus.ordering.system.partner.mapper;

import ma.foodplus.ordering.system.partner.domain.B2CPartner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.dto.B2CPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manual mapper for B2C Partner entities and DTOs.
 * 
 * <p>This mapper handles the conversion between B2CPartner entities and B2CPartnerDTO objects,
 * including all B2C-specific fields such as personal information and preferences.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Component
public class B2CPartnerMapper {
    
    // ========== B2C-Specific Mappings ==========
    
    public B2CPartnerDTO toDTO(B2CPartner b2cPartner) {
        if (b2cPartner == null) return null;
        
        B2CPartnerDTO dto = new B2CPartnerDTO();
        
        // Basic partner information
        dto.setId(b2cPartner.getId());
        dto.setCtNum(b2cPartner.getCtNum());
        dto.setIce(b2cPartner.getIce());
        dto.setDescription(b2cPartner.getDescription());
        dto.setPartnerType(b2cPartner.getPartnerType());
        dto.setCategoryTarifId(b2cPartner.getCateTarif());
        
        // B2C-specific fields
        dto.setPersonalIdNumber(b2cPartner.getPersonalIdNumber());
        dto.setDateOfBirth(b2cPartner.getDateOfBirth());
        dto.setPreferredLanguage(b2cPartner.getPreferredLanguage());
        dto.setMarketingConsent(b2cPartner.getMarketingConsent());
        
        // Contact Information (Individual columns)
        dto.setTelephone(b2cPartner.getTelephone());
        dto.setEmail(b2cPartner.getEmail());
        dto.setAddress(b2cPartner.getAddress());
        dto.setCodePostal(b2cPartner.getPostalCode());
        dto.setVille(b2cPartner.getCity());
        dto.setCountry(b2cPartner.getCountry());
        
        // Credit Information (Individual columns)
        dto.setCreditLimit(b2cPartner.getCreditLimit());
        dto.setOutstandingBalance(b2cPartner.getOutstandingBalance());
        dto.setCreditRating(b2cPartner.getCreditRating());
        dto.setCreditScore(b2cPartner.getCreditScore());
        dto.setPaymentTermDays(b2cPartner.getPaymentTermDays());
        dto.setPreferredPaymentMethod(b2cPartner.getPreferredPaymentMethod());
        dto.setBankAccountInfo(b2cPartner.getBankAccountInfo());
        dto.setLastPaymentDate(b2cPartner.getLastPaymentDate());
        dto.setPaymentHistory(b2cPartner.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        dto.setVip(b2cPartner.getIsVip() != null && b2cPartner.getIsVip());
        dto.setLoyaltyPoints(b2cPartner.getLoyaltyPoints());
        dto.setLastOrderDate(b2cPartner.getLastOrderDate());
        dto.setTotalOrders(b2cPartner.getTotalOrders());
        dto.setTotalSpent(b2cPartner.getTotalSpent());
        dto.setAverageOrderValue(b2cPartner.getAverageOrderValue());
        dto.setCustomerSince(b2cPartner.getPartnerSince());
        
        // Delivery Preference (Individual columns)
        dto.setPreferredDeliveryTime(b2cPartner.getPreferredDeliveryTime());
        dto.setPreferredDeliveryDays(b2cPartner.getPreferredDeliveryDays());
        dto.setSpecialHandlingInstructions(b2cPartner.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        dto.setNotes(b2cPartner.getNotes());
        dto.setActive(b2cPartner.getIsActive() != null && b2cPartner.getIsActive());
        dto.setLastActivityDate(b2cPartner.getLastActivityDate());
        dto.setCreatedBy(b2cPartner.getCreatedBy());
        dto.setUpdatedBy(b2cPartner.getUpdatedBy());
        dto.setCreatedAt(b2cPartner.getCreatedAt());
        dto.setUpdatedAt(b2cPartner.getUpdatedAt());
        
        return dto;
    }

    public B2CPartner toEntity(B2CPartnerDTO dto) {
        if (dto == null) return null;
        
        B2CPartner b2cPartner = new B2CPartner();
        
        // Basic partner information
        b2cPartner.setId(dto.getId());
        b2cPartner.setCtNum(dto.getCtNum());
        b2cPartner.setIce(dto.getIce());
        b2cPartner.setDescription(dto.getDescription());
        b2cPartner.setCateTarif(dto.getCategoryTarifId());
        
        // B2C-specific fields
        b2cPartner.setPersonalIdNumber(dto.getPersonalIdNumber());
        b2cPartner.setDateOfBirth(dto.getDateOfBirth());
        b2cPartner.setPreferredLanguage(dto.getPreferredLanguage());
        b2cPartner.setMarketingConsent(dto.getMarketingConsent());
        
        // Contact Information (Individual columns)
        b2cPartner.setTelephone(dto.getTelephone());
        b2cPartner.setEmail(dto.getEmail());
        b2cPartner.setAddress(dto.getAddress());
        b2cPartner.setCity(dto.getVille());
        b2cPartner.setCountry(dto.getCountry());
        b2cPartner.setPostalCode(dto.getCodePostal());
        
        // Credit Information (Individual columns)
        b2cPartner.setCreditLimit(dto.getCreditLimit());
        b2cPartner.setOutstandingBalance(dto.getOutstandingBalance());
        b2cPartner.setCreditRating(dto.getCreditRating());
        b2cPartner.setCreditScore(dto.getCreditScore());
        b2cPartner.setPaymentTermDays(dto.getPaymentTermDays());
        b2cPartner.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        b2cPartner.setBankAccountInfo(dto.getBankAccountInfo());
        b2cPartner.setLastPaymentDate(dto.getLastPaymentDate());
        b2cPartner.setPaymentHistory(dto.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        b2cPartner.setIsVip(dto.isVip());
        b2cPartner.setLoyaltyPoints(dto.getLoyaltyPoints());
        b2cPartner.setLastOrderDate(dto.getLastOrderDate());
        b2cPartner.setTotalOrders(dto.getTotalOrders());
        b2cPartner.setTotalSpent(dto.getTotalSpent());
        b2cPartner.setAverageOrderValue(dto.getAverageOrderValue());
        b2cPartner.setPartnerSince(dto.getCustomerSince());
        
        // Delivery Preference (Individual columns)
        b2cPartner.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        b2cPartner.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        b2cPartner.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        b2cPartner.setNotes(dto.getNotes());
        b2cPartner.setIsActive(dto.isActive());
        b2cPartner.setLastActivityDate(dto.getLastActivityDate());
        b2cPartner.setCreatedBy(dto.getCreatedBy());
        b2cPartner.setUpdatedBy(dto.getUpdatedBy());
        b2cPartner.setCreatedAt(dto.getCreatedAt());
        b2cPartner.setUpdatedAt(dto.getUpdatedAt());
        
        return b2cPartner;
    }

    public void updateEntityFromDTO(B2CPartnerDTO dto, B2CPartner b2cPartner) {
        if (dto == null || b2cPartner == null) return;
        
        // Update basic partner information
        b2cPartner.setCtNum(dto.getCtNum());
        b2cPartner.setIce(dto.getIce());
        b2cPartner.setDescription(dto.getDescription());
        b2cPartner.setCateTarif(dto.getCategoryTarifId());
        
        // Update B2C-specific fields
        b2cPartner.setPersonalIdNumber(dto.getPersonalIdNumber());
        b2cPartner.setDateOfBirth(dto.getDateOfBirth());
        b2cPartner.setPreferredLanguage(dto.getPreferredLanguage());
        b2cPartner.setMarketingConsent(dto.getMarketingConsent());
        
        // Update Contact Information (Individual columns)
        b2cPartner.setTelephone(dto.getTelephone());
        b2cPartner.setEmail(dto.getEmail());
        b2cPartner.setAddress(dto.getAddress());
        b2cPartner.setCity(dto.getVille());
        b2cPartner.setCountry(dto.getCountry());
        b2cPartner.setPostalCode(dto.getCodePostal());
        
        // Update Credit Information (Individual columns)
        b2cPartner.setCreditLimit(dto.getCreditLimit());
        b2cPartner.setOutstandingBalance(dto.getOutstandingBalance());
        b2cPartner.setCreditRating(dto.getCreditRating());
        b2cPartner.setCreditScore(dto.getCreditScore());
        b2cPartner.setPaymentTermDays(dto.getPaymentTermDays());
        b2cPartner.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        b2cPartner.setBankAccountInfo(dto.getBankAccountInfo());
        b2cPartner.setLastPaymentDate(dto.getLastPaymentDate());
        b2cPartner.setPaymentHistory(dto.getPaymentHistory());
        
        // Update Loyalty Information (Individual columns)
        b2cPartner.setIsVip(dto.isVip());
        b2cPartner.setLoyaltyPoints(dto.getLoyaltyPoints());
        b2cPartner.setLastOrderDate(dto.getLastOrderDate());
        b2cPartner.setTotalOrders(dto.getTotalOrders());
        b2cPartner.setTotalSpent(dto.getTotalSpent());
        b2cPartner.setAverageOrderValue(dto.getAverageOrderValue());
        b2cPartner.setPartnerSince(dto.getCustomerSince());
        
        // Update Delivery Preference (Individual columns)
        b2cPartner.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        b2cPartner.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        b2cPartner.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        
        // Update Audit Information (Individual columns)
        b2cPartner.setNotes(dto.getNotes());
        b2cPartner.setIsActive(dto.isActive());
        b2cPartner.setLastActivityDate(dto.getLastActivityDate());
        b2cPartner.setCreatedBy(dto.getCreatedBy());
        b2cPartner.setUpdatedBy(dto.getUpdatedBy());
        b2cPartner.setCreatedAt(dto.getCreatedAt());
        b2cPartner.setUpdatedAt(dto.getUpdatedAt());
    }

    public List<B2CPartnerDTO> toDTOList(List<B2CPartner> b2cPartners) {
        if (b2cPartners == null) return null;
        List<B2CPartnerDTO> list = new ArrayList<>();
        for (B2CPartner p : b2cPartners) list.add(toDTO(p));
        return list;
    }

    public PartnerDTO toPartnerDTO(B2CPartner b2cPartner) {
        if (b2cPartner == null) return null;
        
        PartnerDTO dto = new PartnerDTO();
        
        // Basic partner information
        dto.setId(b2cPartner.getId());
        dto.setCtNum(b2cPartner.getCtNum());
        dto.setIce(b2cPartner.getIce());
        dto.setDescription(b2cPartner.getDescription());
        dto.setPartnerType(b2cPartner.getPartnerType());
        dto.setCategoryTarifId(b2cPartner.getCateTarif());
        
        // Contact Information (Individual columns)
        dto.setTelephone(b2cPartner.getTelephone());
        dto.setEmail(b2cPartner.getEmail());
        dto.setAddress(b2cPartner.getAddress());
        dto.setCodePostal(b2cPartner.getPostalCode());
        dto.setVille(b2cPartner.getCity());
        dto.setCountry(b2cPartner.getCountry());
        
        // Credit Information (Individual columns)
        dto.setCreditLimit(b2cPartner.getCreditLimit());
        dto.setOutstandingBalance(b2cPartner.getOutstandingBalance());
        dto.setCreditRating(b2cPartner.getCreditRating());
        dto.setCreditScore(b2cPartner.getCreditScore());
        dto.setPaymentTermDays(b2cPartner.getPaymentTermDays());
        dto.setPreferredPaymentMethod(b2cPartner.getPreferredPaymentMethod());
        dto.setBankAccountInfo(b2cPartner.getBankAccountInfo());
        dto.setLastPaymentDate(b2cPartner.getLastPaymentDate());
        dto.setPaymentHistory(b2cPartner.getPaymentHistory());
        
        // Loyalty Information (Individual columns)
        dto.setVip(b2cPartner.getIsVip() != null && b2cPartner.getIsVip());
        dto.setLoyaltyPoints(b2cPartner.getLoyaltyPoints());
        dto.setLastOrderDate(b2cPartner.getLastOrderDate());
        dto.setTotalOrders(b2cPartner.getTotalOrders());
        dto.setTotalSpent(b2cPartner.getTotalSpent());
        dto.setAverageOrderValue(b2cPartner.getAverageOrderValue());
        dto.setCustomerSince(b2cPartner.getPartnerSince());
        
        // Delivery Preference (Individual columns)
        dto.setPreferredDeliveryTime(b2cPartner.getPreferredDeliveryTime());
        dto.setPreferredDeliveryDays(b2cPartner.getPreferredDeliveryDays());
        dto.setSpecialHandlingInstructions(b2cPartner.getSpecialHandlingInstructions());
        
        // Audit Information (Individual columns)
        dto.setNotes(b2cPartner.getNotes());
        dto.setActive(b2cPartner.getIsActive() != null && b2cPartner.getIsActive());
        dto.setLastActivityDate(b2cPartner.getLastActivityDate());
        dto.setCreatedBy(b2cPartner.getCreatedBy());
        dto.setUpdatedBy(b2cPartner.getUpdatedBy());
        dto.setCreatedAt(b2cPartner.getCreatedAt());
        dto.setUpdatedAt(b2cPartner.getUpdatedAt());
        
        return dto;
    }
} 