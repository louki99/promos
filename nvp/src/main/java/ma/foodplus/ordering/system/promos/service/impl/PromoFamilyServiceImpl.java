package ma.foodplus.ordering.system.promos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.promos.dto.PromoFamilyDTO;
import ma.foodplus.ordering.system.promos.exception.PromoFamilyNotFoundException;
import ma.foodplus.ordering.system.promos.exception.PromoFamilyValidationException;
import ma.foodplus.ordering.system.promos.mapper.PromoFamilyMapper;
import ma.foodplus.ordering.system.promos.model.PromoFamily;
import ma.foodplus.ordering.system.promos.model.PromoFamily.PromoFamilyType;
import ma.foodplus.ordering.system.promos.repository.PromoFamilyRepository;
import ma.foodplus.ordering.system.promos.service.PromoFamilyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromoFamilyServiceImpl implements PromoFamilyService {

    private final PromoFamilyRepository promoFamilyRepository;
    private final PromoFamilyMapper promoFamilyMapper;

    @Override
    @Transactional
    public PromoFamilyDTO createPromoFamily(PromoFamilyDTO promoFamilyDTO) {
        log.info("Creating new promo family with code: {}", promoFamilyDTO.getCode());
        validatePromoFamilyDTO(promoFamilyDTO);
        
        if (promoFamilyRepository.existsByCode(promoFamilyDTO.getCode())) {
            throw new PromoFamilyValidationException("Promo family with code " + promoFamilyDTO.getCode() + " already exists");
        }

        PromoFamily promoFamily = promoFamilyMapper.toEntity(promoFamilyDTO);
        promoFamily.setActive(true);
        PromoFamily savedFamily = promoFamilyRepository.save(promoFamily);
        return promoFamilyMapper.toDTO(savedFamily);
    }

    @Override
    @Transactional
    public PromoFamilyDTO updatePromoFamily(Long id, PromoFamilyDTO promoFamilyDTO) {
        log.info("Updating promo family with id: {}", id);
        validatePromoFamilyDTO(promoFamilyDTO);
        
        PromoFamily existingFamily = promoFamilyRepository.findById(id)
                .orElseThrow(() -> new PromoFamilyNotFoundException("Promo family not found with id: " + id));

        if (!existingFamily.getCode().equals(promoFamilyDTO.getCode()) && 
            promoFamilyRepository.existsByCode(promoFamilyDTO.getCode())) {
            throw new PromoFamilyValidationException("Promo family with code " + promoFamilyDTO.getCode() + " already exists");
        }
        
        promoFamilyMapper.updateEntity(existingFamily, promoFamilyDTO);
        PromoFamily updatedFamily = promoFamilyRepository.save(existingFamily);
        return promoFamilyMapper.toDTO(updatedFamily);
    }

    @Override
    @Transactional
    public void deletePromoFamily(Long id) {
        log.info("Deleting promo family with id: {}", id);
        if (!promoFamilyRepository.existsById(id)) {
            throw new PromoFamilyNotFoundException("Promo family not found with id: " + id);
        }
        promoFamilyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PromoFamilyDTO getPromoFamilyById(Long id) {
        log.info("Getting promo family with id: {}", id);
        return promoFamilyRepository.findById(id)
                .map(promoFamilyMapper::toDTO)
                .orElseThrow(() -> new PromoFamilyNotFoundException("Promo family not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PromoFamilyDTO getPromoFamilyByCode(String code) {
        log.info("Getting promo family with code: {}", code);
        return promoFamilyRepository.findByCode(code)
                .map(promoFamilyMapper::toDTO)
                .orElseThrow(() -> new PromoFamilyNotFoundException("Promo family not found with code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoFamilyDTO> getPromoFamiliesByType(PromoFamilyType type) {
        log.info("Getting promo families of type: {}", type);
        return promoFamilyRepository.findByType(type).stream()
                .map(promoFamilyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoFamilyDTO> getActivePromoFamiliesByType(PromoFamilyType type) {
        log.info("Getting active promo families of type: {}", type);
        return promoFamilyRepository.findByTypeAndIsActiveTrue(type).stream()
                .map(promoFamilyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoFamilyDTO> getPromoFamiliesByMemberCode(String memberCode) {
        log.info("Getting promo families containing member code: {}", memberCode);
        return promoFamilyRepository.findActiveByMemberCode(memberCode).stream()
                .map(promoFamilyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addMemberToFamily(Long familyId, String memberCode) {
        log.info("Adding member {} to family {}", memberCode, familyId);
        validateMemberCode(memberCode);
        
        PromoFamily family = promoFamilyRepository.findById(familyId)
                .orElseThrow(() -> new PromoFamilyNotFoundException("Promo family not found with id: " + familyId));
        
        if (!family.getMemberCodes().contains(memberCode)) {
            family.getMemberCodes().add(memberCode);
            promoFamilyRepository.save(family);
        }
    }

    @Override
    @Transactional
    public void removeMemberFromFamily(Long familyId, String memberCode) {
        log.info("Removing member {} from family {}", memberCode, familyId);
        validateMemberCode(memberCode);
        
        PromoFamily family = promoFamilyRepository.findById(familyId)
                .orElseThrow(() -> new PromoFamilyNotFoundException("Promo family not found with id: " + familyId));
        
        family.getMemberCodes().remove(memberCode);
        promoFamilyRepository.save(family);
    }

    @Override
    @Transactional
    public void addMembersToFamily(Long familyId, List<String> memberCodes) {
        log.info("Adding multiple members to family {}", familyId);
        validateMemberCodes(memberCodes);
        
        PromoFamily family = promoFamilyRepository.findById(familyId)
                .orElseThrow(() -> new PromoFamilyNotFoundException("Promo family not found with id: " + familyId));
        
        memberCodes.forEach(code -> {
            if (!family.getMemberCodes().contains(code)) {
                family.getMemberCodes().add(code);
            }
        });
        promoFamilyRepository.save(family);
    }

    @Override
    @Transactional
    public void removeMembersFromFamily(Long familyId, List<String> memberCodes) {
        log.info("Removing multiple members from family {}", familyId);
        validateMemberCodes(memberCodes);
        
        PromoFamily family = promoFamilyRepository.findById(familyId)
                .orElseThrow(() -> new PromoFamilyNotFoundException("Promo family not found with id: " + familyId));
        
        family.getMemberCodes().removeAll(memberCodes);
        promoFamilyRepository.save(family);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMemberInFamily(String familyCode, String memberCode) {
        log.info("Checking if member {} is in family {}", memberCode, familyCode);
        validateMemberCode(memberCode);
        
        return promoFamilyRepository.findByCode(familyCode)
                .map(family -> family.getMemberCodes().contains(memberCode))
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getFamilyMembers(String familyCode) {
        log.info("Getting members of family {}", familyCode);
        return promoFamilyRepository.findByCode(familyCode)
                .map(PromoFamily::getMemberCodes)
                .orElseThrow(() -> new PromoFamilyNotFoundException("Promo family not found with code: " + familyCode));
    }

    private void validatePromoFamilyDTO(PromoFamilyDTO dto) {
        if (dto == null) {
            throw new PromoFamilyValidationException("Promo family DTO cannot be null");
        }
        if (!StringUtils.hasText(dto.getCode())) {
            throw new PromoFamilyValidationException("Promo family code cannot be empty");
        }
        if (!StringUtils.hasText(dto.getName())) {
            throw new PromoFamilyValidationException("Promo family name cannot be empty");
        }
        if (dto.getType() == null) {
            throw new PromoFamilyValidationException("Promo family type cannot be null");
        }
    }

    private void validateMemberCode(String memberCode) {
        if (!StringUtils.hasText(memberCode)) {
            throw new PromoFamilyValidationException("Member code cannot be empty");
        }
    }

    private void validateMemberCodes(List<String> memberCodes) {
        if (memberCodes == null || memberCodes.isEmpty()) {
            throw new PromoFamilyValidationException("Member codes list cannot be empty");
        }
        memberCodes.forEach(this::validateMemberCode);
    }
} 