package ma.foodplus.ordering.system.product.service.dataaccess.product.repository;

import ma.foodplus.ordering.system.product.service.dataaccess.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

}