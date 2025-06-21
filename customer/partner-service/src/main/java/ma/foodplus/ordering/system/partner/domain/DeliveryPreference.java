package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class DeliveryPreference {
    @Column(name = "preferred_delivery_time")
    private String preferredDeliveryTime;
    
    @Column(name = "preferred_delivery_days")
    private String preferredDeliveryDays;

    @Column(columnDefinition = "TEXT")
    private String specialHandlingInstructions;
}