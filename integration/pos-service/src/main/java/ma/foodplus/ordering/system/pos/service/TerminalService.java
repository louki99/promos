package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Terminal;
import ma.foodplus.ordering.system.pos.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TerminalService {
    @Autowired
    private TerminalRepository terminalRepository;

    public List<Terminal> getAllTerminals() {
        return terminalRepository.findAll();
    }

    public List<Terminal> getActiveTerminals() {
        return terminalRepository.findByActiveTrue();
    }

    public Optional<Terminal> getTerminalById(Long id) {
        return terminalRepository.findById(id);
    }

    public Optional<Terminal> getTerminalByCode(String code) {
        return terminalRepository.findByCode(code);
    }

    public List<Terminal> getTerminalsByStore(Long storeId) {
        return terminalRepository.findByStoreId(storeId);
    }

    public Terminal createTerminal(Terminal terminal) {
        if (terminalRepository.existsByCode(terminal.getCode())) {
            throw new RuntimeException("Terminal code already exists");
        }
        terminal.setCreatedAt(LocalDateTime.now());
        terminal.setUpdatedAt(LocalDateTime.now());
        return terminalRepository.save(terminal);
    }

    public Terminal updateTerminal(Long id, Terminal terminalDetails) {
        Terminal terminal = terminalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terminal not found"));
        terminal.setName(terminalDetails.getName());
        terminal.setCode(terminalDetails.getCode());
        terminal.setStore(terminalDetails.getStore());
        terminal.setActive(terminalDetails.isActive());
        terminal.setUpdatedAt(LocalDateTime.now());
        return terminalRepository.save(terminal);
    }

    public void deactivateTerminal(Long id) {
        Terminal terminal = terminalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terminal not found"));
        terminal.setActive(false);
        terminal.setUpdatedAt(LocalDateTime.now());
        terminalRepository.save(terminal);
    }

    public void deleteTerminal(Long id) {
        terminalRepository.deleteById(id);
    }
} 