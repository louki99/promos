package ma.foodplus.ordering.system.category.tariff.service.impl;

import ma.foodplus.ordering.system.category.tariff.domain.CategoryTarif;
import ma.foodplus.ordering.system.category.tariff.repository.CategoryTarifRepository;
import ma.foodplus.ordering.system.category.tariff.service.CategoryTarifService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryTarifServiceImpl implements CategoryTarifService{

    private final CategoryTarifRepository categoryTarifRepository;

    public CategoryTarifServiceImpl(CategoryTarifRepository categoryTarifRepository) {
        this.categoryTarifRepository = categoryTarifRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTarif> findAll() {
        return categoryTarifRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryTarif> findById(Long id) {
        return categoryTarifRepository.findById(id);
    }

    @Override
    public CategoryTarif save(CategoryTarif categoryTarif) {
        return categoryTarifRepository.save(categoryTarif);
    }

    @Override
    public void deleteById(Long id) {
        categoryTarifRepository.deleteById(id);
    }

    @Override
    public CategoryTarif update(Long id, CategoryTarif categoryTarif) {
        return categoryTarifRepository.findById(id)
            .map(existingCategoryTarif -> {
                existingCategoryTarif.setCode(categoryTarif.getCode());
                existingCategoryTarif.setName(categoryTarif.getName());
                existingCategoryTarif.setDescription(categoryTarif.getDescription());
                return categoryTarifRepository.save(existingCategoryTarif);
            })
            .orElseThrow(() -> new RuntimeException("CategoryTarif not found with id: " + id));
    }
} 