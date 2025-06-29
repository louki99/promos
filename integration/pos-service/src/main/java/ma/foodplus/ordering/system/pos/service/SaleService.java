package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.*;
import ma.foodplus.ordering.system.pos.dto.SaleCreateRequest;
import ma.foodplus.ordering.system.pos.dto.SaleResponse;
import ma.foodplus.ordering.system.pos.enums.PaymentMethod;
import ma.foodplus.ordering.system.pos.enums.SaleStatus;
import ma.foodplus.ordering.system.pos.repository.*;
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

    @Autowired
    private CashSessionService cashSessionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public Optional<SaleResponse> getSaleResponseById(Long id) {
        return saleRepository.findById(id).map(SaleResponse::new);
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

    public SaleResponse createSaleFromRequest(SaleCreateRequest request, Long cashierId) {
        // Validate entities exist
        User cashier = userRepository.findById(cashierId)
                .orElseThrow(() -> new RuntimeException("Cashier not found"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Terminal terminal = terminalRepository.findById(request.getTerminalId())
                .orElseThrow(() -> new RuntimeException("Terminal not found"));

        Partner partner = partnerRepository.findById(request.getPartnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        // Validate terminal belongs to store
        if (!terminal.getStore().getId().equals(store.getId())) {
            throw new RuntimeException("Terminal does not belong to the specified store");
        }

        // Validate cashier has open session for this specific terminal
        Optional<CashSession> currentSession = cashSessionService.getCurrentSession(
                cashierId, store.getId(), terminal.getId());
        
        if (currentSession.isEmpty()) {
            throw new RuntimeException("Cashier must have an open cash session for this terminal to create sales");
        }

        CashSession session = currentSession.get();

        // Create sale entity
        Sale sale = new Sale();
        sale.setCashier(cashier);
        sale.setStore(store);
        sale.setTerminal(terminal);
        sale.setPartner(partner);
        sale.setCashSession(session);
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setPaidAmount(request.getPaidAmount());
        sale.setDiscountAmount(request.getDiscountAmount());
        sale.setNotes(request.getNotes());

        // Generate sale number
        sale.setSaleNumber(generateSaleNumber());
        sale.setSaleDate(LocalDateTime.now());
        sale.setCreatedAt(LocalDateTime.now());
        sale.setUpdatedAt(LocalDateTime.now());

        // Create sale items
        List<SaleItem> saleItems = request.getSaleItems().stream()
                .map(itemRequest -> createSaleItem(itemRequest, sale))
                .toList();
        sale.setSaleItems(saleItems);

        // Calculate totals
        sale.calculateTotals();

        // Validate payment
        if (request.getPaidAmount() != null && request.getPaidAmount().compareTo(sale.getTotalAmount()) < 0) {
            throw new RuntimeException("Paid amount cannot be less than total amount");
        }

        // Save sale
        Sale savedSale = saleRepository.save(sale);

        // Update inventory
        for (SaleItem item : saleItems) {
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

        // Update cash session with sale amount
        cashSessionService.addSaleToSession(cashierId, sale.getTotalAmount());

        return new SaleResponse(savedSale);
    }

    private SaleItem createSaleItem(SaleCreateRequest.SaleItemRequest itemRequest, Sale sale) {
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

        SaleItem saleItem = new SaleItem();
        saleItem.setSale(sale);
        saleItem.setProduct(product);
        saleItem.setQuantity(itemRequest.getQuantity());
        saleItem.setUnitPrice(itemRequest.getUnitPrice() != null ? itemRequest.getUnitPrice() : product.getSellingPrice());
        saleItem.setDiscount(itemRequest.getDiscount());
        saleItem.calculateTotals();

        return saleItem;
    }

    public Sale createSale(Sale sale) {
        // Enhanced validation for terminal-specific session
        if (!cashSessionService.hasOpenSession(sale.getCashier().getId(), sale.getStore().getId(), sale.getTerminal().getId())) {
            throw new RuntimeException("Cashier must have an open cash session for this terminal to create sales");
        }

        // Get current cash session for the specific terminal
        Optional<CashSession> currentSession = cashSessionService.getCurrentSession(
                sale.getCashier().getId(), sale.getStore().getId(), sale.getTerminal().getId());
        
        if (currentSession.isPresent()) {
            sale.setCashSession(currentSession.get());
        } else {
            throw new RuntimeException("No active cash session found for this terminal");
        }

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
        for (SaleItem item : sale.getSaleItems()) {
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

        // Update cash session with sale amount
        if (sale.getCashSession() != null) {
            cashSessionService.addSaleToSession(sale.getCashier().getId(), sale.getTotalAmount());
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

    public List<Object[]> getDailyRevenue(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getDailyRevenue(storeId, startDate, endDate);
    }

    public Sale updateSale(Long id, Sale saleDetails) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        
        // Update fields as needed
        sale.setPartner(saleDetails.getPartner());
        sale.setCashier(saleDetails.getCashier());
        sale.setStore(saleDetails.getStore());
        sale.setTerminal(saleDetails.getTerminal());
        sale.setCashSession(saleDetails.getCashSession());
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

    // Business validation methods
    public boolean canCreateSale(Long cashierId, Long storeId, Long terminalId) {
        return cashSessionService.hasOpenSession(cashierId, storeId, terminalId);
    }

    public Optional<CashSession> getCurrentSessionForSale(Long cashierId, Long storeId, Long terminalId) {
        return cashSessionService.getCurrentSession(cashierId, storeId, terminalId);
    }
}
