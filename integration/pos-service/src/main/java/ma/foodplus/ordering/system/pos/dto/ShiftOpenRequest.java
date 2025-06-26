package ma.foodplus.ordering.system.pos.dto;

import java.math.BigDecimal;

public class ShiftOpenRequest {
    private Long terminalId;
    private Long cashierId;
    private Long storeId;
    private BigDecimal openingBalance;

    // Getters and Setters
    public Long getTerminalId() { return terminalId; }
    public void setTerminalId(Long terminalId) { this.terminalId = terminalId; }
    public Long getCashierId() { return cashierId; }
    public void setCashierId(Long cashierId) { this.cashierId = cashierId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }
} 