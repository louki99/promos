package ma.foodplus.ordering.system.customer.service;

import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    void deleteCustomer(Long id);
    CustomerDTO getCustomerById(Long id);
    CustomerDTO getCustomerByCtNum(String ctNum);
    CustomerDTO getCustomerByIce(String ice);
    List<CustomerDTO> getAllCustomers();
    List<CustomerDTO> getAllActiveCustomers();
    List<CustomerDTO> getCustomersByCategoryTarif(Long categoryTarifId);
    List<CustomerDTO> searchCustomersByDescription(String searchTerm);
    void activateCustomer(Long id);
    void deactivateCustomer(Long id);
} 