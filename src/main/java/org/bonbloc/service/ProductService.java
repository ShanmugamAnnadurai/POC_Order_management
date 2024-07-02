package org.bonbloc.service;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.Product;
import org.bonbloc.entity.ProductStorageLocation;
import org.bonbloc.entity.StorageLocation;
import org.bonbloc.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Inject
    ProductRepository productRepository;

    @Inject
    StorageLocationService storageLocationService;


    @Inject
    ProductStorageLocationService productStorageLocationService;


    public List<Product> getAllProducts() {
        logger.trace("Entering getAllProducts()");
        return productRepository.listAll();
    }


    public Product getProductById(Long productId) {
        logger.trace("Entering getProductById() with ID");
        return productRepository.findById(productId);
    }

    @Transactional
    public Product createProduct(Product product) {
        logger.trace("Entering createProduct() with product");
        productRepository.persist(product);
        logger.trace("Exiting createProduct() with created product");
        return product;
    }

    @Transactional
    public void updateProductQuantity(Long productId, int quantity) {
        logger.trace("Entering updateProductQuantity() ");
        Product product = getProductById(productId);
        product.setQuantityAvailable(product.getQuantityAvailable() - quantity);
        updateProduct(product);
    }

    @Transactional
    public Product updateProduct(Product updatedProduct) {
        logger.trace("Entering updateProduct() " );
        Optional<Product> existingProduct = Optional.ofNullable(getProductById(updatedProduct.getProductId()));
        if (existingProduct.isPresent()) {
            return productRepository.getEntityManager().merge(updatedProduct);
        } else {
            logger.error("Error updating product");
            throw new IllegalArgumentException("Product with ID " + updatedProduct.getProductId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteProduct(Long productId) {
        logger.trace("Entering deleteProduct() ");
        return productRepository.deleteById(productId);
    }

    @Transactional
    public void updateProductToReduceQuantity(Product product, int quantity) {
        logger.trace("Entering updateProductToReduceQuantity()");
        int newQuantity = product.getQuantityAvailable() - quantity;
        product.setQuantityAvailable(newQuantity);

        List<ProductStorageLocation> productStorageLocations = product.getProductStorageLocation();
        productStorageLocations.sort((a, b) -> b.getQuantity().compareTo(a.getQuantity()));

        int[] remainingQuantityArray = {quantity};

        productStorageLocations.stream()
                .forEach(productStorageLocation -> {
                    StorageLocation storageLocation = productStorageLocation.getStorageLocation();
                    int availableQuantity = productStorageLocation.getQuantity();

                    if (remainingQuantityArray[0] <= availableQuantity) {
                        productStorageLocation.setQuantity(availableQuantity - remainingQuantityArray[0]);
                        int occupiedSpaceReduction = remainingQuantityArray[0];
                        storageLocation.setOccupiedSpace(Math.max(0, storageLocation.getOccupiedSpace() - occupiedSpaceReduction * product.getSizeOfProduct()));
                        remainingQuantityArray[0] = 0;
                    } else {
                        productStorageLocation.setQuantity(0);
                        int occupiedSpaceReduction = availableQuantity;
                        storageLocation.setOccupiedSpace(Math.max(0, storageLocation.getOccupiedSpace() - occupiedSpaceReduction * product.getSizeOfProduct()));
                        remainingQuantityArray[0] -= availableQuantity;
                    }
                });

        if (remainingQuantityArray[0] > 0) {
            logger.error("Not enough product in storage locations for product " + product.getName());
            throw new RuntimeException("Not enough product in storage locations for product " + product.getName());
        }
        updateProduct(product);
    }
}
