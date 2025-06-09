package ma.foodplus.ordering.system.promos.component;

import ma.foodplus.ordering.system.promos.dto.CartItemDto;
import ma.foodplus.ordering.system.promos.service.CartItemContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the entire shopping cart and its state during the promotion calculation process.
 * It holds the list of items and provides helper methods to query its state.
 */
public class Cart {

    private final Long customerId;
    private final List<CartItemContext> itemContexts; // The list of stateful item wrappers.

    // This constructor takes the raw request DTOs and converts them into our domain model.
    // In a real app, this might also involve a service call to fetch product details.
    public Cart(Long customerId, List<CartItemDto> itemDtos) {
        this.customerId = customerId;
        this.itemContexts = itemDtos.stream()
                // For each DTO, create the immutable CartItem and then wrap it in a stateful CartItemContext
                .map(dto -> {
                    // In a real app, you would fetch productName, familyId, etc., from a ProductService here.
                    // For this example, we'll use placeholder data.
                    CartItem originalItem = new CartItem(
                            dto.getProductId(),
                            "Product " + dto.getProductId(), // Placeholder name
                            dto.getQuantity(),
                            dto.getUnitPrice(),
                            1L, // Placeholder familyId
                            BigDecimal.ONE // Placeholder SKU points
                    );
                    return new CartItemContext(originalItem);
                })
                .collect(Collectors.toList());
    }

    // --- Public Getters for State ---

    public Long getCustomerId() {
        return customerId;
    }

    public List<CartItemContext> getItemContexts() {
        return itemContexts;
    }

    // --- Helper Methods for the Engine ---

    /**
     * Calculates the current total price of the cart after applying all discounts so far.
     * @return The final total price.
     */
    public BigDecimal getFinalTotalPrice() {
        return itemContexts.stream()
                .map(CartItemContext::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Finds all items in the cart that match a specific product ID.
     * @param productId The ID of the product to find.
     * @return A list of matching CartItemContexts.
     */
    public List<CartItemContext> findItemsByProductId(Long productId) {
        return this.itemContexts.stream()
                .filter(itemCtx -> itemCtx.getOriginalItem().getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    /**
     * Finds all items in the cart that belong to a specific product family.
     * @param familyId The ID of the product family.
     * @return A list of matching CartItemContexts.
     */
    public List<CartItemContext> findItemsByFamilyId(Long familyId) {
        return this.itemContexts.stream()
                .filter(itemCtx -> itemCtx.getOriginalItem().getProductFamilyId() != null &&
                        itemCtx.getOriginalItem().getProductFamilyId().equals(familyId))
                .collect(Collectors.toList());
    }
}
