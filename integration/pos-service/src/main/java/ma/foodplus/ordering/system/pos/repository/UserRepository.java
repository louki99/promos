package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByActiveTrue();
    List<User> findByRole(UserRole role);
    List<User> findByStoreId(Long storeId);

    @Query("SELECT u FROM User u WHERE u.store.id = :storeId AND u.active = true")
    List<User> findActiveUsersByStore(@Param("storeId") Long storeId);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}