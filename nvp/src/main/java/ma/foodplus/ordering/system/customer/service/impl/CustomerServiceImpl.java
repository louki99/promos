package ma.foodplus.ordering.system.customer.service.impl;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import ma.foodplus.ordering.system.customer.mapper.CustomerMapper;
import ma.foodplus.ordering.system.customer.model.Customer;
import ma.foodplus.ordering.system.customer.repository.CustomerRepository;
import ma.foodplus.ordering.system.customer.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByCtNum(customerDTO.getCtNum())) {
            throw new IllegalArgumentException("Customer with CT number " + customerDTO.getCtNum() + " already exists");
        }
        if (customerRepository.existsByIce(customerDTO.getIce())) {
            throw new IllegalArgumentException("Customer with ICE " + customerDTO.getIce() + " already exists");
        }
        
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        
        // Check if CT number is being changed and if it's already taken
        if (!customer.getCtNum().equals(customerDTO.getCtNum()) && 
            customerRepository.existsByCtNum(customerDTO.getCtNum())) {
            throw new IllegalArgumentException("Customer with CT number " + customerDTO.getCtNum() + " already exists");
        }
        
        // Check if ICE is being changed and if it's already taken
        if (!customer.getIce().equals(customerDTO.getIce()) && 
            customerRepository.existsByIce(customerDTO.getIce())) {
            throw new IllegalArgumentException("Customer with ICE " + customerDTO.getIce() + " already exists");
        }
        
        customerMapper.updateEntityFromDTO(customerDTO, customer);
        customer = customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByCtNum(String ctNum) {
        return customerRepository.findByCtNum(ctNum)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with CT number: " + ctNum));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByIce(String ice) {
        return customerRepository.findByIce(ice)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ICE: " + ice));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllActiveCustomers() {
        return customerRepository.findAllActive().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByCategoryTarif(Long categoryTarifId) {
        return customerRepository.findByCategoryTarifId(categoryTarifId).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomersByDescription(String searchTerm) {
        return customerRepository.searchByDescription(searchTerm).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        customer.setActive(true);
        customerRepository.save(customer);
    }

    @Override
    public void deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        customer.setActive(false);
        customerRepository.save(customer);
    }
} 