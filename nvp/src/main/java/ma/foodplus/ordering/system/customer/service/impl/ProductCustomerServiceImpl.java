package ma.foodplus.ordering.system.customer.service.impl;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerDTO;
import ma.foodplus.ordering.system.customer.mapper.ProductCustomerMapper;
import ma.foodplus.ordering.system.customer.model.ProductCustomer;
import ma.foodplus.ordering.system.customer.repository.ProductCustomerRepository;
import ma.foodplus.ordering.system.customer.service.ProductCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCustomerServiceImpl implements ProductCustomerService {

    private final ProductCustomerRepository productCustomerRepository;
    private final ProductCustomerMapper productCustomerMapper;

    @Override
    public ProductCustomerDTO createProductCustomer(ProductCustomerDTO productCustomerDTO) {
        if (productCustomerRepository.findByCustomerIdAndProductId(
                productCustomerDTO.getCustomerId(), 
                productCustomerDTO.getProductId()).isPresent()) {
            throw new IllegalArgumentException("Product customer relationship already exists");
        }
        
        ProductCustomer productCustomer = productCustomerMapper.toEntity(productCustomerDTO);
        productCustomer = productCustomerRepository.save(productCustomer);
        return productCustomerMapper.toDTO(productCustomer);
    }

    @Override
    public ProductCustomerDTO updateProductCustomer(Long id, ProductCustomerDTO productCustomerDTO) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        
        productCustomerMapper.updateEntityFromDTO(productCustomerDTO, productCustomer);
        productCustomer = productCustomerRepository.save(productCustomer);
        return productCustomerMapper.toDTO(productCustomer);
    }

    @Override
    public void deleteProductCustomer(Long id) {
        if (!productCustomerRepository.existsById(id)) {
            throw new EntityNotFoundException("Product customer not found with id: " + id);
        }
        productCustomerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCustomerDTO getProductCustomerById(Long id) {
        return productCustomerRepository.findById(id)
                .map(productCustomerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDTO> getProductCustomersByCustomerId(Long customerId) {
        return productCustomerRepository.findByCustomerId(customerId).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDTO> getProductCustomersByProductId(Long productId) {
        return productCustomerRepository.findByProductId(productId).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCustomerDTO getProductCustomerByCustomerIdAndProductId(Long customerId, Long productId) {
        return productCustomerRepository.findByCustomerIdAndProductId(customerId, productId)
                .map(productCustomerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Product customer not found for customer: " + customerId + " and product: " + productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDTO> getProductCustomersByCustomerIdAndCategory(Long customerId, String category) {
        return productCustomerRepository.findByCustomerIdAndCategory(customerId, category).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDTO> getProductCustomersByCustomerIdAndMaxPrice(Long customerId, Double maxPrice) {
        return productCustomerRepository.findByCustomerIdAndMaxPrice(customerId, maxPrice).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProductCustomerPrice(Long id, Double newPrice) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        
        productCustomer.setPrixTTC(BigDecimal.valueOf(newPrice));
        productCustomerRepository.save(productCustomer);
    }

    @Override
    public void updateProductCustomerDiscount(Long id, Double newDiscount) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        
        productCustomer.setRemise(BigDecimal.valueOf(newDiscount));
        productCustomerRepository.save(productCustomer);
    }
} 