package org.bonbloc.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.PurchaseOrder;
import org.bonbloc.entity.Product;
import org.bonbloc.entity.ProductStorageLocation;
import org.bonbloc.entity.StorageLocation;
import org.bonbloc.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PurchaseOrderService {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);
    @Inject
    PurchaseOrderRepository purchaseOrderRepository;
    @Inject
    ProductService productService;

    @Inject
    StorageLocationService storageLocationService;

    @Inject
    ProductStorageLocationService productStorageLocationService;


    public List<PurchaseOrder> getAllInventoryTransaction() {
        logger.trace("Entering getAllInventoryTransaction()");
        return purchaseOrderRepository.listAll();
    }


    public PurchaseOrder getInventoryTransactionById(Long inventoryTransactionsId) {
        logger.trace("Entering getInventoryTransactionById() with ID");
        return purchaseOrderRepository.findById(inventoryTransactionsId);
    }


    @Transactional
    public PurchaseOrder createInventoryTransaction(PurchaseOrder inventoryTransaction) {

        logger.trace("Entering createInventoryTransaction() with inventoryTransactions");
        inventoryTransaction.setTransactionDate(Instant.now());
        inventoryTransaction.setTransactionType("IN");

        for (ProductStorageLocation productStorageLocation : inventoryTransaction.getProductStorageLocations()) {
            Product product = productService.getProductById(productStorageLocation.getProduct().getProductId());
            StorageLocation storageLocation = storageLocationService.getStorageLocationById(productStorageLocation.getStorageLocation().getStorageLocationId());

            if (product == null) {
                logger.error("PRODUCT NOT FOUND");
                throw new RuntimeException("Product not found or Create a new Product");

            }
            if (storageLocation == null) {
                logger.error("STORAGE LOCATION  NOT FOUND");
                throw new RuntimeException("Storage location not found or Create a new StorageLocation");
            }

            if (storageLocation.getCapacity() < storageLocation.getOccupiedSpace() + productStorageLocation.getQuantity() * product.getSizeOfProduct()) {
                logger.error("NOT ENOUGH SPACE TO STORE THE PRODUCT");
                throw new RuntimeException("Space not enough to store the Product");
            }

            product.setQuantityAvailable(product.getQuantityAvailable() + productStorageLocation.getQuantity());
            productService.updateProduct(product);
            logger.trace("Product updated");

            storageLocation.setOccupiedSpace(storageLocation.getOccupiedSpace() + productStorageLocation.getQuantity());
            storageLocationService.updateStorageLocation(storageLocation);
            logger.trace("Storage location updated");

            productStorageLocation.setInventoryTransaction(inventoryTransaction);
            productStorageLocationService.createProductStorageLocation(productStorageLocation);

        }

        purchaseOrderRepository.persist(inventoryTransaction);
        logger.trace("Exiting createInventoryTransaction() with created inventoryTransactions");
        return inventoryTransaction;

    }


    @Transactional
    public PurchaseOrder updateInventoryTransaction(PurchaseOrder updatedInventoryTransaction) {
        logger.trace("Entering updateInventoryTransaction() with updatedInventoryTransaction");
        Optional<PurchaseOrder> existingInventoryTransaction =
                Optional.ofNullable(getInventoryTransactionById(updatedInventoryTransaction.getInventoryTransactionId()));
        if (existingInventoryTransaction.isPresent()) {
            return purchaseOrderRepository.getEntityManager().merge(updatedInventoryTransaction);
        } else {
            String errorMessage = "InventoryTransaction with ID " + updatedInventoryTransaction.getInventoryTransactionId() + " does not exist";
            logger.error(errorMessage);
            throw new IllegalArgumentException("InventoryTransaction with ID " + updatedInventoryTransaction.getInventoryTransactionId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteInventoryTransaction(Long inventoryTransactionId) {
        logger.trace("Entering deleteInventoryTransaction() with ID");
        return purchaseOrderRepository.deleteById(inventoryTransactionId);
    }


}
