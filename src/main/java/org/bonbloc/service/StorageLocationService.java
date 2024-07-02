package org.bonbloc.service;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.StorageLocation;
import org.bonbloc.repository.StorageLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class StorageLocationService {
    private static final Logger logger = LoggerFactory.getLogger(StorageLocationService.class);
    @Inject
    StorageLocationRepository storageLocationRepository;


    public List<StorageLocation> getAllStorageLocation() {
        logger.trace("Entering getAllStorageLocation()");
        return storageLocationRepository.listAll();
    }


    public StorageLocation getStorageLocationById(Long storageLocationId) {
        logger.trace("Entering getStorageLocationById() ");
        return storageLocationRepository.findById(storageLocationId);
    }

    @Transactional
    public StorageLocation createStorageLocation(StorageLocation storageLocation) {
        logger.trace("Entering createStorageLocation() ");
        storageLocationRepository.persist(storageLocation);
        logger.trace("Exiting createStorageLocation() ");
        return storageLocation;
    }

    @Transactional
    public StorageLocation updateStorageLocation(StorageLocation updatedStorageLocation) {
        logger.trace("Entering updateStorageLocation() ");
        Optional<StorageLocation> existingStorageLocation =
                Optional.ofNullable(getStorageLocationById(updatedStorageLocation.getStorageLocationId()));
        if (existingStorageLocation.isPresent()) {
            return storageLocationRepository.getEntityManager().merge(updatedStorageLocation);
        } else {
            String errorMessage = "Storage Location with ID " + updatedStorageLocation.getStorageLocationId() + " does not exist";
            logger.error(errorMessage);
            throw new IllegalArgumentException("Storage Location with ID " + updatedStorageLocation.getStorageLocationId() + " does not exist");
        }
    }


    @Transactional
    public boolean deleteStorageLocation(Long storageLocationId) {
        logger.trace("Entering deleteStorageLocation()");
        return storageLocationRepository.deleteById(storageLocationId);
    }

}
