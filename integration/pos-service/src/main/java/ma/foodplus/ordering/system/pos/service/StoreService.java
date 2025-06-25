package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Store;
import ma.foodplus.ordering.system.pos.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public List<Store> getActiveStores() {
        return storeRepository.findByActiveTrue();
    }

    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }

    public Optional<Store> getStoreByCode(String code) {
        return storeRepository.findByCode(code);
    }

    public List<Store> getStoresByCity(String city) {
        return storeRepository.findByCityIgnoreCase(city);
    }

    public Store createStore(Store store) {
        if (storeRepository.existsByCode(store.getCode())) {
            throw new RuntimeException("Store code already exists");
        }
        store.setCreatedAt(LocalDateTime.now());
        store.setUpdatedAt(LocalDateTime.now());
        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store storeDetails) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        store.setName(storeDetails.getName());
        store.setAddress(storeDetails.getAddress());
        store.setCity(storeDetails.getCity());
        store.setPhone(storeDetails.getPhone());
        store.setEmail(storeDetails.getEmail());
        store.setTaxNumber(storeDetails.getTaxNumber());
        store.setActive(storeDetails.isActive());
        store.setUpdatedAt(LocalDateTime.now());

        return storeRepository.save(store);
    }

    public void deactivateStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        store.setActive(false);
        store.setUpdatedAt(LocalDateTime.now());
        storeRepository.save(store);
    }

    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }
}
