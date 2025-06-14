package ma.foodplus.ordering.system.product.repository;

import io.lettuce.core.dynamic.annotation.Param;
import ma.foodplus.ordering.system.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByReference(String reference);
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByProductFamilyCode(String familyCode);
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
    @Query("SELECT p.promoSkuPoints FROM Product p WHERE p.id = :productId")
    Optional<BigDecimal> findPromoSkuPointsById(@Param("productId") Long productId);

    // Advanced search methods
    @Query("SELECT p FROM Product p WHERE " +
           "(:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
           "(:familyCode IS NULL OR p.productFamily.code = :familyCode) AND " +
           "(:minPrice IS NULL OR p.salePrice >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.salePrice <= :maxPrice) AND " +
           "(:isDeliverable IS NULL OR p.deliverable = :isDeliverable) AND " +
           "(:isActive IS NULL OR p.inactive = :isActive) AND " +
           "(:isWholesaleOnly IS NULL OR p.isWholesaleOnly = :isWholesaleOnly) AND " +
           "(:requiresColdStorage IS NULL OR p.requiresColdStorage = :requiresColdStorage) AND " +
           "(:createdAfter IS NULL OR p.createdAt >= :createdAfter) AND " +
           "(:createdBefore IS NULL OR p.createdAt <= :createdBefore)")
    Page<Product> searchProducts(
        @Param("title") String title,
        @Param("description") String description,
        @Param("familyCode") String familyCode,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("isDeliverable") Boolean isDeliverable,
        @Param("isActive") Boolean isActive,
        @Param("isWholesaleOnly") Boolean isWholesaleOnly,
        @Param("requiresColdStorage") Boolean requiresColdStorage,
        @Param("createdAfter") ZonedDateTime createdAfter,
        @Param("createdBefore") ZonedDateTime createdBefore,
        Pageable pageable
    );

    // Full-text search
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.reference) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> fullTextSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Category-based search
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id IN :categoryIds")
    Page<Product> findByCategories(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    // Price range search with category
    @Query("SELECT p FROM Product p JOIN p.categories c " +
           "WHERE c.id = :categoryId AND p.salePrice BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByCategoryAndPriceRange(
        @Param("categoryId") Long categoryId,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        Pageable pageable
    );

    // Recently updated products
    @Query("SELECT p FROM Product p WHERE p.updatedAt >= :since")
    Page<Product> findRecentlyUpdated(@Param("since") ZonedDateTime since, Pageable pageable);

    // Find products by multiple references
    List<Product> findByReferenceIn(List<String> references);

    // Find products by multiple barcodes
    List<Product> findByBarcodeIn(List<String> barcodes);

    // Find products by supplier
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.supplierCode) = LOWER(:supplierCode) OR " +
           "LOWER(p.supplierName) LIKE LOWER(CONCAT('%', :supplierName, '%'))")
    Page<Product> findBySupplier(
        @Param("supplierCode") String supplierCode,
        @Param("supplierName") String supplierName,
        Pageable pageable
    );
} 