package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.Shift;
import ma.foodplus.ordering.system.pos.enums.ShiftStatus;
import ma.foodplus.ordering.system.pos.service.ShiftService;
import ma.foodplus.ordering.system.pos.dto.ShiftOpenRequest;
import ma.foodplus.ordering.system.pos.dto.ShiftCloseRequest;
import ma.foodplus.ordering.system.pos.dto.ShiftResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@Tag(name = "Shift Management", description = "Endpoints for managing cashier shifts and sessions.")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    @GetMapping
    @Operation(summary = "Get all shifts", description = "Retrieve a list of all shifts.")
    public List<Shift> getAllShifts() {
        return shiftService.getAllShifts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shift by ID", description = "Retrieve a shift by its unique ID.")
    public ResponseEntity<Shift> getShiftById(@PathVariable Long id) {
        return shiftService.getShiftById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/terminal/{terminalId}")
    @Operation(summary = "Get shifts by terminal", description = "Retrieve all shifts for a specific terminal.")
    public List<Shift> getShiftsByTerminal(@PathVariable Long terminalId) {
        return shiftService.getShiftsByTerminal(terminalId);
    }

    @GetMapping("/cashier/{cashierId}")
    @Operation(summary = "Get shifts by cashier", description = "Retrieve all shifts for a specific cashier.")
    public List<Shift> getShiftsByCashier(@PathVariable Long cashierId) {
        return shiftService.getShiftsByCashier(cashierId);
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get shifts by store", description = "Retrieve all shifts for a specific store.")
    public List<Shift> getShiftsByStore(@PathVariable Long storeId) {
        return shiftService.getShiftsByStore(storeId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get shifts by status", description = "Retrieve all shifts with a specific status.")
    public List<Shift> getShiftsByStatus(@PathVariable ShiftStatus status) {
        return shiftService.getShiftsByStatus(status);
    }

    @GetMapping("/period")
    @Operation(summary = "Get shifts by period", description = "Retrieve all shifts within a specific time period.")
    public List<Shift> getShiftsByPeriod(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return shiftService.getShiftsByPeriod(start, end);
    }

    @PostMapping
    @Operation(summary = "Create shift", description = "Create a new shift.")
    public Shift createShift(@RequestBody Shift shift) {
        return shiftService.createShift(shift);
    }

    @PostMapping("/open")
    @Operation(summary = "Open shift", description = "Open a new shift for a cashier at a terminal.")
    public ShiftResponse openShift(@RequestBody ShiftOpenRequest request) {
        return shiftService.openShift(request);
    }

    @PutMapping("/{id}/close")
    @Operation(summary = "Close shift", description = "Close an active shift by ID.")
    public ShiftResponse closeShift(@PathVariable Long id, @RequestBody ShiftCloseRequest request) {
        return shiftService.closeShift(id, request);
    }

    @GetMapping("/active")
    @Operation(summary = "Get current active shift", description = "Get the currently active shift for a cashier and terminal.")
    public ResponseEntity<ShiftResponse> getCurrentActiveShift(@RequestParam Long terminalId, @RequestParam Long cashierId) {
        ShiftResponse shift = shiftService.getCurrentActiveShift(terminalId, cashierId);
        if (shift == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(shift);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shift", description = "Delete a shift by its ID.")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }
} 