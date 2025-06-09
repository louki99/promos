package ma.foodplus.ordering.system.product.repository;

import ma.foodplus.ordering.system.product.model.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
    Optional<ProductJpaEntity> findByReference(String reference);
    Optional<ProductJpaEntity> findByBarcode(String barcode);
    List<ProductJpaEntity> findByFamilyCode(String familyCode);
    List<ProductJpaEntity> findByDeliverableTrue();
    List<ProductJpaEntity> findByInactiveFalse();
    boolean existsByReference(String reference);
    boolean existsByBarcode(String barcode);
} 