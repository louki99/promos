package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;

@Embeddable
@Data
public class CompanyInfo {
    @Column(name = "company_name")
    private String companyName;
    
    @Column(name = "legal_form")
    private String legalForm;
    
    @Column(name = "registration_number")
    private String registrationNumber;
    
    @Column(name = "tax_id")
    private String taxId;
    
    @Column(name = "vat_number")
    private String vatNumber;
    
    @Column(name = "business_activity")
    private String businessActivity;
    
    @Column(name = "annual_turnover")
    private BigDecimal annualTurnover;
    
    @Column(name = "number_of_employees")
    private Integer numberOfEmployees;
}
