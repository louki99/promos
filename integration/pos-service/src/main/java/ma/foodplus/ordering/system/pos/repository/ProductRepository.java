package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByActiveTrue();
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByActiveTrueAndCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "p.sku LIKE CONCAT('%', :search, '%') OR " +
            "p.barcode LIKE CONCAT('%', :search, '%'))")
    Page<Product> searchActiveProducts(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
            "p.sellingPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT p FROM Product p JOIN p.inventories i WHERE " +
            "p.active = true AND i.store.id = :storeId AND i.quantity > 0")
    List<Product> findAvailableProductsByStore(@Param("storeId") Long storeId);

    boolean existsBySku(String sku);
    boolean existsByBarcode(String barcode);
}