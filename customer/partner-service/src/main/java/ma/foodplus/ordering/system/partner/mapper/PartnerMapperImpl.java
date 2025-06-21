package ma.foodplus.ordering.system.partner.mapper;

import ma.foodplus.ordering.system.partner.domain.Partner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class PartnerMapperImpl {
    public PartnerDTO toDTO(Partner partner) {
        if (partner == null) return null;
        PartnerDTO dto = new PartnerDTO();
        dto.setId(partner.getId());
        dto.setCtNum(partner.getCtNum());
        dto.setIce(partner.getIce());
        dto.setDescription(partner.getDescription());
        dto.setPartnerType(partner.getPartnerType());
        dto.setCategoryTarifId(partner.getCateTarif());
        // Embedded: ContactInfo
        if (partner.getContactInfo() != null) {
            dto.setTelephone(partner.getContactInfo().getTelephone());
            dto.setEmail(partner.getContactInfo().getEmail());
            dto.setAddress(partner.getContactInfo().getAddress());
            dto.setCodePostal(partner.getContactInfo().getPostalCode());
            dto.setVille(partner.getContactInfo().getCity());
            dto.setCountry(partner.getContactInfo().getCountry());
        }
        // Embedded: CreditInfo
        if (partner.getCreditInfo() != null) {
            dto.setCreditLimit(partner.getCreditInfo().getCreditLimit());
            dto.setOutstandingBalance(partner.getCreditInfo().getOutstandingBalance());
            dto.setCreditRating(partner.getCreditInfo().getCreditRating());
            dto.setCreditScore(partner.getCreditInfo().getCreditScore());
            dto.setPaymentTermDays(partner.getCreditInfo().getPaymentTermDays());
            dto.setPreferredPaymentMethod(partner.getCreditInfo().getPreferredPaymentMethod());
            dto.setBankAccountInfo(partner.getCreditInfo().getBankAccountInfo());
            dto.setLastPaymentDate(partner.getCreditInfo().getLastPaymentDate());
            dto.setPaymentHistory(partner.getCreditInfo().getPaymentHistory());
        }
        // Embedded: LoyaltyInfo
        if (partner.getLoyaltyInfo() != null) {
            dto.setVip(partner.getLoyaltyInfo().isVip());
            dto.setLoyaltyPoints(partner.getLoyaltyInfo().getLoyaltyPoints());
            dto.setLastOrderDate(partner.getLoyaltyInfo().getLastOrderDate());
            dto.setTotalOrders(partner.getLoyaltyInfo().getTotalOrders());
            dto.setTotalSpent(partner.getLoyaltyInfo().getTotalSpent());
            dto.setAverageOrderValue(partner.getLoyaltyInfo().getAverageOrderValue());
            dto.setCustomerSince(partner.getLoyaltyInfo().getPartnerSince());
        }
        // Embedded: DeliveryPreference
        if (partner.getDeliveryPreference() != null) {
            dto.setPreferredDeliveryTime(partner.getDeliveryPreference().getPreferredDeliveryTime());
            dto.setPreferredDeliveryDays(partner.getDeliveryPreference().getPreferredDeliveryDays());
            dto.setSpecialHandlingInstructions(partner.getDeliveryPreference().getSpecialHandlingInstructions());
        }
        // Embedded: AuditInfo
        if (partner.getAuditInfo() != null) {
            dto.setNotes(partner.getAuditInfo().getNotes());
            dto.setActive(partner.getAuditInfo().isActive());
            dto.setLastActivityDate(partner.getAuditInfo().getLastActivityDate());
            dto.setCreatedBy(partner.getAuditInfo().getCreatedBy());
            dto.setUpdatedBy(partner.getAuditInfo().getUpdatedBy());
            dto.setCreatedAt(partner.getAuditInfo().getCreatedAt());
            dto.setUpdatedAt(partner.getAuditInfo().getUpdatedAt());
        }
        return dto;
    }

    public Partner toEntity(PartnerDTO dto) {
        if (dto == null) return null;
        Partner partner = new ma.foodplus.ordering.system.partner.domain.B2BPartner(); // Default, override in B2B/B2C/Supplier mappers
        partner.setId(dto.getId());
        partner.setCtNum(dto.getCtNum());
        partner.setIce(dto.getIce());
        partner.setDescription(dto.getDescription());
        partner.setCateTarif(dto.getCategoryTarifId());
        // Embedded: ContactInfo
        if (dto.getTelephone() != null || dto.getEmail() != null || dto.getAddress() != null) {
            ma.foodplus.ordering.system.partner.domain.ContactInfo contactInfo = new ma.foodplus.ordering.system.partner.domain.ContactInfo();
            contactInfo.setTelephone(dto.getTelephone());
            contactInfo.setEmail(dto.getEmail());
            contactInfo.setAddress(dto.getAddress());
            contactInfo.setPostalCode(dto.getCodePostal());
            contactInfo.setCity(dto.getVille());
            contactInfo.setCountry(dto.getCountry());
            partner.setContactInfo(contactInfo);
        }
        // Embedded: CreditInfo
        if (dto.getCreditLimit() != null || dto.getOutstandingBalance() != null) {
            ma.foodplus.ordering.system.partner.domain.CreditInfo creditInfo = new ma.foodplus.ordering.system.partner.domain.CreditInfo();
            creditInfo.setCreditLimit(dto.getCreditLimit());
            creditInfo.setOutstandingBalance(dto.getOutstandingBalance());
            creditInfo.setCreditRating(dto.getCreditRating());
            creditInfo.setCreditScore(dto.getCreditScore());
            creditInfo.setPaymentTermDays(dto.getPaymentTermDays());
            creditInfo.setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
            creditInfo.setBankAccountInfo(dto.getBankAccountInfo());
            creditInfo.setLastPaymentDate(dto.getLastPaymentDate());
            creditInfo.setPaymentHistory(dto.getPaymentHistory());
            partner.setCreditInfo(creditInfo);
        }
        // Embedded: LoyaltyInfo
        if (dto.getLoyaltyPoints() != null || dto.isVip()) {
            ma.foodplus.ordering.system.partner.domain.LoyaltyInfo loyaltyInfo = new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo();
            loyaltyInfo.setVip(dto.isVip());
            loyaltyInfo.setLoyaltyPoints(dto.getLoyaltyPoints());
            loyaltyInfo.setLastOrderDate(dto.getLastOrderDate());
            loyaltyInfo.setTotalOrders(dto.getTotalOrders());
            loyaltyInfo.setTotalSpent(dto.getTotalSpent());
            loyaltyInfo.setAverageOrderValue(dto.getAverageOrderValue());
            loyaltyInfo.setPartnerSince(dto.getCustomerSince());
            partner.setLoyaltyInfo(loyaltyInfo);
        }
        // Embedded: DeliveryPreference
        if (dto.getPreferredDeliveryTime() != null || dto.getPreferredDeliveryDays() != null) {
            ma.foodplus.ordering.system.partner.domain.DeliveryPreference deliveryPreference = new ma.foodplus.ordering.system.partner.domain.DeliveryPreference();
            deliveryPreference.setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
            deliveryPreference.setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
            deliveryPreference.setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
            partner.setDeliveryPreference(deliveryPreference);
        }
        // Embedded: AuditInfo
        ma.foodplus.ordering.system.partner.domain.AuditInfo auditInfo = new ma.foodplus.ordering.system.partner.domain.AuditInfo();
        auditInfo.setNotes(dto.getNotes());
        auditInfo.setActive(dto.isActive());
        auditInfo.setLastActivityDate(dto.getLastActivityDate());
        auditInfo.setCreatedBy(dto.getCreatedBy());
        auditInfo.setUpdatedBy(dto.getUpdatedBy());
        auditInfo.setCreatedAt(dto.getCreatedAt());
        auditInfo.setUpdatedAt(dto.getUpdatedAt());
        partner.setAuditInfo(auditInfo);
        return partner;
    }

    public void updateEntityFromDTO(PartnerDTO dto, Partner partner) {
        if (dto == null || partner == null) return;
        partner.setCtNum(dto.getCtNum());
        partner.setIce(dto.getIce());
        partner.setDescription(dto.getDescription());
        partner.setCateTarif(dto.getCategoryTarifId());
        // ContactInfo
        if (partner.getContactInfo() == null) partner.setContactInfo(new ma.foodplus.ordering.system.partner.domain.ContactInfo());
        partner.getContactInfo().setTelephone(dto.getTelephone());
        partner.getContactInfo().setEmail(dto.getEmail());
        partner.getContactInfo().setAddress(dto.getAddress());
        partner.getContactInfo().setPostalCode(dto.getCodePostal());
        partner.getContactInfo().setCity(dto.getVille());
        partner.getContactInfo().setCountry(dto.getCountry());
        // CreditInfo
        if (partner.getCreditInfo() == null) partner.setCreditInfo(new ma.foodplus.ordering.system.partner.domain.CreditInfo());
        partner.getCreditInfo().setCreditLimit(dto.getCreditLimit());
        partner.getCreditInfo().setOutstandingBalance(dto.getOutstandingBalance());
        partner.getCreditInfo().setCreditRating(dto.getCreditRating());
        partner.getCreditInfo().setCreditScore(dto.getCreditScore());
        partner.getCreditInfo().setPaymentTermDays(dto.getPaymentTermDays());
        partner.getCreditInfo().setPreferredPaymentMethod(dto.getPreferredPaymentMethod());
        partner.getCreditInfo().setBankAccountInfo(dto.getBankAccountInfo());
        partner.getCreditInfo().setLastPaymentDate(dto.getLastPaymentDate());
        partner.getCreditInfo().setPaymentHistory(dto.getPaymentHistory());
        // LoyaltyInfo
        if (partner.getLoyaltyInfo() == null) partner.setLoyaltyInfo(new ma.foodplus.ordering.system.partner.domain.LoyaltyInfo());
        partner.getLoyaltyInfo().setVip(dto.isVip());
        partner.getLoyaltyInfo().setLoyaltyPoints(dto.getLoyaltyPoints());
        partner.getLoyaltyInfo().setLastOrderDate(dto.getLastOrderDate());
        partner.getLoyaltyInfo().setTotalOrders(dto.getTotalOrders());
        partner.getLoyaltyInfo().setTotalSpent(dto.getTotalSpent());
        partner.getLoyaltyInfo().setAverageOrderValue(dto.getAverageOrderValue());
        partner.getLoyaltyInfo().setPartnerSince(dto.getCustomerSince());
        // DeliveryPreference
        if (partner.getDeliveryPreference() == null) partner.setDeliveryPreference(new ma.foodplus.ordering.system.partner.domain.DeliveryPreference());
        partner.getDeliveryPreference().setPreferredDeliveryTime(dto.getPreferredDeliveryTime());
        partner.getDeliveryPreference().setPreferredDeliveryDays(dto.getPreferredDeliveryDays());
        partner.getDeliveryPreference().setSpecialHandlingInstructions(dto.getSpecialHandlingInstructions());
        // AuditInfo
        if (partner.getAuditInfo() == null) partner.setAuditInfo(new ma.foodplus.ordering.system.partner.domain.AuditInfo());
        partner.getAuditInfo().setNotes(dto.getNotes());
        partner.getAuditInfo().setActive(dto.isActive());
        partner.getAuditInfo().setLastActivityDate(dto.getLastActivityDate());
        partner.getAuditInfo().setCreatedBy(dto.getCreatedBy());
        partner.getAuditInfo().setUpdatedBy(dto.getUpdatedBy());
        partner.getAuditInfo().setCreatedAt(dto.getCreatedAt());
        partner.getAuditInfo().setUpdatedAt(dto.getUpdatedAt());
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