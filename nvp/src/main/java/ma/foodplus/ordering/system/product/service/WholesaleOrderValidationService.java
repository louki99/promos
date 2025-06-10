package ma.foodplus.ordering.system.product.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.product.dto.BulkOrderRequestDto;
import ma.foodplus.ordering.system.product.dto.WholesalePriceRequestDto;
import ma.foodplus.ordering.system.product.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class WholesaleOrderValidationService {

    /**
     * Validate a wholesale order
     * @param product The product being ordered
     * @param quantity The quantity being ordered
     * @return true if the order is valid
     */
    public boolean validateOrder(Product product, Integer quantity) {
        if (product == null || quantity == null) {
            return false;
        }

        // Check if product is available for wholesale
        if (!product.isAvailableForWholesale(quantity, false)) {
            return false;
        }

        // Check minimum quantity
        if (quantity < product.getWholesaleMinimumQuantity()) {
            return false;
        }

        // Check maximum quantity if set
        if (product.getMaximumOrderQuantity() != null && quantity > product.getMaximumOrderQuantity()) {
            return false;
        }

        // Check bulk package size if applicable
        if (product.getIsBulkItem() && product.getBulkPackageSize() != null) {
            if (quantity % product.getBulkPackageSize() != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validate a wholesale price request
     * @param request The wholesale price request
     * @param product The product being ordered
     * @param hasContract Whether the customer has a valid contract
     * @return A list of validation errors, empty if valid
     */
    public List<String> validateWholesalePriceRequest(
            @Valid WholesalePriceRequestDto request,
            Product product,
            boolean hasContract) {
        
        List<String> errors = new ArrayList<>();

        if (product == null) {
            errors.add("Product not found");
            return errors;
        }

        if (!product.isAvailableForWholesale(request.getQuantity(), hasContract)) {
            errors.add("Product is not available for wholesale at this quantity");
        }

        if (product.getRequiresContract() && !hasContract) {
            errors.add("This product requires a valid contract");
        }

        if (request.getQuantity() < product.getWholesaleMinimumQuantity()) {
            errors.add(String.format("Minimum wholesale quantity is %d", 
                product.getWholesaleMinimumQuantity()));
        }

        return errors;
    }

    /**
     * Validate a bulk order request
     * @param request The bulk order request
     * @param product The product being ordered
     * @return A list of validation errors, empty if valid
     */
    public List<String> validateBulkOrderRequest(
            @Valid BulkOrderRequestDto request,
            Product product) {
        
        List<String> errors = new ArrayList<>();

        if (product == null) {
            errors.add("Product not found");
            return errors;
        }

        if (!product.getIsBulkItem()) {
            errors.add("Product is not available for bulk ordering");
        }

        if (product.getBulkPackageSize() == null) {
            errors.add("Product does not have a defined bulk package size");
        }

        if (product.getBulkDiscountThreshold() == null) {
            errors.add("Product does not have a defined bulk discount threshold");
        }

        return errors;
    }

    /**
     * Validate a product for bulk ordering
     * @param product The product to validate
     * @return A list of validation errors, empty if valid
     */
    public List<String> validateProductForBulkOrdering(Product product) {
        List<String> errors = new ArrayList<>();

        if (product == null) {
            errors.add("Product not found");
            return errors;
        }

        if (!product.getIsBulkItem()) {
            errors.add("Product is not available for bulk ordering");
        }

        if (product.getBulkPackageSize() == null) {
            errors.add("Product does not have a defined bulk package size");
        }

        if (product.getBulkDiscountThreshold() == null) {
            errors.add("Product does not have a defined bulk discount threshold");
        }

        return errors;
    }
} 