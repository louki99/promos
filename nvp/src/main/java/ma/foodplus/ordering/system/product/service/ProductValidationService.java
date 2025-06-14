package ma.foodplus.ordering.system.product.service;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.product.dto.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.dto.update.UpdateProductCommand;
import ma.foodplus.ordering.system.product.model.Product;
import ma.foodplus.ordering.system.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductValidationService {

    private final ProductRepository productRepository;

    public List<String> validateCreateProduct(CreateProductCommand command) {
        List<String> errors = new ArrayList<>();

        // Required fields validation
        if (command.reference() == null || command.reference().trim().isEmpty()) {
            errors.add("Product reference is required");
        }
        if (command.title() == null || command.title().trim().isEmpty()) {
            errors.add("Product title is required");
        }
        if (command.description() == null || command.description().trim().isEmpty()) {
            errors.add("Product description is required");
        }
        if (command.salePrice() == null || command.salePrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Sale price must be greater than zero");
        }

        // Business rules validation
        if (command.reference() != null && productRepository.existsByReference(command.reference())) {
            errors.add("Product reference already exists");
        }
        if (command.barcode() != null && productRepository.existsByBarcode(command.barcode())) {
            errors.add("Product barcode already exists");
        }

        // Price validation
        if (command.salePrice() != null && command.priceIncludingTax() != null) {
            if (command.priceIncludingTax().compareTo(command.salePrice()) < 0) {
                errors.add("Price including tax cannot be less than sale price");
            }
        }

        return errors;
    }

    public List<String> validateUpdateProduct(UpdateProductCommand command, Product existingProduct) {
        List<String> errors = new ArrayList<>();

        // Required fields validation
        if (command.reference() == null || command.reference().trim().isEmpty()) {
            errors.add("Product reference is required");
        }
        if (command.title() == null || command.title().trim().isEmpty()) {
            errors.add("Product title is required");
        }
        if (command.description() == null || command.description().trim().isEmpty()) {
            errors.add("Product description is required");
        }
        if (command.salePrice() == null || command.salePrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Sale price must be greater than zero");
        }

        // Business rules validation
        if (command.reference() != null && !command.reference().equals(existingProduct.getReference()) 
            && productRepository.existsByReference(command.reference())) {
            errors.add("Product reference already exists");
        }
        if (command.barcode() != null && !command.barcode().equals(existingProduct.getBarcode()) 
            && productRepository.existsByBarcode(command.barcode())) {
            errors.add("Product barcode already exists");
        }

        // Price validation
        if (command.salePrice() != null && command.priceIncludingTax() != null) {
            if (command.priceIncludingTax().compareTo(command.salePrice()) < 0) {
                errors.add("Price including tax cannot be less than sale price");
            }
        }

        return errors;
    }

    public List<String> validateProduct(Product product) {
        List<String> errors = new ArrayList<>();

        // Basic validation
        if (product.getReference() == null || product.getReference().trim().isEmpty()) {
            errors.add("Product reference is required");
        }
        if (product.getTitle() == null || product.getTitle().trim().isEmpty()) {
            errors.add("Product title is required");
        }
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            errors.add("Product description is required");
        }
        if (product.getSalePrice() == null || product.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Sale price must be greater than zero");
        }

        // Business rules validation
        if (product.getIsWholesaleOnly() && product.getWholesalePrice() == null) {
            errors.add("Wholesale price is required for wholesale-only products");
        }
        if (product.getIsPerishable() && !product.getRequiresColdStorage()) {
            errors.add("Perishable products must require cold storage");
        }
        if (product.getB2bMinimumOrderValue() != null && product.getB2bMinimumOrderValue().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("B2B minimum order value must be greater than zero");
        }

        // Price validation
        if (product.getWholesalePrice() != null && product.getSalePrice() != null 
            && product.getWholesalePrice().compareTo(product.getSalePrice()) >= 0) {
            errors.add("Wholesale price must be less than sale price");
        }
        if (product.getB2cPromoPrice() != null && product.getB2cRetailPrice() != null 
            && product.getB2cPromoPrice().compareTo(product.getB2cRetailPrice()) >= 0) {
            errors.add("Promotional price must be less than retail price");
        }

        // Date validation
        if (product.getB2cPromoStartDate() != null && product.getB2cPromoEndDate() != null 
            && product.getB2cPromoEndDate().isBefore(product.getB2cPromoStartDate())) {
            errors.add("Promotion end date must be after start date");
        }

        // Stock validation
        if (product.getIsBulkItem() && (product.getBulkPackageSize() == null || product.getBulkPackageSize() <= 0)) {
            errors.add("Bulk package size is required for bulk items");
        }

        return errors;
    }

    public boolean isValidForB2BSale(Product product) {
        if (!product.isAvailableForB2B()) {
            return false;
        }
        if (product.getIsWholesaleOnly() && product.getWholesalePrice() == null) {
            return false;
        }
        if (product.getB2bMinimumOrderValue() != null && product.getB2bMinimumOrderValue().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return true;
    }

    public boolean isValidForB2CSale(Product product) {
        if (!product.isAvailableForB2C()) {
            return false;
        }
        if (product.getIsWholesaleOnly()) {
            return false;
        }
        if (!product.getB2cDisplayInCatalog()) {
            return false;
        }
        return true;
    }

    public boolean isValidForPromotion(Product product, BigDecimal promoPrice, ZonedDateTime startDate, ZonedDateTime endDate) {
        if (promoPrice == null || promoPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (promoPrice.compareTo(product.getSalePrice()) >= 0) {
            return false;
        }
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            return false;
        }
        if (product.getIsWholesaleOnly()) {
            return false;
        }
        return true;
    }
} 