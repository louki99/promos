package ma.foodplus.ordering.system.customer.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProductCustomerStatisticsDTO {
    private Long customerId;
    private int totalProducts;
    private int activeProducts;
    private int productsWithDiscount;
    private int productsWithPendingChanges;
    private BigDecimal averagePrice;
    private BigDecimal averageDiscount;
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;
    private Map<String, Integer> productsByCategory;
    private Map<String, BigDecimal> averagePriceByCategory;
    private Map<String, BigDecimal> averageDiscountByCategory;
}