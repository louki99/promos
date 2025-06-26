package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.Terminal;
import ma.foodplus.ordering.system.pos.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terminals")
public class TerminalController {
    @Autowired
    private TerminalService terminalService;

    @GetMapping
    public List<Terminal> getAllTerminals() {
        return terminalService.getAllTerminals();
    }

    @GetMapping("/active")
    public List<Terminal> getActiveTerminals() {
        return terminalService.getActiveTerminals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terminal> getTerminalById(@PathVariable Long id) {
        return terminalService.getTerminalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Terminal createTerminal(@RequestBody Terminal terminal) {
        return terminalService.createTerminal(terminal);
    }

    @PutMapping("/{id}")
    public Terminal updateTerminal(@PathVariable Long id, @RequestBody Terminal terminal) {
        return terminalService.updateTerminal(id, terminal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerminal(@PathVariable Long id) {
        terminalService.deleteTerminal(id);
        return ResponseEntity.noContent().build();
    }
} 