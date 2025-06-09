package ma.foodplus.ordering.system.inventory.service;

import ma.foodplus.ordering.system.inventory.dto.SiteDTO;
import java.util.List;

public interface SiteService {
    SiteDTO createSite(SiteDTO siteDTO);
    SiteDTO updateSite(Long id, SiteDTO siteDTO);
    void deleteSite(Long id);
    SiteDTO getSiteById(Long id);
    SiteDTO getSiteByCode(String siteCode);
    List<SiteDTO> getAllSites();
    List<SiteDTO> getActiveSites();
    List<SiteDTO> getSitesByCity(String city);
    List<SiteDTO> getSitesByCountry(String country);
    List<SiteDTO> getSitesByMinCapacity(Double minCapacity);
    List<SiteDTO> searchSites(String searchTerm);
    long countActiveSites();
    void activateSite(Long id);
    void deactivateSite(Long id);
} 