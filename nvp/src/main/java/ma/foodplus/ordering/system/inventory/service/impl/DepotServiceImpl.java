package ma.foodplus.ordering.system.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.inventory.dto.DepotDTO;
import ma.foodplus.ordering.system.inventory.mapper.DepotMapper;
import ma.foodplus.ordering.system.inventory.model.Depot;
import ma.foodplus.ordering.system.inventory.model.Depot.DepotType;
import ma.foodplus.ordering.system.inventory.repository.DepotRepository;
import ma.foodplus.ordering.system.inventory.service.DepotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepotServiceImpl implements DepotService {

    private final DepotRepository depotRepository;
    private final DepotMapper depotMapper;

    @Override
    public DepotDTO createDepot(DepotDTO depotDTO) {
        Depot depot = depotMapper.toEntity(depotDTO);
        depot = depotRepository.save(depot);
        return depotMapper.toDTO(depot);
    }

    @Override
    public DepotDTO updateDepot(Long id, DepotDTO depotDTO) {
        Depot depot = depotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depot not found with id: " + id));
        depotMapper.updateEntityFromDTO(depotDTO, depot);
        depot = depotRepository.save(depot);
        return depotMapper.toDTO(depot);
    }

    @Override
    public void deleteDepot(Long id) {
        depotRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DepotDTO getDepotById(Long id) {
        return depotRepository.findById(id)
                .map(depotMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Depot not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public DepotDTO getDepotByCode(String depotCode) {
        return depotRepository.findByDepotCode(depotCode)
                .map(depotMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Depot not found with code: " + depotCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepotDTO> getAllDepots() {
        return depotRepository.findAll().stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepotDTO> getActiveDepots() {
        return depotRepository.findByIsActive(true).stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepotDTO> getDepotsByType(DepotType depotType) {
        return depotRepository.findByDepotType(depotType).stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepotDTO> getDepotsBySiteId(Long siteId) {
        return depotRepository.findActiveDepotsBySiteId(siteId).stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepotDTO> getRefrigeratedDepots() {
        return depotRepository.findActiveRefrigeratedDepots().stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepotDTO> getDepotsByMinCapacity(Double minCapacity) {
        return depotRepository.findActiveDepotsByMinCapacity(minCapacity).stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepotDTO> getDepotsByMinSecurityLevel(Integer minSecurityLevel) {
        return depotRepository.findActiveDepotsByMinSecurityLevel(minSecurityLevel).stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepotDTO> searchDepots(String searchTerm) {
        return depotRepository.searchDepotsByDescription(searchTerm).stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveDepots() {
        return depotRepository.countActiveDepots();
    }

    @Override
    @Transactional(readOnly = true)
    public long countDepotsByType(DepotType depotType) {
        return depotRepository.countActiveDepotsByType(depotType);
    }

    @Override
    public void activateDepot(Long id) {
        Depot depot = depotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depot not found with id: " + id));
        depot.setActive(true);
        depotRepository.save(depot);
    }

    @Override
    public void deactivateDepot(Long id) {
        Depot depot = depotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depot not found with id: " + id));
        depot.setActive(false);
        depotRepository.save(depot);
    }
} 