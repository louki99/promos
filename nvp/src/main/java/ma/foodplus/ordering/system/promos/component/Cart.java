package ma.foodplus.ordering.system.promos.component;

import ma.foodplus.ordering.system.promos.dto.CartItemDto;
import ma.foodplus.ordering.system.promos.component.CartItemContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the entire shopping cart and its state during the promotion calculation process.
 * It holds the list of items and provides helper methods to query its state.
 */
public class Cart {

    private Long customerId;
    private List<CartItemContext> items;
    private BigDecimal subtotal;
    private BigDecimal finalTotalPrice;

    public Cart() {
        this.items = new ArrayList<>();
        this.subtotal = BigDecimal.ZERO;
        this.finalTotalPrice = BigDecimal.ZERO;
    }

    // This constructor takes the raw request DTOs and converts them into our domain model.
    // In a real app, this might also involve a service call to fetch product details.
    public Cart(Long customerId, List<CartItemDto> itemDtos) {
        this.customerId = customerId;
        this.items = itemDtos.stream()
                // For each DTO, create the immutable CartItem and then wrap it in a stateful CartItemContext
                .map(dto -> {
                    // In a real app, you would fetch productName, familyId, etc., from a ProductService here.
                    // For this example, we'll use placeholder data.
                    CartItem originalItem = new CartItem(
                            dto.getProductId(),
                            1L, // Placeholder familyId
                            "Product " + dto.getProductId(), // Placeholder name
                            dto.getUnitPrice(),
                            dto.getQuantity(),
                            "SKU-" + dto.getProductId(), // Placeholder SKU
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

    public List<CartItemContext> getItems() {
        return items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getFinalTotalPrice() {
        return finalTotalPrice;
    }

    // --- Helper Methods for the Engine ---

    /**
     * Finds all items in the cart that match a specific product ID.
     * @param productId The ID of the product to find.
     * @return A list of matching CartItemContexts.
     */
    public List<CartItemContext> findItemsByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getOriginalItem().getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    /**
     * Finds all items in the cart that belong to a specific product family.
     * @param familyId The ID of the product family.
     * @return A list of matching CartItemContexts.
     */
    public List<CartItemContext> findItemsByFamilyId(Long familyId) {
        return items.stream()
                .filter(item -> item.getOriginalItem().getProductFamilyId().equals(familyId))
                .collect(Collectors.toList());
    }

    public void addItem(CartItemContext item) {
        items.add(item);
        recalculateTotals();
    }

    public void removeItem(CartItemContext item) {
        items.remove(item);
        recalculateTotals();
    }

    private void recalculateTotals() {
        this.subtotal = items.stream()
                .map(item -> item.getOriginalItem().getPrice().multiply(new BigDecimal(item.getOriginalItem().getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Apply any discounts or adjustments here
        this.finalTotalPrice = this.subtotal;
    }
}
