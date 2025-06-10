package ma.foodplus.ordering.system.customer.repository;

import ma.foodplus.ordering.system.customer.model.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {
    Optional<CustomerGroup> findByName(String name);
    boolean existsByName(String name);
} 