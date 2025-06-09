package ma.foodplus.ordering.system.customer.repository;

import ma.foodplus.ordering.system.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCtNum(String ctNum);
    Optional<Customer> findByIce(String ice);
    boolean existsByCtNum(String ctNum);
    boolean existsByIce(String ice);
    
    @Query("SELECT c FROM Customer c WHERE c.active = true")
    List<Customer> findAllActive();
    
    @Query("SELECT c FROM Customer c WHERE c.cateTarif.id = :categoryTarifId")
    List<Customer> findByCategoryTarifId(Long categoryTarifId);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchByDescription(String searchTerm);
} 