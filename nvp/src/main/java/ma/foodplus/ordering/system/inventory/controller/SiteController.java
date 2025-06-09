package ma.foodplus.ordering.system.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.inventory.dto.SiteDTO;
import ma.foodplus.ordering.system.inventory.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/sites")
@RequiredArgsConstructor
@Tag(name = "Site Management", description = "APIs for managing inventory sites")
public class SiteController extends InventoryBaseController {

    private final SiteService siteService;

    @PostMapping
    @Operation(summary = "Create a new site")
    public ResponseEntity<SiteDTO> createSite(@RequestBody SiteDTO siteDTO) {
        return new ResponseEntity<>(siteService.createSite(siteDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing site")
    public ResponseEntity<SiteDTO> updateSite(
            @PathVariable Long id,
            @RequestBody SiteDTO siteDTO) {
        return ResponseEntity.ok(siteService.updateSite(id, siteDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a site")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSite(@PathVariable Long id) {
        siteService.deleteSite(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a site by ID")
    public ResponseEntity<SiteDTO> getSiteById(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.getSiteById(id));
    }

    @GetMapping("/code/{siteCode}")
    @Operation(summary = "Get a site by code")
    public ResponseEntity<SiteDTO> getSiteByCode(@PathVariable String siteCode) {
        return ResponseEntity.ok(siteService.getSiteByCode(siteCode));
    }

    @GetMapping
    @Operation(summary = "Get all sites")
    public ResponseEntity<List<SiteDTO>> getAllSites() {
        return ResponseEntity.ok(siteService.getAllSites());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active sites")
    public ResponseEntity<List<SiteDTO>> getActiveSites() {
        return ResponseEntity.ok(siteService.getActiveSites());
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get sites by city")
    public ResponseEntity<List<SiteDTO>> getSitesByCity(@PathVariable String city) {
        return ResponseEntity.ok(siteService.getSitesByCity(city));
    }

    @GetMapping("/country/{country}")
    @Operation(summary = "Get sites by country")
    public ResponseEntity<List<SiteDTO>> getSitesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(siteService.getSitesByCountry(country));
    }

    @GetMapping("/capacity")
    @Operation(summary = "Get sites by minimum capacity")
    public ResponseEntity<List<SiteDTO>> getSitesByMinCapacity(@RequestParam Double minCapacity) {
        return ResponseEntity.ok(siteService.getSitesByMinCapacity(minCapacity));
    }

    @GetMapping("/search")
    @Operation(summary = "Search sites")
    public ResponseEntity<List<SiteDTO>> searchSites(@RequestParam String searchTerm) {
        return ResponseEntity.ok(siteService.searchSites(searchTerm));
    }

    @GetMapping("/stats/active/count")
    @Operation(summary = "Count active sites")
    public ResponseEntity<Long> countActiveSites() {
        return ResponseEntity.ok(siteService.countActiveSites());
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a site")
    public ResponseEntity<Void> activateSite(@PathVariable Long id) {
        siteService.activateSite(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a site")
    public ResponseEntity<Void> deactivateSite(@PathVariable Long id) {
        siteService.deactivateSite(id);
        return ResponseEntity.ok().build();
    }
}