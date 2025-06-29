package ma.foodplus.ordering.system.pos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ma.foodplus.ordering.system.pos.enums.CashSessionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cash_sessions")
public class CashSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sessionId;

    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    @NotNull
    private User cashier;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull
    private Store store;

    @ManyToOne
    @JoinColumn(name = "terminal_id", nullable = false)
    @NotNull
    private Terminal terminal;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime openedAt;

    @Column
    private LocalDateTime closedAt;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal initialAmount;

    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal totalSales = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.0")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal cashCollected = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CashSessionStatus status = CashSessionStatus.OPEN;

    @Column(precision = 12, scale = 2)
    private BigDecimal expectedCash;

    @Column(precision = 12, scale = 2)
    private BigDecimal cashDifference;

    @Size(max = 500)
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CashSession() {
        this.sessionId = UUID.randomUUID();
        this.openedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public CashSession(User cashier, Store store, Terminal terminal, BigDecimal initialAmount) {
        this();
        this.cashier = cashier;
        this.store = store;
        this.terminal = terminal;
        this.initialAmount = initialAmount;
    }

    // Business methods
    public void closeSession(BigDecimal cashCollected) {
        this.cashCollected = cashCollected;
        this.closedAt = LocalDateTime.now();
        this.status = CashSessionStatus.CLOSED;
        this.expectedCash = this.initialAmount.add(this.totalSales);
        this.cashDifference = this.cashCollected.subtract(this.expectedCash);
        this.updatedAt = LocalDateTime.now();
    }

    public void addSale(BigDecimal saleAmount) {
        this.totalSales = this.totalSales.add(saleAmount);
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOpen() {
        return this.status == CashSessionStatus.OPEN;
    }

    public BigDecimal getExpectedCash() {
        return this.initialAmount.add(this.totalSales);
    }

    // Getters and Setters
    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public User getCashier() { return cashier; }
    public void setCashier(User cashier) { this.cashier = cashier; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public Terminal getTerminal() { return terminal; }
    public void setTerminal(Terminal terminal) { this.terminal = terminal; }

    public LocalDateTime getOpenedAt() { return openedAt; }
    public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }

    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }

    public BigDecimal getInitialAmount() { return initialAmount; }
    public void setInitialAmount(BigDecimal initialAmount) { this.initialAmount = initialAmount; }

    public BigDecimal getTotalSales() { return totalSales; }
    public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }

    public BigDecimal getCashCollected() { return cashCollected; }
    public void setCashCollected(BigDecimal cashCollected) { this.cashCollected = cashCollected; }

    public CashSessionStatus getStatus() { return status; }
    public void setStatus(CashSessionStatus status) { this.status = status; }

    public BigDecimal getExpectedCashAmount() { return expectedCash; }
    public void setExpectedCash(BigDecimal expectedCash) { this.expectedCash = expectedCash; }

    public BigDecimal getCashDifference() { return cashDifference; }
    public void setCashDifference(BigDecimal cashDifference) { this.cashDifference = cashDifference; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 