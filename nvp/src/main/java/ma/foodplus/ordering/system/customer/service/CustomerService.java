package ma.foodplus.ordering.system.customer.service;

import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    
    /**
     * Get a customer by ID
     * @param id the customer ID
     * @return the customer DTO
     */
    CustomerDTO getCustomerById(Long id);

    /**
     * Get all customers
     * @return list of all customers
     */
    List<CustomerDTO> getAllCustomers();

    /**
     * Create a new customer
     * @param customerDTO the customer to create
     * @return the created customer
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    /**
     * Update an existing customer
     * @param id the customer ID
     * @param customerDTO the updated customer data
     * @return the updated customer
     */
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    /**
     * Delete a customer
     * @param id the customer ID
     */
    void deleteCustomer(Long id);

    /**
     * Get customers by product preference
     * @param productId the product ID
     * @return list of customers who prefer this product
     */
    List<CustomerDTO> getCustomersByProductPreference(Long productId);

    /**
     * Get VIP customers
     * @return list of VIP customers
     */
    List<CustomerDTO> getVipCustomers();

    /**
     * Update customer loyalty points
     * @param id the customer ID
     * @param points the points to add/remove
     * @return the updated customer
     */
    CustomerDTO updateLoyaltyPoints(Long id, int points);

    CustomerDTO getCustomerByCtNum(String ctNum);
    CustomerDTO getCustomerByIce(String ice);
    List<CustomerDTO> getAllActiveCustomers();
    List<CustomerDTO> getCustomersByCategoryTarif(Long categoryTarifId);
    List<CustomerDTO> searchCustomersByDescription(String searchTerm);
    void activateCustomer(Long id);
    void deactivateCustomer(Long id);
} 