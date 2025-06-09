package ma.foodplus.ordering.system.inventory.service;

import ma.foodplus.ordering.system.inventory.dto.DepotDTO;
import ma.foodplus.ordering.system.inventory.model.Depot.DepotType;
import java.util.List;

public interface DepotService {
    DepotDTO createDepot(DepotDTO depotDTO);
    DepotDTO updateDepot(Long id, DepotDTO depotDTO);
    void deleteDepot(Long id);
    DepotDTO getDepotById(Long id);
    DepotDTO getDepotByCode(String depotCode);
    List<DepotDTO> getAllDepots();
    List<DepotDTO> getActiveDepots();
    List<DepotDTO> getDepotsByType(DepotType depotType);
    List<DepotDTO> getDepotsBySiteId(Long siteId);
    List<DepotDTO> getRefrigeratedDepots();
    List<DepotDTO> getDepotsByMinCapacity(Double minCapacity);
    List<DepotDTO> getDepotsByMinSecurityLevel(Integer minSecurityLevel);
    List<DepotDTO> searchDepots(String searchTerm);
    long countActiveDepots();
    long countDepotsByType(DepotType depotType);
    void activateDepot(Long id);
    void deactivateDepot(Long id);
} 