package ma.foodplus.ordering.system.promos.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class PromotionCustomerFamilyDTO {
    private Long id;
    private Long promotionId;
    private String customerFamilyCode;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
} 