package ma.foodplus.ordering.system.promos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ApplyPromotionRequest {
    @NotNull
    private Long customerId;

    @NotEmpty
    private List<@Valid OrdertemDto> orderItems; // Note the @Valid on the list items too!

    @Pattern(regexp = "^[A-Za-z0-9-]{2,10}$", message = "Invalid promotion code format")
    private String promoCode;

    public Long getCustomerId(){
        return customerId;
    }

    public List<OrdertemDto> getOrderItems(){
        return orderItems;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setCustomerId(Long customerId){
        this.customerId=customerId;
    }

    public void setOrderItems(List<OrdertemDto> orderItems){
        this.orderItems=orderItems;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
