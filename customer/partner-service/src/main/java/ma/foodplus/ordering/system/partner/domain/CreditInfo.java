package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Embeddable
@Data
public class CreditInfo {
    @Column(precision = 24, scale = 6)
    private BigDecimal creditLimit;

    @Column(name = "credit_rating")
    private String creditRating;
    
    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(columnDefinition = "TEXT")
    private String paymentHistory;

    @Column(precision = 24, scale = 6)
    private BigDecimal outstandingBalance;

    @Column(name = "last_payment_date")
    private ZonedDateTime lastPaymentDate;
    
    @Column(name = "payment_term_days")
    private Integer paymentTermDays;
    
    @Column(name = "preferred_payment_method")
    private String preferredPaymentMethod;

    @Column(columnDefinition = "TEXT")
    private String bankAccountInfo;
}

