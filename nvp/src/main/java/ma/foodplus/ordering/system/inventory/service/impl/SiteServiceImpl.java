package ma.foodplus.ordering.system.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.inventory.dto.SiteDTO;
import ma.foodplus.ordering.system.inventory.mapper.SiteMapper;
import ma.foodplus.ordering.system.inventory.model.Site;
import ma.foodplus.ordering.system.inventory.repository.SiteRepository;
import ma.foodplus.ordering.system.inventory.service.SiteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;

    @Override
    public SiteDTO createSite(SiteDTO siteDTO) {
        Site site = siteMapper.toEntity(siteDTO);
        site = siteRepository.save(site);
        return siteMapper.toDTO(site);
    }

    @Override
    public SiteDTO updateSite(Long id, SiteDTO siteDTO) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site not found with id: " + id));
        siteMapper.updateEntityFromDTO(siteDTO, site);
        site = siteRepository.save(site);
        return siteMapper.toDTO(site);
    }

    @Override
    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SiteDTO getSiteById(Long id) {
        return siteRepository.findById(id)
                .map(siteMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Site not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public SiteDTO getSiteByCode(String siteCode) {
        return siteRepository.findBySiteCode(siteCode)
                .map(siteMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Site not found with code: " + siteCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getAllSites() {
        return siteRepository.findAll().stream()
                .map(siteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getActiveSites() {
        return siteRepository.findByIsActive(true).stream()
                .map(siteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getSitesByCity(String city) {
        return siteRepository.findActiveSitesByCity(city).stream()
                .map(siteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getSitesByCountry(String country) {
        return siteRepository.findActiveSitesByCountry(country).stream()
                .map(siteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getSitesByMinCapacity(Double minCapacity) {
        return siteRepository.findActiveSitesByMinCapacity(minCapacity).stream()
                .map(siteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> searchSites(String searchTerm) {
        return siteRepository.searchSitesByDescription(searchTerm).stream()
                .map(siteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveSites() {
        return siteRepository.countActiveSites();
    }

    @Override
    public void activateSite(Long id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site not found with id: " + id));
        site.setActive(true);
        siteRepository.save(site);
    }

    @Override
    public void deactivateSite(Long id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site not found with id: " + id));
        site.setActive(false);
        siteRepository.save(site);
    }
} 