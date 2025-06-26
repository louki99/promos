package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.Partner;
import ma.foodplus.ordering.system.pos.enums.PartnerType;
import ma.foodplus.ordering.system.pos.enums.LoyaltyTier;
import ma.foodplus.ordering.system.pos.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private PartnerService partnerService;

    @GetMapping
    public List<Partner> getAllCustomers() {
        return partnerService.getAllPartners();
    }

    @GetMapping("/active")
    public List<Partner> getActiveCustomers() {
        return partnerService.getActivePartners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partner> getCustomerById(@PathVariable Long id) {
        return partnerService.getPartnerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Partner> getCustomerByEmail(@PathVariable String email) {
        return partnerService.getPartnerByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<Partner> getCustomerByPhone(@PathVariable String phone) {
        return partnerService.getPartnerByPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public List<Partner> getCustomersByType(@PathVariable PartnerType type) {
        return partnerService.getPartnersByType(type);
    }

    @GetMapping("/loyalty-tier/{tier}")
    public List<Partner> getCustomersByLoyaltyTier(@PathVariable LoyaltyTier tier) {
        return partnerService.getPartnersByLoyaltyTier(tier);
    }

    @GetMapping("/search")
    public Page<Partner> searchCustomers(@RequestParam String search,Pageable pageable) {
        return partnerService.searchPartners(search, pageable);
    }

    @PostMapping
    public Partner createCustomer(@RequestBody Partner partner) {
        return partnerService.createPartner(partner);
    }

    @PutMapping("/{id}")
    public Partner updateCustomer(@PathVariable Long id,@RequestBody Partner partner) {
        return partnerService.updatePartner(id,partner);
    }

    @PutMapping("/{id}/loyalty/add")
    public void addLoyaltyPoints(@PathVariable Long id, @RequestParam Integer points) {
        partnerService.addLoyaltyPoints(id, points);
    }

    @PutMapping("/{id}/loyalty/deduct")
    public void deductLoyaltyPoints(@PathVariable Long id, @RequestParam Integer points) {
        partnerService.deductLoyaltyPoints(id, points);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
} 