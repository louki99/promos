package ma.foodplus.ordering.system.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import ma.foodplus.ordering.system.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.createCustomer(customerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing customer")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by ID")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/ct-num/{ctNum}")
    @Operation(summary = "Get a customer by CT number")
    public ResponseEntity<CustomerDTO> getCustomerByCtNum(@PathVariable String ctNum) {
        return ResponseEntity.ok(customerService.getCustomerByCtNum(ctNum));
    }

    @GetMapping("/ice/{ice}")
    @Operation(summary = "Get a customer by ICE")
    public ResponseEntity<CustomerDTO> getCustomerByIce(@PathVariable String ice) {
        return ResponseEntity.ok(customerService.getCustomerByIce(ice));
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active customers")
    public ResponseEntity<List<CustomerDTO>> getAllActiveCustomers() {
        return ResponseEntity.ok(customerService.getAllActiveCustomers());
    }

    @GetMapping("/category-tarif/{categoryTarifId}")
    @Operation(summary = "Get customers by category tariff ID")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCategoryTarif(@PathVariable Long categoryTarifId) {
        return ResponseEntity.ok(customerService.getCustomersByCategoryTarif(categoryTarifId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers by description")
    public ResponseEntity<List<CustomerDTO>> searchCustomersByDescription(@RequestParam String searchTerm) {
        return ResponseEntity.ok(customerService.searchCustomersByDescription(searchTerm));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a customer")
    public ResponseEntity<Void> activateCustomer(@PathVariable Long id) {
        customerService.activateCustomer(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a customer")
    public ResponseEntity<Void> deactivateCustomer(@PathVariable Long id) {
        customerService.deactivateCustomer(id);
        return ResponseEntity.ok().build();
    }
} 