package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.Shift;
import ma.foodplus.ordering.system.pos.enums.ShiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByTerminalId(Long terminalId);
    List<Shift> findByCashierId(Long cashierId);
    List<Shift> findByStoreId(Long storeId);
    List<Shift> findByStatus(ShiftStatus status);
    List<Shift> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
} 