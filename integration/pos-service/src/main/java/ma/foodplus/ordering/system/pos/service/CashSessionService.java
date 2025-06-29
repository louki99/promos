package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.CashSession;
import ma.foodplus.ordering.system.pos.domain.Store;
import ma.foodplus.ordering.system.pos.domain.Terminal;
import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.dto.CashSessionCloseRequest;
import ma.foodplus.ordering.system.pos.dto.CashSessionOpenRequest;
import ma.foodplus.ordering.system.pos.enums.CashSessionStatus;
import ma.foodplus.ordering.system.pos.repository.CashSessionRepository;
import ma.foodplus.ordering.system.pos.repository.StoreRepository;
import ma.foodplus.ordering.system.pos.repository.TerminalRepository;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CashSessionService {

    @Autowired
    private CashSessionRepository cashSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    public CashSession openSession(Long cashierId, CashSessionOpenRequest request) {
        // Validate cashier exists
        User cashier = userRepository.findById(cashierId)
                .orElseThrow(() -> new RuntimeException("Cashier not found"));

        // Validate store exists
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // Validate terminal exists
        Terminal terminal = terminalRepository.findById(request.getTerminalId())
                .orElseThrow(() -> new RuntimeException("Terminal not found"));

        // Check if cashier already has an open session
        if (cashSessionRepository.existsByCashierIdAndStatus(cashierId, CashSessionStatus.OPEN)) {
            throw new RuntimeException("Cashier already has an open session");
        }

        // Check if terminal already has an open session
        if (cashSessionRepository.existsByTerminalIdAndStatus(request.getTerminalId(), CashSessionStatus.OPEN)) {
            throw new RuntimeException("Terminal already has an open session");
        }

        // Create new cash session
        CashSession cashSession = new CashSession(cashier, store, terminal, request.getInitialAmount());
        cashSession.setNotes(request.getNotes());

        return cashSessionRepository.save(cashSession);
    }

    public CashSession closeSession(Long cashierId, CashSessionCloseRequest request) {
        // Find open session for cashier
        CashSession cashSession = cashSessionRepository.findByCashierIdAndStatus(cashierId, CashSessionStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("No open session found for cashier"));

        // Close the session
        cashSession.closeSession(request.getCashCollected());
        if (request.getNotes() != null && !request.getNotes().trim().isEmpty()) {
            cashSession.setNotes(cashSession.getNotes() + "\n" + request.getNotes());
        }

        return cashSessionRepository.save(cashSession);
    }

    public Optional<CashSession> getCurrentSession(Long cashierId) {
        return cashSessionRepository.findByCashierIdAndStatus(cashierId, CashSessionStatus.OPEN);
    }

    public Optional<CashSession> getCurrentSession(Long cashierId, Long storeId) {
        return cashSessionRepository.findByCashierIdAndStoreIdAndStatus(cashierId, storeId, CashSessionStatus.OPEN);
    }

    public Optional<CashSession> getCurrentSession(Long cashierId, Long storeId, Long terminalId) {
        return cashSessionRepository.findByCashierIdAndStoreIdAndTerminalIdAndStatus(
                cashierId, storeId, terminalId, CashSessionStatus.OPEN);
    }

    public CashSession getSessionById(UUID sessionId) {
        return cashSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Cash session not found"));
    }

    public List<CashSession> getSessionsByCashier(Long cashierId) {
        return cashSessionRepository.findByCashierIdOrderByOpenedAtDesc(cashierId);
    }

    public List<CashSession> getSessionsByStore(Long storeId) {
        return cashSessionRepository.findByStoreIdOrderByOpenedAtDesc(storeId);
    }

    public List<CashSession> getSessionsByTerminal(Long terminalId) {
        return cashSessionRepository.findByTerminalIdOrderByOpenedAtDesc(terminalId);
    }

    public List<CashSession> getSessionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return cashSessionRepository.findByDateRange(startDate, endDate);
    }

    public List<CashSession> getSessionsByStoreAndDateRange(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        return cashSessionRepository.findByStoreAndDateRange(storeId, startDate, endDate);
    }

    public List<CashSession> getOpenSessions() {
        return cashSessionRepository.findByStatus(CashSessionStatus.OPEN);
    }

    public void addSaleToSession(Long cashierId, BigDecimal saleAmount) {
        Optional<CashSession> currentSession = getCurrentSession(cashierId);
        if (currentSession.isPresent()) {
            CashSession session = currentSession.get();
            session.addSale(saleAmount);
            cashSessionRepository.save(session);
        }
    }

    public boolean hasOpenSession(Long cashierId) {
        return cashSessionRepository.existsByCashierIdAndStatus(cashierId, CashSessionStatus.OPEN);
    }

    public boolean hasOpenSession(Long cashierId, Long storeId) {
        return cashSessionRepository.findByCashierIdAndStoreIdAndStatus(cashierId, storeId, CashSessionStatus.OPEN).isPresent();
    }

    public boolean hasOpenSession(Long cashierId, Long storeId, Long terminalId) {
        return cashSessionRepository.findByCashierIdAndStoreIdAndTerminalIdAndStatus(
                cashierId, storeId, terminalId, CashSessionStatus.OPEN).isPresent();
    }

    public void deleteSession(UUID sessionId) {
        CashSession session = getSessionById(sessionId);
        if (session.getStatus() == CashSessionStatus.OPEN) {
            throw new RuntimeException("Cannot delete an open session");
        }
        cashSessionRepository.deleteById(sessionId);
    }
} 