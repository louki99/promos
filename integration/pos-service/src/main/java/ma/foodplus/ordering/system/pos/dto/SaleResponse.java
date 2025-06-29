package ma.foodplus.ordering.system.pos.dto;

import ma.foodplus.ordering.system.pos.domain.Sale;
import ma.foodplus.ordering.system.pos.enums.PaymentMethod;
import ma.foodplus.ordering.system.pos.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SaleResponse {
    private Long id;
    private String saleNumber;
    private PartnerInfo partner;
    private CashierInfo cashier;
    private StoreInfo store;
    private TerminalInfo terminal;
    private CashSessionInfo cashSession;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal changeAmount;
    private PaymentMethod paymentMethod;
    private SaleStatus status;
    private String notes;
    private Integer loyaltyPointsEarned;
    private Integer loyaltyPointsUsed;
    private LocalDateTime saleDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SaleItemResponse> saleItems;

    // Constructors
    public SaleResponse() {}

    public SaleResponse(Sale sale) {
        this.id = sale.getId();
        this.saleNumber = sale.getSaleNumber();
        this.partner = sale.getPartner() != null ? new PartnerInfo(sale.getPartner()) : null;
        this.cashier = new CashierInfo(sale.getCashier());
        this.store = new StoreInfo(sale.getStore());
        this.terminal = new TerminalInfo(sale.getTerminal());
        this.cashSession = sale.getCashSession() != null ? new CashSessionInfo(sale.getCashSession()) : null;
        this.subtotal = sale.getSubtotal();
        this.taxAmount = sale.getTaxAmount();
        this.discountAmount = sale.getDiscountAmount();
        this.totalAmount = sale.getTotalAmount();
        this.paidAmount = sale.getPaidAmount();
        this.changeAmount = sale.getChangeAmount();
        this.paymentMethod = sale.getPaymentMethod();
        this.status = sale.getStatus();
        this.notes = sale.getNotes();
        this.loyaltyPointsEarned = sale.getLoyaltyPointsEarned();
        this.loyaltyPointsUsed = sale.getLoyaltyPointsUsed();
        this.saleDate = sale.getSaleDate();
        this.createdAt = sale.getCreatedAt();
        this.updatedAt = sale.getUpdatedAt();
        this.saleItems = sale.getSaleItems() != null ? 
            sale.getSaleItems().stream().map(SaleItemResponse::new).collect(Collectors.toList()) : null;
    }

    // Inner classes for nested objects
    public static class PartnerInfo {
        private Long id;
        private String name;
        private String email;

        public PartnerInfo(ma.foodplus.ordering.system.pos.domain.Partner partner) {
            this.id = partner.getId();
            this.name = partner.getFirstName() + " " + partner.getLastName();
            this.email = partner.getEmail();
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class CashierInfo {
        private Long id;
        private String username;
        private String fullName;

        public CashierInfo(ma.foodplus.ordering.system.pos.domain.User cashier) {
            this.id = cashier.getId();
            this.username = cashier.getUsername();
            this.fullName = cashier.getFirstName() + " " + cashier.getLastName();
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
    }

    public static class StoreInfo {
        private Long id;
        private String name;
        private String code;

        public StoreInfo(ma.foodplus.ordering.system.pos.domain.Store store) {
            this.id = store.getId();
            this.name = store.getName();
            this.code = store.getCode();
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    public static class TerminalInfo {
        private Long id;
        private String name;
        private String code;
        private String location;

        public TerminalInfo(ma.foodplus.ordering.system.pos.domain.Terminal terminal) {
            this.id = terminal.getId();
            this.name = terminal.getName();
            this.code = terminal.getCode();
            this.location = terminal.getLocation();
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }

    public static class CashSessionInfo {
        private UUID sessionId;
        private LocalDateTime openedAt;
        private BigDecimal initialAmount;
        private BigDecimal totalSales;
        private String status;

        public CashSessionInfo(ma.foodplus.ordering.system.pos.domain.CashSession cashSession) {
            this.sessionId = cashSession.getSessionId();
            this.openedAt = cashSession.getOpenedAt();
            this.initialAmount = cashSession.getInitialAmount();
            this.totalSales = cashSession.getTotalSales();
            this.status = cashSession.getStatus().name();
        }

        // Getters and Setters
        public UUID getSessionId() { return sessionId; }
        public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }
        public LocalDateTime getOpenedAt() { return openedAt; }
        public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }
        public BigDecimal getInitialAmount() { return initialAmount; }
        public void setInitialAmount(BigDecimal initialAmount) { this.initialAmount = initialAmount; }
        public BigDecimal getTotalSales() { return totalSales; }
        public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class SaleItemResponse {
        private Long id;
        private ProductInfo product;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private BigDecimal discount;
        private BigDecimal taxAmount;

        public SaleItemResponse(ma.foodplus.ordering.system.pos.domain.SaleItem saleItem) {
            this.id = saleItem.getId();
            this.product = new ProductInfo(saleItem.getProduct());
            this.quantity = saleItem.getQuantity();
            this.unitPrice = saleItem.getUnitPrice();
            this.totalPrice = saleItem.getTotalPrice();
            this.discount = saleItem.getDiscount();
            this.taxAmount = saleItem.getTaxAmount();
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public ProductInfo getProduct() { return product; }
        public void setProduct(ProductInfo product) { this.product = product; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
        public BigDecimal getDiscount() { return discount; }
        public void setDiscount(BigDecimal discount) { this.discount = discount; }
        public BigDecimal getTaxAmount() { return taxAmount; }
        public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

        public static class ProductInfo {
            private Long id;
            private String name;
            private String sku;
            private String barcode;

            public ProductInfo(ma.foodplus.ordering.system.pos.domain.Product product) {
                this.id = product.getId();
                this.name = product.getName();
                this.sku = product.getSku();
                this.barcode = product.getBarcode();
            }

            // Getters and Setters
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            public String getSku() { return sku; }
            public void setSku(String sku) { this.sku = sku; }
            public String getBarcode() { return barcode; }
            public void setBarcode(String barcode) { this.barcode = barcode; }
        }
    }

    // Getters and Setters for main class
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSaleNumber() { return saleNumber; }
    public void setSaleNumber(String saleNumber) { this.saleNumber = saleNumber; }

    public PartnerInfo getPartner() { return partner; }
    public void setPartner(PartnerInfo partner) { this.partner = partner; }

    public CashierInfo getCashier() { return cashier; }
    public void setCashier(CashierInfo cashier) { this.cashier = cashier; }

    public StoreInfo getStore() { return store; }
    public void setStore(StoreInfo store) { this.store = store; }

    public TerminalInfo getTerminal() { return terminal; }
    public void setTerminal(TerminalInfo terminal) { this.terminal = terminal; }

    public CashSessionInfo getCashSession() { return cashSession; }
    public void setCashSession(CashSessionInfo cashSession) { this.cashSession = cashSession; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public BigDecimal getChangeAmount() { return changeAmount; }
    public void setChangeAmount(BigDecimal changeAmount) { this.changeAmount = changeAmount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getLoyaltyPointsEarned() { return loyaltyPointsEarned; }
    public void setLoyaltyPointsEarned(Integer loyaltyPointsEarned) { this.loyaltyPointsEarned = loyaltyPointsEarned; }

    public Integer getLoyaltyPointsUsed() { return loyaltyPointsUsed; }
    public void setLoyaltyPointsUsed(Integer loyaltyPointsUsed) { this.loyaltyPointsUsed = loyaltyPointsUsed; }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<SaleItemResponse> getSaleItems() { return saleItems; }
    public void setSaleItems(List<SaleItemResponse> saleItems) { this.saleItems = saleItems; }
} 