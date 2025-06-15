package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.dto.PromoFamilyDTO;
import ma.foodplus.ordering.system.promos.model.PromoFamily.PromoFamilyType;

import java.util.List;

public interface PromoFamilyService {
    
    PromoFamilyDTO createPromoFamily(PromoFamilyDTO promoFamilyDTO);
    
    PromoFamilyDTO updatePromoFamily(Long id, PromoFamilyDTO promoFamilyDTO);
    
    void deletePromoFamily(Long id);
    
    PromoFamilyDTO getPromoFamilyById(Long id);
    
    PromoFamilyDTO getPromoFamilyByCode(String code);
    
    List<PromoFamilyDTO> getPromoFamiliesByType(PromoFamilyType type);
    
    List<PromoFamilyDTO> getActivePromoFamiliesByType(PromoFamilyType type);
    
    List<PromoFamilyDTO> getPromoFamiliesByMemberCode(String memberCode);
    
    void addMemberToFamily(Long familyId, String memberCode);
    
    void removeMemberFromFamily(Long familyId, String memberCode);
    
    void addMembersToFamily(Long familyId, List<String> memberCodes);
    
    void removeMembersFromFamily(Long familyId, List<String> memberCodes);
    
    boolean isMemberInFamily(String familyCode, String memberCode);
    
    List<String> getFamilyMembers(String familyCode);
} 