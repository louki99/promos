package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Inventory;
import ma.foodplus.ordering.system.pos.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Optional<Inventory> getInventoryByProductAndStore(Long productId, Long storeId) {
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId);
    }

    public List<Inventory> getInventoryByStore(Long storeId) {
        return inventoryRepository.findByStoreId(storeId);
    }

    public List<Inventory> getInventoryByProduct(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public List<Inventory> getLowStockItems(Long storeId) {
        return inventoryRepository.findLowStockItems(storeId);
    }

    public List<Inventory> getOverStockItems(Long storeId) {
        return inventoryRepository.findOverStockItems(storeId);
    }

    public List<Inventory> getAvailableInventory(Long storeId) {
        return inventoryRepository.findAvailableInventoryByStore(storeId);
    }

    public Inventory createOrUpdateInventory(Inventory inventory) {
        Optional<Inventory> existing = inventoryRepository
                .findByProductIdAndStoreId(inventory.getProduct().getId(), inventory.getStore().getId());

        if (existing.isPresent()) {
            Inventory existingInventory = existing.get();
            existingInventory.setQuantity(inventory.getQuantity());
            existingInventory.setLastUpdated(LocalDateTime.now());
            return inventoryRepository.save(existingInventory);
        } else {
            inventory.setCreatedAt(LocalDateTime.now());
            inventory.setLastUpdated(LocalDateTime.now());
            return inventoryRepository.save(inventory);
        }
    }

    public void addStock(Long productId, Long storeId, Integer quantity) {
        Optional<Inventory> inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);

        if (inventory.isPresent()) {
            inventory.get().adjustQuantity(quantity);
            inventoryRepository.save(inventory.get());
        } else {
            throw new RuntimeException("Inventory record not found");
        }
    }

    public void reduceStock(Long productId, Long storeId, Integer quantity) {
        Optional<Inventory> inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);

        if (inventory.isPresent()) {
            Inventory inv = inventory.get();
            if (inv.getAvailableQuantity() >= quantity) {
                inv.adjustQuantity(-quantity);
                inventoryRepository.save(inv);
            } else {
                throw new RuntimeException("Insufficient stock available");
            }
        } else {
            throw new RuntimeException("Inventory record not found");
        }
    }

    public void setStockLevel(Long productId, Long storeId, Integer newQuantity) {
        Optional<Inventory> inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);

        if (inventory.isPresent()) {
            inventory.get().setQuantity(newQuantity);
            inventoryRepository.save(inventory.get());
        } else {
            throw new RuntimeException("Inventory record not found");
        }
    }

    public Integer getAvailableStock(Long productId, Long storeId) {
        Optional<Inventory> inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
        return inventory.map(Inventory::getAvailableQuantity).orElse(0);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public List<Inventory> getInventoriesByProduct(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public List<Inventory> getInventoriesByStore(Long storeId) {
        return inventoryRepository.findByStoreId(storeId);
    }

    public Inventory createInventory(Inventory inventory) {
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setLastUpdated(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventory.setProduct(inventoryDetails.getProduct());
        inventory.setStore(inventoryDetails.getStore());
        inventory.setQuantity(inventoryDetails.getQuantity());
        inventory.setReservedQuantity(inventoryDetails.getReservedQuantity());
        inventory.setLastUpdated(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }
}
