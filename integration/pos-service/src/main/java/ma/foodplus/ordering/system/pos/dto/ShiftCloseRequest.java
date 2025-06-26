package ma.foodplus.ordering.system.pos.dto;

import java.math.BigDecimal;

public class ShiftCloseRequest {
    private BigDecimal closingBalance;

    public BigDecimal getClosingBalance() { return closingBalance; }
    public void setClosingBalance(BigDecimal closingBalance) { this.closingBalance = closingBalance; }
} 