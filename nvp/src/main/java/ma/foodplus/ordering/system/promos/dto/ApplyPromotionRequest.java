package ma.foodplus.ordering.system.promos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ApplyPromotionRequest {
    @NotNull
    private Long customerId;

    @NotEmpty
    private List<@Valid CartItemDto> cartItems; // Note the @Valid on the list items too!

    public Long getCustomerId(){
        return customerId;
    }

    public List<CartItemDto> getCartItems(){
        return cartItems;
    }

    public void setCustomerId(Long customerId){
        this.customerId=customerId;
    }

    public void setCartItems(List<CartItemDto> cartItems){
        this.cartItems=cartItems;
    }
}
