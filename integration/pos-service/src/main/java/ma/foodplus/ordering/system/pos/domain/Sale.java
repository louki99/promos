package ma.foodplus.ordering.system.pos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ma.foodplus.ordering.system.pos.enums.PaymentMethod;
import ma.foodplus.ordering.system.pos.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String saleNumber;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    @NotNull(message = "Cashier is required")
    private User cashier;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull(message = "Store is required")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "terminal_id", nullable = false)
    @NotNull(message = "Terminal is required")
    private Terminal terminal;

    @ManyToOne
    @JoinColumn(name = "cash_session_id")
    private CashSession cashSession;

    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal;

    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal paidAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal changeAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private SaleStatus status = SaleStatus.COMPLETED;

    @Size(max = 500)
    private String notes;

    private Integer loyaltyPointsEarned = 0;
    private Integer loyaltyPointsUsed = 0;

    private LocalDateTime saleDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaleItem> saleItems;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<Payment> payments;

    // Constructors
    public Sale() {
        this.saleDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Sale(String saleNumber, User cashier, Store store, Terminal terminal) {
        this();
        this.saleNumber = saleNumber;
        this.cashier = cashier;
        this.store = store;
        this.terminal = terminal;
    }

    // Business methods
    public void calculateTotals() {
        if (saleItems != null && !saleItems.isEmpty()) {
            this.subtotal = saleItems.stream()
                    .map(SaleItem::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            this.taxAmount = saleItems.stream()
                    .map(SaleItem::getTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            this.totalAmount = subtotal.add(taxAmount).subtract(discountAmount);

            if (paidAmount != null) {
                this.changeAmount = paidAmount.subtract(totalAmount);
                if (changeAmount.compareTo(BigDecimal.ZERO) < 0) {
                    this.changeAmount = BigDecimal.ZERO;
                }
            }
        } else {
            this.subtotal = BigDecimal.ZERO;
            this.taxAmount = BigDecimal.ZERO;
            this.totalAmount = BigDecimal.ZERO;
            this.changeAmount = BigDecimal.ZERO;
        }
    }

    public boolean isFullyPaid() {
        return paidAmount != null && paidAmount.compareTo(totalAmount) >= 0;
    }

    public boolean hasValidSession() {
        return cashSession != null && cashSession.isOpen();
    }

    public boolean isLinkedToSession() {
        return cashSession != null;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSaleNumber() { return saleNumber; }
    public void setSaleNumber(String saleNumber) { this.saleNumber = saleNumber; }

    public Partner getPartner() { return partner; }
    public void setPartner(Partner partner) { this.partner=partner; }

    public User getCashier() { return cashier; }
    public void setCashier(User cashier) { this.cashier = cashier; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public Terminal getTerminal() { return terminal; }
    public void setTerminal(Terminal terminal) { this.terminal = terminal; }

    public CashSession getCashSession() { return cashSession; }
    public void setCashSession(CashSession cashSession) { this.cashSession = cashSession; }

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

    public List<SaleItem> getSaleItems() { return saleItems; }
    public void setSaleItems(List<SaleItem> saleItems) { this.saleItems = saleItems; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
}
