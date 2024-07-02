package org.bonbloc.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.ProductStorageLocation;
import org.bonbloc.repository.ProductStorageLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class ProductStorageLocationService {
    private static final Logger logger = LoggerFactory.getLogger(ProductStorageLocationService.class);
    @Inject
    ProductStorageLocationRepository productStorageLocationLocationRepository;

    public List<ProductStorageLocation> getAllProductStorageLocation() {
        logger.trace("Entering getAllProductStorageLocation()");
        return productStorageLocationLocationRepository.listAll();
    }


    public ProductStorageLocation getProductStorageLocationById(Long productStorageLocationId) {
        logger.trace("Entering getProductStorageLocationById() ");
        return productStorageLocationLocationRepository.findById(productStorageLocationId);
    }

    @Transactional
    public ProductStorageLocation createProductStorageLocation(ProductStorageLocation productStorageLocation) {
        logger.trace("Entering createProductStorageLocation()");
        productStorageLocationLocationRepository.persist(productStorageLocation);
        logger.trace("Exiting createProductStorageLocation() ");
        return productStorageLocation;
    }

    @Transactional
    public ProductStorageLocation updateProductStorageLocation(ProductStorageLocation updatedProductStorageLocation) {
        logger.trace("Entering updateProductStorageLocation() ");
        Optional<ProductStorageLocation> existingProductStorageLocation =
                Optional.ofNullable(getProductStorageLocationById(updatedProductStorageLocation.getProductStorageLocationId()));
        if (existingProductStorageLocation.isPresent()) {
            logger.trace("Exiting updateProductStorageLocation() with updated productStorageLocation");
            return productStorageLocationLocationRepository.getEntityManager().merge(updatedProductStorageLocation);
        } else {
            String errorMessage = "ProductStorageLocation with ID " + updatedProductStorageLocation.getProductStorageLocationId() + " does not exist";
            logger.error(errorMessage);
            throw new IllegalArgumentException("ProductStorageLocation with ID " + updatedProductStorageLocation.getProductStorageLocationId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteProductStorageLocation(Long productStorageLocationId) {
        logger.trace("Entering deleteProductStorageLocation()");
        return productStorageLocationLocationRepository.deleteById(productStorageLocationId);
    }
}
