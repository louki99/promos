package ma.foodplus.ordering.system.pos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CashSessionCloseRequest {
    @NotNull(message = "Cash collected amount is required")
    @DecimalMin(value = "0.0", message = "Cash collected amount cannot be negative")
    private BigDecimal cashCollected;

    private String notes;

    // Getters and Setters
    public BigDecimal getCashCollected() { return cashCollected; }
    public void setCashCollected(BigDecimal cashCollected) { this.cashCollected = cashCollected; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 