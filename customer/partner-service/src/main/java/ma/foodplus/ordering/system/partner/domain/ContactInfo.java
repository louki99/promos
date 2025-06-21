package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ContactInfo {
    private String telephone;
    private String telecopie;
    private String email;
    private String address;
    private String city;
    private String country;
    private String region;
    
    @Column(name = "postal_code")
    private String postalCode;
}
