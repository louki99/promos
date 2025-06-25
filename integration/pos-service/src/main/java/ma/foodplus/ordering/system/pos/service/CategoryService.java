package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Category;
import ma.foodplus.ordering.system.pos.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Category> getActiveCategories(){
        return categoryRepository.findByActiveTrue();
    }

    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByCode(String code){
        return categoryRepository.findByCode(code);
    }

    public List<Category> getParentCategories(){
        return categoryRepository.findActiveParentCategories();
    }
}