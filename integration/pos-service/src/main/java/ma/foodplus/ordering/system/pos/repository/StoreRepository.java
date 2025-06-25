package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByCode(String code);
    List<Store> findByActiveTrue();
    List<Store> findByCityIgnoreCase(String city);
    boolean existsByCode(String code);
}