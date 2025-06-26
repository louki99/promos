package ma.foodplus.ordering.system.pos.dto;

import ma.foodplus.ordering.system.pos.enums.ShiftStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public class ShiftResponse {
    @Schema(description = "Shift ID", example = "1")
    private Long id;
    @Schema(description = "Terminal ID", example = "10")
    private Long terminalId;
    @Schema(description = "Cashier ID", example = "5")
    private Long cashierId;
    @Schema(description = "Store ID", example = "2")
    private Long storeId;
    @Schema(description = "Opening balance for the shift", example = "100.00")
    private BigDecimal openingBalance;
    @Schema(description = "Closing balance for the shift", example = "200.00")
    private BigDecimal closingBalance;
    @Schema(description = "Date and time when the shift was opened", example = "2024-06-01T08:00:00")
    private LocalDateTime openedAt;
    @Schema(description = "Date and time when the shift was closed", example = "2024-06-01T16:00:00")
    private LocalDateTime closedAt;
    @Schema(description = "Status of the shift", example = "OPEN")
    private ShiftStatus status;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTerminalId() { return terminalId; }
    public void setTerminalId(Long terminalId) { this.terminalId = terminalId; }
    public Long getCashierId() { return cashierId; }
    public void setCashierId(Long cashierId) { this.cashierId = cashierId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }
    public BigDecimal getClosingBalance() { return closingBalance; }
    public void setClosingBalance(BigDecimal closingBalance) { this.closingBalance = closingBalance; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }
    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
    public ShiftStatus getStatus() { return status; }
    public void setStatus(ShiftStatus status) { this.status = status; }
} 