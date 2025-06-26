package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Tax;
import ma.foodplus.ordering.system.pos.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaxService {
    @Autowired
    private TaxRepository taxRepository;

    public List<Tax> getAllTaxes() {
        return taxRepository.findAll();
    }

    public Optional<Tax> getTaxById(Long id) {
        return taxRepository.findById(id);
    }

    public Tax createTax(Tax tax) {
        tax.setCreatedAt(LocalDateTime.now());
        tax.setUpdatedAt(LocalDateTime.now());
        return taxRepository.save(tax);
    }

    public Tax updateTax(Long id, Tax taxDetails) {
        Tax tax = taxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax not found"));
        tax.setName(taxDetails.getName());
        tax.setRate(taxDetails.getRate());
        tax.setDescription(taxDetails.getDescription());
        tax.setActive(taxDetails.isActive());
        tax.setUpdatedAt(LocalDateTime.now());
        return taxRepository.save(tax);
    }

    public void deleteTax(Long id) {
        taxRepository.deleteById(id);
    }
} 