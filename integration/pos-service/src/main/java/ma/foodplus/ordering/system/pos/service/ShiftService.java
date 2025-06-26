package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Shift;
import ma.foodplus.ordering.system.pos.enums.ShiftStatus;
import ma.foodplus.ordering.system.pos.repository.ShiftRepository;
import ma.foodplus.ordering.system.pos.dto.ShiftOpenRequest;
import ma.foodplus.ordering.system.pos.dto.ShiftCloseRequest;
import ma.foodplus.ordering.system.pos.dto.ShiftResponse;
import ma.foodplus.ordering.system.pos.domain.Terminal;
import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.domain.Store;
import ma.foodplus.ordering.system.pos.repository.TerminalRepository;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import ma.foodplus.ordering.system.pos.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private TerminalRepository terminalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(Long id) {
        return shiftRepository.findById(id);
    }

    public List<Shift> getShiftsByTerminal(Long terminalId) {
        return shiftRepository.findByTerminalId(terminalId);
    }

    public List<Shift> getShiftsByCashier(Long cashierId) {
        return shiftRepository.findByCashierId(cashierId);
    }

    public List<Shift> getShiftsByStore(Long storeId) {
        return shiftRepository.findByStoreId(storeId);
    }

    public List<Shift> getShiftsByStatus(ShiftStatus status) {
        return shiftRepository.findByStatus(status);
    }

    public List<Shift> getShiftsByPeriod(LocalDateTime start, LocalDateTime end) {
        return shiftRepository.findByStartTimeBetween(start, end);
    }

    public Shift createShift(Shift shift) {
        shift.setStartTime(LocalDateTime.now());
        return shiftRepository.save(shift);
    }

    public ShiftResponse openShift(ShiftOpenRequest request) {
        // Check for existing open shift for this cashier/terminal
        List<Shift> openShifts = shiftRepository.findByTerminalId(request.getTerminalId())
            .stream().filter(s -> s.getCashier().getId().equals(request.getCashierId()) && s.getStatus() == ShiftStatus.OPEN).toList();
        if (!openShifts.isEmpty()) {
            throw new RuntimeException("There is already an open shift for this cashier and terminal.");
        }
        Terminal terminal = terminalRepository.findById(request.getTerminalId())
            .orElseThrow(() -> new RuntimeException("Terminal not found"));
        User cashier = userRepository.findById(request.getCashierId())
            .orElseThrow(() -> new RuntimeException("Cashier not found"));
        Store store = storeRepository.findById(request.getStoreId())
            .orElseThrow(() -> new RuntimeException("Store not found"));
        Shift shift = new Shift(terminal, cashier, store, request.getOpeningBalance());
        shift.setOpenedAt(java.time.LocalDateTime.now());
        shift.setStatus(ShiftStatus.OPEN);
        Shift saved = shiftRepository.save(shift);
        return toResponse(saved);
    }

    public ShiftResponse closeShift(Long shiftId, ShiftCloseRequest request) {
        Shift shift = shiftRepository.findById(shiftId)
            .orElseThrow(() -> new RuntimeException("Shift not found"));
        if (shift.getStatus() != ShiftStatus.OPEN) {
            throw new RuntimeException("Shift is not open");
        }
        shift.setClosingBalance(request.getClosingBalance());
        shift.setClosedAt(java.time.LocalDateTime.now());
        shift.setEndTime(java.time.LocalDateTime.now());
        shift.setStatus(ShiftStatus.CLOSED);
        Shift saved = shiftRepository.save(shift);
        return toResponse(saved);
    }

    public ShiftResponse getCurrentActiveShift(Long terminalId, Long cashierId) {
        List<Shift> openShifts = shiftRepository.findByTerminalId(terminalId)
            .stream().filter(s -> s.getCashier().getId().equals(cashierId) && s.getStatus() == ShiftStatus.OPEN).toList();
        if (openShifts.isEmpty()) {
            return null;
        }
        return toResponse(openShifts.get(0));
    }

    private ShiftResponse toResponse(Shift shift) {
        ShiftResponse dto = new ShiftResponse();
        dto.setId(shift.getId());
        dto.setTerminalId(shift.getTerminal().getId());
        dto.setCashierId(shift.getCashier().getId());
        dto.setStoreId(shift.getStore().getId());
        dto.setOpeningBalance(shift.getOpeningBalance());
        dto.setClosingBalance(shift.getClosingBalance());
        dto.setOpenedAt(shift.getOpenedAt());
        dto.setClosedAt(shift.getClosedAt());
        dto.setStatus(shift.getStatus());
        return dto;
    }

    public void deleteShift(Long id) {
        shiftRepository.deleteById(id);
    }
} 