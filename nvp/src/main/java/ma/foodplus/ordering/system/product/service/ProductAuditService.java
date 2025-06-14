package ma.foodplus.ordering.system.product.service;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.product.model.Product;
import ma.foodplus.ordering.system.product.model.ProductAudit;
import ma.foodplus.ordering.system.product.repository.ProductAuditRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductAuditService {

    private final ProductAuditRepository auditRepository;

    public List<ProductAudit> findByProductId(Long productId) {
        return auditRepository.findByProductId(productId);
    }

    public Page<ProductAudit> findByProductId(Long productId, Pageable pageable) {
        return auditRepository.findByProductId(productId, pageable);
    }

    public List<ProductAudit> findByUserId(String userId) {
        return auditRepository.findByUserId(userId);
    }

    public Page<ProductAudit> findByUserId(String userId, Pageable pageable) {
        return auditRepository.findByUserId(userId, pageable);
    }

    public List<ProductAudit> findByActionAndTimestampBetween(AuditAction action, ZonedDateTime startDate, ZonedDateTime endDate) {
        return auditRepository.findByActionAndTimestampBetween(action, startDate, endDate);
    }

    public List<ProductAudit> findByProductIdAndAction(Long productId, AuditAction action) {
        return auditRepository.findByProductIdAndAction(productId, action);
    }

    public List<ProductAudit> findByProductIdAndTimestampBetween(Long productId, ZonedDateTime startDate, ZonedDateTime endDate) {
        return auditRepository.findByProductIdAndTimestampBetween(productId, startDate, endDate);
    }

    @Transactional
    public void logProductCreation(Product product, String userId) {
        ProductAudit audit = ProductAudit.builder()
            .productId(product.getId())
            .action(AuditAction.CREATE)
            .userId(userId)
            .timestamp(ZonedDateTime.now())
            .details(createAuditDetails(product))
            .build();
        
        auditRepository.save(audit);
    }

    @Transactional
    public void logProductUpdate(Product oldProduct, Product newProduct, String userId) {
        Map<String, String> changes = compareProducts(oldProduct, newProduct);
        if (!changes.isEmpty()) {
            ProductAudit audit = ProductAudit.builder()
                .productId(newProduct.getId())
                .action(AuditAction.UPDATE)
                .userId(userId)
                .timestamp(ZonedDateTime.now())
                .changes(changes)
                .build();
            
            auditRepository.save(audit);
        }
    }

    @Transactional
    public void logProductDeletion(Product product, String userId) {
        ProductAudit audit = ProductAudit.builder()
            .productId(product.getId())
            .action(AuditAction.DELETE)
            .userId(userId)
            .timestamp(ZonedDateTime.now())
            .details(createAuditDetails(product))
            .build();
        
        auditRepository.save(audit);
    }

    @Transactional
    public void logPriceChange(Product product, BigDecimal oldPrice, BigDecimal newPrice, String userId) {
        Map<String, String> changes = new HashMap<>();
        changes.put("oldPrice", oldPrice.toString());
        changes.put("newPrice", newPrice.toString());

        ProductAudit audit = ProductAudit.builder()
            .productId(product.getId())
            .action(AuditAction.PRICE_CHANGE)
            .userId(userId)
            .timestamp(ZonedDateTime.now())
            .changes(changes)
            .build();
        
        auditRepository.save(audit);
    }

    @Transactional
    public void logStatusChange(Product product, boolean oldStatus, boolean newStatus, String userId) {
        Map<String, String> changes = new HashMap<>();
        changes.put("oldStatus", String.valueOf(oldStatus));
        changes.put("newStatus", String.valueOf(newStatus));

        ProductAudit audit = ProductAudit.builder()
            .productId(product.getId())
            .action(AuditAction.STATUS_CHANGE)
            .userId(userId)
            .timestamp(ZonedDateTime.now())
            .changes(changes)
            .build();
        
        auditRepository.save(audit);
    }

    private Map<String, String> createAuditDetails(Product product) {
        Map<String, String> details = new HashMap<>();
        details.put("reference", product.getReference());
        details.put("title", product.getTitle());
        details.put("description", product.getDescription());
        details.put("salePrice", product.getSalePrice().toString());
        details.put("inactive", String.valueOf(product.getInactive()));
        return details;
    }

    private Map<String, String> compareProducts(Product oldProduct, Product newProduct) {
        Map<String, String> changes = new HashMap<>();

        if (!Objects.equals(oldProduct.getReference(), newProduct.getReference())) {
            changes.put("reference", String.format("%s -> %s", oldProduct.getReference(), newProduct.getReference()));
        }
        if (!Objects.equals(oldProduct.getTitle(), newProduct.getTitle())) {
            changes.put("title", String.format("%s -> %s", oldProduct.getTitle(), newProduct.getTitle()));
        }
        if (!Objects.equals(oldProduct.getDescription(), newProduct.getDescription())) {
            changes.put("description", String.format("%s -> %s", oldProduct.getDescription(), newProduct.getDescription()));
        }
        if (!Objects.equals(oldProduct.getSalePrice(), newProduct.getSalePrice())) {
            changes.put("salePrice", String.format("%s -> %s", oldProduct.getSalePrice(), newProduct.getSalePrice()));
        }
        if (!Objects.equals(oldProduct.getInactive(), newProduct.getInactive())) {
            changes.put("inactive", String.format("%s -> %s", oldProduct.getInactive(), newProduct.getInactive()));
        }
        if (!Objects.equals(oldProduct.getDeliverable(), newProduct.getDeliverable())) {
            changes.put("deliverable", String.format("%s -> %s", oldProduct.getDeliverable(), newProduct.getDeliverable()));
        }
        if (!Objects.equals(oldProduct.getStockTracking(), newProduct.getStockTracking())) {
            changes.put("stockTracking", String.format("%s -> %s", oldProduct.getStockTracking(), newProduct.getStockTracking()));
        }  

        return changes;
    }

    public enum AuditAction {
        CREATE,
        UPDATE,
        DELETE,
        PRICE_CHANGE,
        STATUS_CHANGE
    }
} 