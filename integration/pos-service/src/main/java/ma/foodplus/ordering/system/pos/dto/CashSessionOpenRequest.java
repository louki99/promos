package ma.foodplus.ordering.system.pos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CashSessionOpenRequest {
    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Terminal ID is required")
    private Long terminalId;

    @NotNull(message = "Initial amount is required")
    @DecimalMin(value = "0.01", message = "Initial amount must be greater than 0")
    private BigDecimal initialAmount;

    private String notes;

    // Getters and Setters
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public Long getTerminalId() { return terminalId; }
    public void setTerminalId(Long terminalId) { this.terminalId = terminalId; }

    public BigDecimal getInitialAmount() { return initialAmount; }
    public void setInitialAmount(BigDecimal initialAmount) { this.initialAmount = initialAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 