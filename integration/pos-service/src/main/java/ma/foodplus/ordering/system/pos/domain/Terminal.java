package ma.foodplus.ordering.system.pos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ma.foodplus.ordering.system.pos.enums.TerminalType;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "terminals")
public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TerminalType type = TerminalType.CASH_REGISTER;

    private boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "terminal")
    private List<Shift> shifts;

    @Size(max = 255)
    private String location;

    public Terminal() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Terminal(String name, String code, Store store, String location) {
        this();
        this.name = name;
        this.code = code;
        this.store = store;
        this.location = location;
    }

    public Terminal(String name, String code, Store store, TerminalType type, String location) {
        this();
        this.name = name;
        this.code = code;
        this.store = store;
        this.type = type;
        this.location = location;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    public TerminalType getType() { return type; }
    public void setType(TerminalType type) { this.type = type; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Shift> getShifts() { return shifts; }
    public void setShifts(List<Shift> shifts) { this.shifts = shifts; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
} 