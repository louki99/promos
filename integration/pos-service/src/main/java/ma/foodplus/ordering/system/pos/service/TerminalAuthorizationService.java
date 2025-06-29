package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Terminal;
import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.repository.TerminalRepository;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TerminalAuthorizationService {

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Validate if a user is authorized to use a specific terminal
     */
    public boolean isUserAuthorizedForTerminal(Long userId, Long terminalId) {
        // Get user and terminal
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Terminal> terminalOpt = terminalRepository.findById(terminalId);

        if (userOpt.isEmpty() || terminalOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        Terminal terminal = terminalOpt.get();

        // Check if user is active
        if (!user.isActive()) {
            return false;
        }

        // Check if terminal is active
        if (!terminal.isActive()) {
            return false;
        }

        // For now, allow all CASHIER users to use any terminal in their store
        // This can be enhanced with TerminalAssignment validation later
        if (user.getStore() != null && terminal.getStore() != null) {
            return user.getStore().getId().equals(terminal.getStore().getId());
        }

        return false;
    }

    /**
     * Validate terminal authorization during login
     */
    public void validateTerminalAuthorization(String username, Long terminalId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!isUserAuthorizedForTerminal(user.getId(), terminalId)) {
            throw new RuntimeException("User is not authorized to use this terminal");
        }
    }

    /**
     * Get terminal by ID with validation
     */
    public Terminal getValidTerminal(Long terminalId) {
        Terminal terminal = terminalRepository.findById(terminalId)
                .orElseThrow(() -> new RuntimeException("Terminal not found"));

        if (!terminal.isActive()) {
            throw new RuntimeException("Terminal is not active");
        }

        return terminal;
    }

    /**
     * Check if terminal belongs to store
     */
    public boolean isTerminalInStore(Long terminalId, Long storeId) {
        Optional<Terminal> terminalOpt = terminalRepository.findById(terminalId);
        return terminalOpt.isPresent() && 
               terminalOpt.get().getStore() != null && 
               terminalOpt.get().getStore().getId().equals(storeId);
    }
} 