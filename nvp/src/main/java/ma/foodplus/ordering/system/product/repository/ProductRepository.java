package ma.foodplus.ordering.system.product.repository;

import io.lettuce.core.dynamic.annotation.Param;
import ma.foodplus.ordering.system.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByReference(String reference);
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByFamilyCode(String familyCode);
    List<Product> findByDeliverableTrue();
    List<Product> findByInactiveFalse();
    boolean existsByReference(String reference);
    boolean existsByBarcode(String barcode);

    /**
     * A highly optimized query to fetch only the SKU points for a given product ID.
     * This avoids loading the entire Product entity into memory when only one field is needed.
     *
     * @param productId The ID of the product.
     * @return An Optional containing the promoSkuPoints if the product is found.
     */
    @Query ("SELECT p.promoSkuPoints FROM Product p WHERE p.id = :productId")
    Optional<BigDecimal> findPromoSkuPointsById(@Param ("productId") Long productId);

} 