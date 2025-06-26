package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.Sale;
import ma.foodplus.ordering.system.pos.enums.PaymentMethod;
import ma.foodplus.ordering.system.pos.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public List<Sale> getSalesByCustomer(@PathVariable Long customerId) {
        return saleService.getSalesByPartner(customerId);
    }

    @GetMapping("/store/{storeId}")
    public List<Sale> getSalesByStore(@PathVariable Long storeId) {
        return saleService.getSalesByStore(storeId);
    }

    @GetMapping("/date-range")
    public List<Sale> getSalesByDateRange(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return saleService.getSalesByDateRange(start, end);
    }

    @PostMapping
    public Sale createSale(@RequestBody Sale sale) {
        return saleService.createSale(sale);
    }

    @PutMapping("/{id}")
    public Sale updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        return saleService.updateSale(id, sale);
    }

    @PutMapping("/{id}/pay")
    public Sale processSalePayment(@PathVariable Long id, @RequestParam BigDecimal paidAmount, @RequestParam PaymentMethod paymentMethod) {
        return saleService.processSalePayment(id, paidAmount, paymentMethod);
    }

    @PutMapping("/{id}/cancel")
    public void cancelSale(@PathVariable Long id) {
        saleService.cancelSale(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
} 