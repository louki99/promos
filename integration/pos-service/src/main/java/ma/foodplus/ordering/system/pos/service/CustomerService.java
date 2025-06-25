package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Customer;
import ma.foodplus.ordering.system.pos.enums.CustomerType;
import ma.foodplus.ordering.system.pos.enums.LoyaltyTier;
import ma.foodplus.ordering.system.pos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getActiveCustomers() {
        return customerRepository.findByActiveTrue();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Customer> getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    public List<Customer> getCustomersByType(CustomerType type) {
        return customerRepository.findByCustomerType(type);
    }

    public List<Customer> getCustomersByLoyaltyTier(LoyaltyTier tier) {
        return customerRepository.findByLoyaltyTier(tier);
    }

    public Page<Customer> searchCustomers(String search, Pageable pageable) {
        return customerRepository.searchActiveCustomers(search, pageable);
    }

    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhone(customerDetails.getPhone());
        customer.setAddress(customerDetails.getAddress());
        customer.setCity(customerDetails.getCity());
        customer.setCustomerType(customerDetails.getCustomerType());
        customer.setActive(customerDetails.isActive());
        customer.setUpdatedAt(LocalDateTime.now());

        return customerRepository.save(customer);
    }

    public void addLoyaltyPoints(Long customerId, Integer points) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.addLoyaltyPoints(points);
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void deductLoyaltyPoints(Long customerId, Integer points) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        int newPoints = Math.max(0, customer.getLoyaltyPoints() - points);
        customer.setLoyaltyPoints(newPoints);
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setActive(false);
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
