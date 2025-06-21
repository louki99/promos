package ma.foodplus.ordering.system.partner.mapper;

import ma.foodplus.ordering.system.partner.domain.B2CPartner;
import ma.foodplus.ordering.system.partner.dto.B2CPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for B2C Partner entities and DTOs.
 * 
 * <p>This mapper handles the conversion between B2CPartner entities and B2CPartnerDTO objects,
 * including all B2C-specific fields such as personal information and marketing preferences.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface B2CPartnerMapper {
    
    // ========== B2C-Specific Mappings ==========
    
    @Mapping(target = "personalIdNumber", source = "personalIdNumber")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "preferredLanguage", source = "preferredLanguage")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    
    // Common field mappings
    @Mapping(target = "telephone", source = "contactInfo.telephone")
    @Mapping(target = "email", source = "contactInfo.email")
    @Mapping(target = "address", source = "contactInfo.address")
    @Mapping(target = "codePostal", source = "contactInfo.postalCode")
    @Mapping(target = "ville", source = "contactInfo.city")
    @Mapping(target = "country", source = "contactInfo.country")
    
    @Mapping(target = "creditLimit", source = "creditInfo.creditLimit")
    @Mapping(target = "creditRating", source = "creditInfo.creditRating")
    @Mapping(target = "creditScore", source = "creditInfo.creditScore")
    @Mapping(target = "paymentHistory", source = "creditInfo.paymentHistory")
    @Mapping(target = "outstandingBalance", source = "creditInfo.outstandingBalance")
    @Mapping(target = "lastPaymentDate", source = "creditInfo.lastPaymentDate")
    @Mapping(target = "paymentTermDays", source = "creditInfo.paymentTermDays")
    @Mapping(target = "preferredPaymentMethod", source = "creditInfo.preferredPaymentMethod")
    @Mapping(target = "bankAccountInfo", source = "creditInfo.bankAccountInfo")
    
    @Mapping(target = "vip", source = "loyaltyInfo.vip")
    @Mapping(target = "loyaltyPoints", source = "loyaltyInfo.loyaltyPoints")
    @Mapping(target = "lastOrderDate", source = "loyaltyInfo.lastOrderDate")
    @Mapping(target = "totalOrders", source = "loyaltyInfo.totalOrders")
    @Mapping(target = "totalSpent", source = "loyaltyInfo.totalSpent")
    @Mapping(target = "averageOrderValue", source = "loyaltyInfo.averageOrderValue")
    @Mapping(target = "customerSince", source = "loyaltyInfo.partnerSince")
    
    @Mapping(target = "preferredDeliveryTime", source = "deliveryPreference.preferredDeliveryTime")
    @Mapping(target = "preferredDeliveryDays", source = "deliveryPreference.preferredDeliveryDays")
    @Mapping(target = "specialHandlingInstructions", source = "deliveryPreference.specialHandlingInstructions")
    
    @Mapping(target = "notes", source = "auditInfo.notes")
    @Mapping(target = "active", source = "auditInfo.active")
    @Mapping(target = "lastActivityDate", source = "auditInfo.lastActivityDate")
    @Mapping(target = "createdBy", source = "auditInfo.createdBy")
    @Mapping(target = "updatedBy", source = "auditInfo.updatedBy")
    @Mapping(target = "createdAt", source = "auditInfo.createdAt")
    @Mapping(target = "updatedAt", source = "auditInfo.updatedAt")
    
    @Mapping(target = "categoryTarifId", source = "cateTarif")
    @Mapping(target = "partnerType", constant = "B2C")
    B2CPartnerDTO toDTO(B2CPartner b2cPartner);
    
    @Mapping(target = "personalIdNumber", source = "personalIdNumber")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "preferredLanguage", source = "preferredLanguage")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    
    // Common field mappings
    @Mapping(target = "contactInfo.telephone", source = "telephone")
    @Mapping(target = "contactInfo.email", source = "email")
    @Mapping(target = "contactInfo.address", source = "address")
    @Mapping(target = "contactInfo.postalCode", source = "codePostal")
    @Mapping(target = "contactInfo.city", source = "ville")
    @Mapping(target = "contactInfo.country", source = "country")
    
    @Mapping(target = "creditInfo.creditLimit", source = "creditLimit")
    @Mapping(target = "creditInfo.creditRating", source = "creditRating")
    @Mapping(target = "creditInfo.creditScore", source = "creditScore")
    @Mapping(target = "creditInfo.paymentHistory", source = "paymentHistory")
    @Mapping(target = "creditInfo.outstandingBalance", source = "outstandingBalance")
    @Mapping(target = "creditInfo.lastPaymentDate", source = "lastPaymentDate")
    @Mapping(target = "creditInfo.paymentTermDays", source = "paymentTermDays")
    @Mapping(target = "creditInfo.preferredPaymentMethod", source = "preferredPaymentMethod")
    @Mapping(target = "creditInfo.bankAccountInfo", source = "bankAccountInfo")
    
    @Mapping(target = "loyaltyInfo.vip", source = "vip")
    @Mapping(target = "loyaltyInfo.loyaltyPoints", source = "loyaltyPoints")
    @Mapping(target = "loyaltyInfo.lastOrderDate", source = "lastOrderDate")
    @Mapping(target = "loyaltyInfo.totalOrders", source = "totalOrders")
    @Mapping(target = "loyaltyInfo.totalSpent", source = "totalSpent")
    @Mapping(target = "loyaltyInfo.averageOrderValue", source = "averageOrderValue")
    @Mapping(target = "loyaltyInfo.partnerSince", source = "customerSince")
    
    @Mapping(target = "deliveryPreference.preferredDeliveryTime", source = "preferredDeliveryTime")
    @Mapping(target = "deliveryPreference.preferredDeliveryDays", source = "preferredDeliveryDays")
    @Mapping(target = "deliveryPreference.specialHandlingInstructions", source = "specialHandlingInstructions")
    
    @Mapping(target = "auditInfo.notes", source = "notes")
    @Mapping(target = "auditInfo.active", source = "active")
    @Mapping(target = "auditInfo.lastActivityDate", source = "lastActivityDate")
    @Mapping(target = "auditInfo.createdBy", source = "createdBy")
    @Mapping(target = "auditInfo.updatedBy", source = "updatedBy")
    @Mapping(target = "auditInfo.createdAt", source = "createdAt")
    @Mapping(target = "auditInfo.updatedAt", source = "updatedAt")
    
    @Mapping(target = "cateTarif", source = "categoryTarifId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "partnerGroups", ignore = true)
    B2CPartner toEntity(B2CPartnerDTO b2cPartnerDTO);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "partnerGroups", ignore = true)
    void updateEntityFromDTO(B2CPartnerDTO b2cPartnerDTO, @MappingTarget B2CPartner b2cPartner);
    
    List<B2CPartnerDTO> toDTOList(List<B2CPartner> b2cPartners);
    
    // ========== Compatibility with Generic PartnerDTO ==========
    
    /**
     * Maps B2CPartner to generic PartnerDTO for backward compatibility.
     */
    @Mapping(target = "personalIdNumber", source = "personalIdNumber")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "preferredLanguage", source = "preferredLanguage")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    
    // Common field mappings
    @Mapping(target = "telephone", source = "contactInfo.telephone")
    @Mapping(target = "email", source = "contactInfo.email")
    @Mapping(target = "address", source = "contactInfo.address")
    @Mapping(target = "codePostal", source = "contactInfo.postalCode")
    @Mapping(target = "ville", source = "contactInfo.city")
    @Mapping(target = "country", source = "contactInfo.country")
    
    @Mapping(target = "creditLimit", source = "creditInfo.creditLimit")
    @Mapping(target = "creditRating", source = "creditInfo.creditRating")
    @Mapping(target = "creditScore", source = "creditInfo.creditScore")
    @Mapping(target = "paymentHistory", source = "creditInfo.paymentHistory")
    @Mapping(target = "outstandingBalance", source = "creditInfo.outstandingBalance")
    @Mapping(target = "lastPaymentDate", source = "creditInfo.lastPaymentDate")
    @Mapping(target = "paymentTermDays", source = "creditInfo.paymentTermDays")
    @Mapping(target = "preferredPaymentMethod", source = "creditInfo.preferredPaymentMethod")
    @Mapping(target = "bankAccountInfo", source = "creditInfo.bankAccountInfo")
    
    @Mapping(target = "vip", source = "loyaltyInfo.vip")
    @Mapping(target = "loyaltyPoints", source = "loyaltyInfo.loyaltyPoints")
    @Mapping(target = "lastOrderDate", source = "loyaltyInfo.lastOrderDate")
    @Mapping(target = "totalOrders", source = "loyaltyInfo.totalOrders")
    @Mapping(target = "totalSpent", source = "loyaltyInfo.totalSpent")
    @Mapping(target = "averageOrderValue", source = "loyaltyInfo.averageOrderValue")
    @Mapping(target = "customerSince", source = "loyaltyInfo.partnerSince")
    
    @Mapping(target = "preferredDeliveryTime", source = "deliveryPreference.preferredDeliveryTime")
    @Mapping(target = "preferredDeliveryDays", source = "deliveryPreference.preferredDeliveryDays")
    @Mapping(target = "specialHandlingInstructions", source = "deliveryPreference.specialHandlingInstructions")
    
    @Mapping(target = "notes", source = "auditInfo.notes")
    @Mapping(target = "active", source = "auditInfo.active")
    @Mapping(target = "lastActivityDate", source = "auditInfo.lastActivityDate")
    @Mapping(target = "createdBy", source = "auditInfo.createdBy")
    @Mapping(target = "updatedBy", source = "auditInfo.updatedBy")
    @Mapping(target = "createdAt", source = "auditInfo.createdAt")
    @Mapping(target = "updatedAt", source = "auditInfo.updatedAt")
    
    @Mapping(target = "categoryTarifId", source = "cateTarif")
    @Mapping(target = "partnerType", constant = "B2C")
    PartnerDTO toPartnerDTO(B2CPartner b2cPartner);
} 