package ma.foodplus.ordering.system.store.repository;

import ma.foodplus.ordering.system.store.entity.StoreEntity;
import ma.foodplus.ordering.system.store.entity.StoreEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreJpaRepository extends JpaRepository<StoreEntity, StoreEntityId> {

    Optional<List<StoreEntity>> findByStoreIdIdAndProductIdIn(UUID storeId, List<UUID> productIds);
}
