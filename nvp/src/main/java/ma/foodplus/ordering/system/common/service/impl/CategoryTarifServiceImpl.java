package ma.foodplus.ordering.system.common.service.impl;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.common.dto.CategoryTarifDTO;
import ma.foodplus.ordering.system.common.mapper.CategoryTarifMapper;
import ma.foodplus.ordering.system.common.model.CategoryTarif;
import ma.foodplus.ordering.system.common.repository.CategoryTarifRepository;
import ma.foodplus.ordering.system.common.service.CategoryTarifService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryTarifServiceImpl implements CategoryTarifService {

    private final CategoryTarifRepository categoryTarifRepository;
    private final CategoryTarifMapper categoryTarifMapper;

    @Override
    public CategoryTarifDTO createCategoryTarif(CategoryTarifDTO categoryTarifDTO) {
        if (categoryTarifRepository.existsByDescription(categoryTarifDTO.getDescription())) {
            throw new IllegalArgumentException("Category tariff with this description already exists");
        }
        CategoryTarif categoryTarif = categoryTarifMapper.toEntity(categoryTarifDTO);
        categoryTarif = categoryTarifRepository.save(categoryTarif);
        return categoryTarifMapper.toDTO(categoryTarif);
    }

    @Override
    public CategoryTarifDTO updateCategoryTarif(Long id, CategoryTarifDTO categoryTarifDTO) {
        CategoryTarif categoryTarif = categoryTarifRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category tariff not found with id: " + id));
        
        categoryTarifMapper.updateEntityFromDTO(categoryTarifDTO, categoryTarif);
        categoryTarif = categoryTarifRepository.save(categoryTarif);
        return categoryTarifMapper.toDTO(categoryTarif);
    }

    @Override
    public void deleteCategoryTarif(Long id) {
        if (!categoryTarifRepository.existsById(id)) {
            throw new EntityNotFoundException("Category tariff not found with id: " + id);
        }
        categoryTarifRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryTarifDTO getCategoryTarifById(Long id) {
        return categoryTarifRepository.findById(id)
                .map(categoryTarifMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Category tariff not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTarifDTO> getAllCategoryTarifs() {
        return categoryTarifRepository.findAll().stream()
                .map(categoryTarifMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryTarifDTO getCategoryTarifByDescription(String description) {
        return categoryTarifRepository.findByDescription(description)
                .map(categoryTarifMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Category tariff not found with description: " + description));
    }
} 