package ma.foodplus.ordering.system.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import ma.foodplus.ordering.system.customer.exception.CustomerNotFoundException;
import ma.foodplus.ordering.system.customer.mapper.CustomerMapper;
import ma.foodplus.ordering.system.customer.model.Customer;
import ma.foodplus.ordering.system.customer.model.CustomerGroup;
import ma.foodplus.ordering.system.customer.model.CustomerType;
import ma.foodplus.ordering.system.customer.repository.CustomerGroupRepository;
import ma.foodplus.ordering.system.customer.repository.CustomerRepository;
import ma.foodplus.ordering.system.customer.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.info("Creating new customer: {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer.setCreatedAt(ZonedDateTime.now());
        customer.setActive(true);
        customer = customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        log.info("Updating customer with id {}: {}", id, customerDTO);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        
        customerMapper.updateEntityFromDTO(customerDTO, customer);
        customer.setUpdatedAt(ZonedDateTime.now());
        customer = customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        log.info("Getting customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        log.info("Getting all customers");
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> searchCustomers(String searchTerm, Pageable pageable) {
        log.info("Searching customers with term: {}", searchTerm);
        return customerRepository.searchCustomers(searchTerm, pageable)
                .map(customerMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByType(CustomerType type) {
        log.info("Getting customers by type: {}", type);
        return customerRepository.findByCustomerType(type).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByGroup(Long groupId) {
        log.info("Getting customers by group id: {}", groupId);
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer group not found with id: " + groupId));
        return group.getCustomers().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getVipCustomers() {
        log.info("Getting VIP customers");
        return customerRepository.findByIsVipTrue().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByCreditRating(String creditRating) {
        log.info("Getting customers by credit rating: {}", creditRating);
        return customerRepository.findByCreditRating(creditRating).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersWithExpiringContracts(int daysThreshold) {
        log.info("Getting customers with contracts expiring within {} days", daysThreshold);
        ZonedDateTime thresholdDate = ZonedDateTime.now().plusDays(daysThreshold);
        return customerRepository.findByContractEndDateBefore(thresholdDate).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersWithOverduePayments() {
        log.info("Getting customers with overdue payments");
        return customerRepository.findByOutstandingBalanceGreaterThan(BigDecimal.ZERO).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByAnnualTurnoverRange(BigDecimal min, BigDecimal max) {
        log.info("Getting customers by annual turnover range: {} - {}", min, max);
        return customerRepository.findByAnnualTurnoverBetween(min, max).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByBusinessActivity(String activity) {
        log.info("Getting customers by business activity: {}", activity);
        return customerRepository.findByBusinessActivityContainingIgnoreCase(activity).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateCustomerCreditLimit(Long id, BigDecimal newLimit) {
        log.info("Updating credit limit for customer {} to {}", id, newLimit);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setCreditLimit(newLimit);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomerCreditScore(Long id, Integer newScore) {
        log.info("Updating credit score for customer {} to {}", id, newScore);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setCreditScore(newScore);
        customerRepository.save(customer);
    }

    @Override
    public void addCustomerToGroup(Long customerId, Long groupId) {
        log.info("Adding customer {} to group {}", customerId, groupId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer group not found with id: " + groupId));
        
        customer.getCustomerGroups().add(group);
        customerRepository.save(customer);
    }

    @Override
    public void removeCustomerFromGroup(Long customerId, Long groupId) {
        log.info("Removing customer {} from group {}", customerId, groupId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        CustomerGroup group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer group not found with id: " + groupId));
        
        customer.getCustomerGroups().remove(group);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomerLoyaltyPoints(Long id, Integer points) {
        log.info("Updating loyalty points for customer {} to {}", id, points);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setLoyaltyPoints(points);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomerVipStatus(Long id, boolean isVip) {
        log.info("Updating VIP status for customer {} to {}", id, isVip);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setVip(isVip);
        customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCustomerContract(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return customer.hasValidContract();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCustomerCredit(Long id, BigDecimal amount) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return customer.getCreditLimit().compareTo(customer.getOutstandingBalance().add(amount)) >= 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCustomerActive(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return customer.isActive();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCustomerStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Total customers
        statistics.put("totalCustomers", customerRepository.count());
        
        // Customers by type
        statistics.put("b2bCustomers", customerRepository.countByCustomerType(CustomerType.B2B));
        statistics.put("b2cCustomers", customerRepository.countByCustomerType(CustomerType.B2C));
        
        // VIP customers
        statistics.put("vipCustomers", customerRepository.countByIsVipTrue());
        
        // Active customers
        statistics.put("activeCustomers", customerRepository.countByActiveTrue());
        
        // Average order value
        BigDecimal avgOrderValue = customerRepository.findAll().stream()
                .map(Customer::getAverageOrderValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(customerRepository.count()), 2, RoundingMode.HALF_UP);
        statistics.put("averageOrderValue", avgOrderValue);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getTopCustomersBySpending(int limit) {
        log.info("Getting top {} customers by spending", limit);
        return customerRepository.findTopByOrderByTotalSpentDesc(limit).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByProductPreference(Long productId) {
        log.info("Getting customers by product preference: {}", productId);
        return customerRepository.findByProductPreferences(productId).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getCustomerDistributionByType() {
        Map<String, Integer> distribution = new HashMap<>();
        distribution.put("B2B", (int) customerRepository.countByCustomerType(CustomerType.B2B));
        distribution.put("B2C", (int) customerRepository.countByCustomerType(CustomerType.B2C));
        return distribution;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getAverageOrderValueByCustomerType() {
        Map<String, BigDecimal> averages = new HashMap<>();
        
        BigDecimal b2bAvg = customerRepository.findByCustomerType(CustomerType.B2B).stream()
                .map(Customer::getAverageOrderValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(customerRepository.countByCustomerType(CustomerType.B2B)), 2, RoundingMode.HALF_UP);
        
        BigDecimal b2cAvg = customerRepository.findByCustomerType(CustomerType.B2C).stream()
                .map(Customer::getAverageOrderValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(customerRepository.countByCustomerType(CustomerType.B2C)), 2, RoundingMode.HALF_UP);
        
        averages.put("B2B", b2bAvg);
        averages.put("B2C", b2cAvg);
        
        return averages;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByCtNum(String ctNum) {
        log.info("Getting customer by CT number: {}", ctNum);
        Customer customer = customerRepository.findByCtNum(ctNum)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with CT number: " + ctNum));
        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByIce(String ice) {
        log.info("Getting customer by ICE: {}", ice);
        Customer customer = customerRepository.findByIce(ice)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ICE: " + ice));
        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllActiveCustomers() {
        log.info("Getting all active customers");
        return customerRepository.findByActiveTrue().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByCategoryTarif(Long categoryTarifId) {
        log.info("Getting customers by category tariff ID: {}", categoryTarifId);
        return customerRepository.findByCategoryTarifId(categoryTarifId).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomersByDescription(String searchTerm) {
        log.info("Searching customers by description: {}", searchTerm);
        return customerRepository.searchCustomers(searchTerm, Pageable.unpaged()).stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void activateCustomer(Long id) {
        log.info("Activating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setActive(true);
        customerRepository.save(customer);
    }

    @Override
    public void deactivateCustomer(Long id) {
        log.info("Deactivating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setActive(false);
        customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCustomerLoyaltyLevel(String entityId) {
        log.info("Getting customer loyalty level for entity ID: {}", entityId);
        Customer customer = customerRepository.findByCtNum(entityId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with entity ID: " + entityId));
        return calculateLoyaltyLevel(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCustomerPurchaseHistory(String entityId) {
        log.info("Getting customer purchase history for entity ID: {}", entityId);
        Customer customer = customerRepository.findByCtNum(entityId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with entity ID: " + entityId));
        
        Map<String, Object> history = new HashMap<>();
        history.put("totalOrders", customer.getTotalOrders());
        history.put("totalSpent", customer.getTotalSpent());
        history.put("averageOrderValue", customer.getAverageOrderValue());
        history.put("lastOrderDate", customer.getLastOrderDate());
        return history;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getCustomerGroups(String entityId) {
        log.info("Getting customer groups for entity ID: {}", entityId);
        Customer customer = customerRepository.findByCtNum(entityId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with entity ID: " + entityId));
        return customer.getCustomerGroups().stream()
                .map(CustomerGroup::getName)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCustomerInGroup(Long customerId, Long groupId) {
        log.info("Checking if customer {} is in group {}", customerId, groupId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        return customer.getCustomerGroups().stream()
                .anyMatch(group -> group.getId().equals(groupId));
    }

    @Override
    @Transactional(readOnly = true)
    public int getCustomerLoyaltyLevel(Long customerId) {
        log.info("Getting customer loyalty level for ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        return calculateLoyaltyLevel(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCustomerTotalSpent(Long customerId) {
        log.info("Getting total spent for customer ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        return customer.getTotalSpent();
    }

    @Override
    public CustomerDTO updateLoyaltyPoints(Long id, int points) {
        log.info("Updating loyalty points for customer {} by {}", id, points);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        customer = customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    @Override
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toDTO);
    }

    private int calculateLoyaltyLevel(Customer customer) {
        if (customer.getTotalSpent() == null || customer.getTotalSpent().compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        BigDecimal totalSpent = customer.getTotalSpent();
        if (totalSpent.compareTo(new BigDecimal("100000")) >= 0) {
            return 5;
        } else if (totalSpent.compareTo(new BigDecimal("50000")) >= 0) {
            return 4;
        } else if (totalSpent.compareTo(new BigDecimal("25000")) >= 0) {
            return 3;
        } else if (totalSpent.compareTo(new BigDecimal("10000")) >= 0) {
            return 2;
        } else if (totalSpent.compareTo(new BigDecimal("5000")) >= 0) {
            return 1;
        }
        return 0;
    }
} 