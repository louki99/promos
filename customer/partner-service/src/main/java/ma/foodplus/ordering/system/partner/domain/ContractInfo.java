package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.ZonedDateTime;

@Embeddable
@Data
public class ContractInfo {
    @Column(name = "contract_number")
    private String contractNumber;
    
    @Column(name = "contract_start_date")
    private ZonedDateTime contractStartDate;
    
    @Column(name = "contract_end_date")
    private ZonedDateTime contractEndDate;
    
    @Column(name = "contract_type")
    private String contractType;

    @Column(columnDefinition = "TEXT")
    private String contractTerms;

    @Column(columnDefinition = "TEXT")
    private String paymentTerms;

    @Column(columnDefinition = "TEXT")
    private String deliveryTerms;

    @Column(columnDefinition = "TEXT")
    private String specialConditions;
}