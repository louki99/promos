package ma.foodplus.ordering.system.customer.service;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.customer.model.ProductCustomer;
import ma.foodplus.ordering.system.customer.repository.ProductCustomerRepository;
import ma.foodplus.ordering.system.product.service.ProductService;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCustomerValidationService {
    
    private final ProductCustomerRepository productCustomerRepository;
    private final ProductService productService;

    public List<String> validateProductCustomer(ProductCustomer productCustomer) {
        List<String> errors = new ArrayList<>();

        // Basic validations
        if (productCustomer.getProduct() == null) {
            errors.add("Product is required");
        }
        if (productCustomer.getCustomer() == null) {
            errors.add("Customer is required");
        }
        if (productCustomer.getPrixTTC() == null || productCustomer.getPrixTTC().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Price must be greater than 0");
        }

        // Business rule validations
        validatePriceRules(productCustomer, errors);
        validateDiscountRules(productCustomer, errors);
        validateQuantityRules(productCustomer, errors);
        validateCategoryRules(productCustomer, errors);
        validateCoefficientRules(productCustomer, errors);

        return errors;
    }

    private void validatePriceRules(ProductCustomer productCustomer, List<String> errors) {
        if (productCustomer.getPrixTTC() != null) {
            // Check if price is within acceptable range (e.g., not more than 3x the base price)
            BigDecimal basePrice = productCustomer.getProduct().getSalePrice();
            if (productCustomer.getPrixTTC().compareTo(basePrice.multiply(new BigDecimal("3"))) > 0) {
                errors.add("Price cannot be more than 3 times the base price");
            }

            // Check if new price is valid
            if (productCustomer.getPrixVenNouv() != null) {
                if (productCustomer.getPrixVenNouv().compareTo(BigDecimal.ZERO) <= 0) {
                    errors.add("New price must be greater than 0");
                }
                if (productCustomer.getPrixVenNouv().compareTo(basePrice.multiply(new BigDecimal("3"))) > 0) {
                    errors.add("New price cannot be more than 3 times the base price");
                }
            }
        }
    }

    private void validateDiscountRules(ProductCustomer productCustomer, List<String> errors) {
        if (productCustomer.getRemise() != null) {
            // Check if discount is within acceptable range (e.g., not more than 50%)
            if (productCustomer.getRemise().compareTo(new BigDecimal("50")) > 0) {
                errors.add("Discount cannot be more than 50%");
            }

            // Check if new discount is valid
            if (productCustomer.getRemiseNouv() != null) {
                if (productCustomer.getRemiseNouv().compareTo(BigDecimal.ZERO) < 0) {
                    errors.add("New discount cannot be negative");
                }
                if (productCustomer.getRemiseNouv().compareTo(new BigDecimal("50")) > 0) {
                    errors.add("New discount cannot be more than 50%");
                }
            }
        }
    }

    private void validateQuantityRules(ProductCustomer productCustomer, List<String> errors) {
        if (productCustomer.getQteMont() != null) {
            // Check if quantity is within acceptable range
            if (productCustomer.getQteMont() <= 0) {
                errors.add("Quantity must be greater than 0");
            }
            if (productCustomer.getQteMont() > 10000) {
                errors.add("Quantity cannot exceed 10000");
            }
        }
    }

    private void validateCategoryRules(ProductCustomer productCustomer, List<String> errors) {
        if (productCustomer.getCategory() != null) {
            // Check if category is valid
            if (productCustomer.getCategory().length() > 50) {
                errors.add("Category name cannot exceed 50 characters");
            }
        }
    }

    private void validateCoefficientRules(ProductCustomer productCustomer, List<String> errors) {
        if (productCustomer.getCoef() != null) {
            // Check if coefficient is within acceptable range
            if (productCustomer.getCoef().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Coefficient must be greater than 0");
            }
            if (productCustomer.getCoef().compareTo(new BigDecimal("10")) > 0) {
                errors.add("Coefficient cannot exceed 10");
            }
        }
    }

    public boolean isPriceChangeValid(ProductCustomer productCustomer, BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Check if price change is within acceptable range (e.g., not more than 20% increase)
        BigDecimal currentPrice = productCustomer.getPrixTTC();
        BigDecimal maxAllowedPrice = currentPrice.multiply(new BigDecimal("1.2"));
        
        return newPrice.compareTo(maxAllowedPrice) <= 0;
    }

    public boolean isDiscountChangeValid(ProductCustomer productCustomer, BigDecimal newDiscount) {
        if (newDiscount == null) {
            return false;
        }

        // Check if discount is within acceptable range
        return newDiscount.compareTo(BigDecimal.ZERO) >= 0 && 
               newDiscount.compareTo(new BigDecimal("50")) <= 0;
    }

    public boolean isDuplicateProductCustomer(ProductCustomer productCustomer) {
        return productCustomerRepository.findByCustomerIdAndProductId(
                productCustomer.getCustomer().getId(),
                productCustomer.getProduct().getId()
        ).isPresent();
    }
} 