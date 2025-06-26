package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Sale;
import ma.foodplus.ordering.system.pos.domain.SaleItem;
import ma.foodplus.ordering.system.pos.enums.PaymentMethod;
import ma.foodplus.ordering.system.pos.enums.SaleStatus;
import ma.foodplus.ordering.system.pos.repository.SaleItemRepository;
import ma.foodplus.ordering.system.pos.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PartnerService partnerService;

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public Optional<Sale> getSaleBySaleNumber(String saleNumber) {
        return saleRepository.findBySaleNumber(saleNumber);
    }

    public List<Sale> getSalesByStore(Long storeId) {
        return saleRepository.findByStoreId(storeId);
    }

    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate);
    }

    public Page<Sale> getSalesByStore(Long storeId, Pageable pageable) {
        return saleRepository.findByStoreIdOrderBySaleDateDesc(storeId, pageable);
    }

    public BigDecimal getTotalSales(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = saleRepository.getTotalSalesByStoreAndDateRange(storeId, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long getTransactionCount(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        Long count = saleRepository.getTransactionCountByStoreAndDateRange(storeId, startDate, endDate);
        return count != null ? count : 0L;
    }

    public List<Sale> getSalesByPartner(Long customerId) {
        return saleRepository.findByPartnerId(customerId);
    }

    public Sale createSale(Sale sale) {
        // Generate sale number
        sale.setSaleNumber(generateSaleNumber());
        sale.setSaleDate(LocalDateTime.now());
        sale.setCreatedAt(LocalDateTime.now());
        sale.setUpdatedAt(LocalDateTime.now());

        // Calculate totals
        sale.calculateTotals();

        // Save sale
        Sale savedSale = saleRepository.save(sale);

        // Update inventory
        for ( SaleItem item : sale.getSaleItems()) {
            item.setSale(savedSale);
            item.calculateTotals();
            if (item.getProduct().isTrackInventory()) {
                inventoryService.reduceStock(item.getProduct().getId(),
                        savedSale.getStore().getId(),
                        item.getQuantity());
            }
        }

        // Update partner loyalty points
        if (sale.getPartner() != null) {
            int pointsEarned = calculateLoyaltyPoints(sale.getTotalAmount());
            sale.setLoyaltyPointsEarned(pointsEarned);
            partnerService.addLoyaltyPoints(sale.getPartner().getId(), pointsEarned);
        }

        return saleRepository.save(savedSale);
    }

    public Sale updateSaleStatus(Long id, SaleStatus status) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        sale.setStatus(status);
        sale.setUpdatedAt(LocalDateTime.now());

        return saleRepository.save(sale);
    }

    public Sale processSalePayment(Long saleId, BigDecimal paidAmount, PaymentMethod paymentMethod) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        sale.setPaidAmount(paidAmount);
        sale.setPaymentMethod(paymentMethod);
        sale.calculateTotals();

        if (sale.isFullyPaid()) {
            sale.setStatus(SaleStatus.COMPLETED);
        }

        sale.setUpdatedAt(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    public void cancelSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        // Restore inventory
        for (SaleItem item : sale.getSaleItems()) {
            if (item.getProduct().isTrackInventory()) {
                inventoryService.addStock(item.getProduct().getId(),
                        sale.getStore().getId(),
                        item.getQuantity());
            }
        }

        // Revert loyalty points
        if (sale.getPartner() != null && sale.getLoyaltyPointsEarned() > 0) {
            partnerService.deductLoyaltyPoints(sale.getPartner().getId(),
                    sale.getLoyaltyPointsEarned());
        }

        sale.setStatus(SaleStatus.CANCELLED);
        sale.setUpdatedAt(LocalDateTime.now());
        saleRepository.save(sale);
    }

    private String generateSaleNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "SALE-" + timestamp;
    }

    private int calculateLoyaltyPoints(BigDecimal totalAmount) {
        // 1 point per dollar spent
        return totalAmount.intValue();
    }

    public List<Object[]> getTopSellingProducts(LocalDateTime startDate, LocalDateTime endDate) {
        return saleItemRepository.getTopSellingProducts(startDate, endDate);
    }

    public Sale updateSale(Long id, Sale saleDetails) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        // Update fields as needed
        sale.setPartner(saleDetails.getPartner());
        sale.setCashier(saleDetails.getCashier());
        sale.setStore(saleDetails.getStore());
        sale.setSubtotal(saleDetails.getSubtotal());
        sale.setTaxAmount(saleDetails.getTaxAmount());
        sale.setDiscountAmount(saleDetails.getDiscountAmount());
        sale.setTotalAmount(saleDetails.getTotalAmount());
        sale.setPaidAmount(saleDetails.getPaidAmount());
        sale.setChangeAmount(saleDetails.getChangeAmount());
        sale.setPaymentMethod(saleDetails.getPaymentMethod());
        sale.setStatus(saleDetails.getStatus());
        sale.setNotes(saleDetails.getNotes());
        sale.setLoyaltyPointsEarned(saleDetails.getLoyaltyPointsEarned());
        sale.setLoyaltyPointsUsed(saleDetails.getLoyaltyPointsUsed());
        sale.setSaleDate(saleDetails.getSaleDate());
        sale.setUpdatedAt(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}
