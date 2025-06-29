package ma.foodplus.ordering.system.pos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "terminal_assignments")
public class TerminalAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "terminal_id", nullable = false)
    @NotNull
    private Terminal terminal;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull
    private Store store;

    private boolean active = true;
    private LocalDateTime assignedAt;
    private LocalDateTime expiresAt;
    private String assignedBy; // Username of who assigned this
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public TerminalAssignment() {
        this.assignedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public TerminalAssignment(User user, Terminal terminal, Store store) {
        this();
        this.user = user;
        this.terminal = terminal;
        this.store = store;
    }

    // Business methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isValid() {
        return active && !isExpired();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Terminal getTerminal() { return terminal; }
    public void setTerminal(Terminal terminal) { this.terminal = terminal; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 