package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Embeddable
@Data
public class LoyaltyInfo {
    @Column(name = "is_vip")
    private boolean isVip;
    
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;
    
    @Column(name = "total_orders")
    private Integer totalOrders;

    @Column(precision = 24, scale = 6)
    private BigDecimal totalSpent;

    @Column(precision = 24, scale = 6)
    private BigDecimal averageOrderValue;

    @Column(name = "last_order_date")
    private ZonedDateTime lastOrderDate;
    
    @Column(name = "partner_since")
    private ZonedDateTime partnerSince;
}
