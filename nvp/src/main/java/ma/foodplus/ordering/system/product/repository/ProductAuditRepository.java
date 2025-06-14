package ma.foodplus.ordering.system.product.repository;

import ma.foodplus.ordering.system.product.model.ProductAudit;
import ma.foodplus.ordering.system.product.service.ProductAuditService.AuditAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ProductAuditRepository extends JpaRepository<ProductAudit, Long> {
    
    List<ProductAudit> findByProductId(Long productId);
    
    Page<ProductAudit> findByProductId(Long productId, Pageable pageable);
    
    List<ProductAudit> findByProductIdAndAction(Long productId, AuditAction action);
    
    List<ProductAudit> findByProductIdAndTimestampBetween(
        Long productId, 
        ZonedDateTime startDate, 
        ZonedDateTime endDate
    );
    
    List<ProductAudit> findByUserId(String userId);
    
    Page<ProductAudit> findByUserId(String userId, Pageable pageable);
    
    List<ProductAudit> findByActionAndTimestampBetween(
        AuditAction action,
        ZonedDateTime startDate,
        ZonedDateTime endDate
    );
} 