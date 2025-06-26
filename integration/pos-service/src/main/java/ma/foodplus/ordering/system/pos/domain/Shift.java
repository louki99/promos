package ma.foodplus.ordering.system.pos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ma.foodplus.ordering.system.pos.enums.ShiftStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "terminal_id", nullable = false)
    private Terminal terminal;

    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    private User cashier;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @NotNull
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ShiftStatus status = ShiftStatus.OPEN;

    @Column(precision = 12, scale = 2)
    private java.math.BigDecimal openingBalance;
    @Column(precision = 12, scale = 2)
    private java.math.BigDecimal closingBalance;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    public Shift() {
        this.startTime = LocalDateTime.now();
    }

    public Shift(Terminal terminal, User cashier, Store store, java.math.BigDecimal openingBalance) {
        this();
        this.terminal = terminal;
        this.cashier = cashier;
        this.store = store;
        this.openingBalance = openingBalance;
        this.openedAt = LocalDateTime.now();
        this.status = ma.foodplus.ordering.system.pos.enums.ShiftStatus.OPEN;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Terminal getTerminal() { return terminal; }
    public void setTerminal(Terminal terminal) { this.terminal = terminal; }
    public User getCashier() { return cashier; }
    public void setCashier(User cashier) { this.cashier = cashier; }
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public ShiftStatus getStatus() { return status; }
    public void setStatus(ShiftStatus status) { this.status = status; }
    public java.math.BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(java.math.BigDecimal openingBalance) { this.openingBalance = openingBalance; }
    public java.math.BigDecimal getClosingBalance() { return closingBalance; }
    public void setClosingBalance(java.math.BigDecimal closingBalance) { this.closingBalance = closingBalance; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }
    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
} 