package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Partner;
import ma.foodplus.ordering.system.pos.enums.PartnerType;
import ma.foodplus.ordering.system.pos.enums.LoyaltyTier;
import ma.foodplus.ordering.system.pos.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartnerService{

    @Autowired
    private PartnerRepository partnerRepository;

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    public List<Partner> getActivePartners() {
        return partnerRepository.findByActiveTrue();
    }

    public Optional<Partner> getPartnerById(Long id) {
        return partnerRepository.findById(id);
    }

    public Optional<Partner> getPartnerByEmail(String email) {
        return partnerRepository.findByEmail(email);
    }

    public Optional<Partner> getPartnerByPhone(String phone) {
        return partnerRepository.findByPhone(phone);
    }

    public List<Partner> getPartnersByType(PartnerType type) {
        return partnerRepository.findByPartnerType(type);
    }

    public List<Partner> getPartnersByLoyaltyTier(LoyaltyTier tier) {
        return partnerRepository.findByLoyaltyTier(tier);
    }

    public Page<Partner> searchPartners(String search,Pageable pageable) {
        return partnerRepository.searchActiveCustomers(search, pageable);
    }

    public Partner createPartner(Partner partner) {
        partner.setCreatedAt(LocalDateTime.now());
        partner.setUpdatedAt(LocalDateTime.now());
        return partnerRepository.save(partner);
    }

    public Partner updatePartner(Long id,Partner partnerDetails) {
        Partner partner= partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        partner.setFirstName(partnerDetails.getFirstName());
        partner.setLastName(partnerDetails.getLastName());
        partner.setEmail(partnerDetails.getEmail());
        partner.setPhone(partnerDetails.getPhone());
        partner.setAddress(partnerDetails.getAddress());
        partner.setCity(partnerDetails.getCity());
        partner.setPartnerType(partnerDetails.getPartnerType());
        partner.setActive(partnerDetails.isActive());
        partner.setUpdatedAt(LocalDateTime.now());

        return partnerRepository.save(partner);
    }

    public void addLoyaltyPoints(Long customerId, Integer points) {
        Partner partner= partnerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        partner.addLoyaltyPoints(points);
        partner.setUpdatedAt(LocalDateTime.now());
        partnerRepository.save(partner);
    }

    public void deductLoyaltyPoints(Long customerId, Integer points) {
        Partner partner= partnerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        int newPoints = Math.max(0, partner.getLoyaltyPoints() - points);
        partner.setLoyaltyPoints(newPoints);
        partner.setUpdatedAt(LocalDateTime.now());
        partnerRepository.save(partner);
    }

    public void deactivateCustomer(Long id) {
        Partner partner= partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found"));
        partner.setActive(false);
        partner.setUpdatedAt(LocalDateTime.now());
        partnerRepository.save(partner);
    }

    public void deletePartner(Long id) {
        partnerRepository.deleteById(id);
    }
}
