package ma.foodplus.ordering.system.product.service;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.product.dto.WholesalePriceRequestDto;
import ma.foodplus.ordering.system.product.dto.WholesalePriceResponseDto;
import ma.foodplus.ordering.system.product.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class WholesalePriceCalculationService {

    private final WholesaleOrderValidationService validationService;

    /**
     * Calculate wholesale price for a product
     * @param product The product to calculate price for
     * @param quantity The quantity being ordered
     * @return The calculated wholesale price
     */
    @Transactional(readOnly = true)
    public BigDecimal calculatePrice(Product product, Integer quantity) {
        if (product == null || quantity == null) {
            throw new IllegalArgumentException("Product and quantity are required");
        }

        // Calculate base wholesale price
        BigDecimal wholesalePrice = product.calculateWholesalePrice(quantity);
        
        // Calculate bulk discount if applicable
        if (product.isEligibleForBulkDiscount(quantity)) {
            BigDecimal bulkDiscount = wholesalePrice.multiply(product.calculateBulkDiscount(quantity))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            wholesalePrice = wholesalePrice.subtract(bulkDiscount);
        }

        return wholesalePrice;
    }

    /**
     * Calculate wholesale price for a product with detailed response
     * @param request The wholesale price request
     * @param product The product to calculate price for
     * @param hasContract Whether the customer has a valid contract
     * @return The calculated wholesale price response
     */
    @Transactional(readOnly = true)
    public WholesalePriceResponseDto calculateWholesalePrice(
            WholesalePriceRequestDto request,
            Product product,
            boolean hasContract) {

        // Validate the request
        var validationErrors = validationService.validateWholesalePriceRequest(request, product, hasContract);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", validationErrors));
        }

        // Calculate base wholesale price
        BigDecimal wholesalePrice = product.calculateWholesalePrice(request.getQuantity());
        
        // Calculate bulk discount if applicable
        BigDecimal bulkDiscount = BigDecimal.ZERO;
        if (request.getIncludeBulkDiscount() && product.isEligibleForBulkDiscount(request.getQuantity())) {
            bulkDiscount = wholesalePrice.multiply(product.calculateBulkDiscount(request.getQuantity()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        // Calculate tax if included
        BigDecimal taxAmount = BigDecimal.ZERO;
        if (request.getIncludeTax() && product.getTaxRate() != null) {
            BigDecimal taxableAmount = wholesalePrice.subtract(bulkDiscount);
            taxAmount = taxableAmount.multiply(product.getTaxRate())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        // Calculate total price
        BigDecimal totalPrice = wholesalePrice
                .subtract(bulkDiscount)
                .add(taxAmount)
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        // Build response
        return WholesalePriceResponseDto.builder()
                .productId(product.getId())
                .productName(product.getTitle())
                .sku(product.getSku())
                .quantity(request.getQuantity())
                .unitPrice(product.getUnitPrice())
                .wholesalePrice(wholesalePrice)
                .bulkDiscount(bulkDiscount)
                .taxAmount(taxAmount)
                .totalPrice(totalPrice)
                .appliedTier(product.getAppliedWholesaleTier(request.getQuantity()))
                .requiresContract(product.getRequiresContract())
                .contractNumber(request.getContractNumber())
                .pricingNotes(product.getWholesalePricingNotes(request.getQuantity()))
                .build();
    }
} 