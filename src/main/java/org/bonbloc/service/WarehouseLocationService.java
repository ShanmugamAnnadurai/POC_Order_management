package org.bonbloc.service;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.WarehouseLocation;
import org.bonbloc.repository.WarehouseLocationRepository;
import org.jboss.logging.Logger;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WarehouseLocationService {

    private static final Logger logger = Logger.getLogger(WarehouseLocationService.class);

    @Inject
    WarehouseLocationRepository warehouseLocationRepository;


    public List<WarehouseLocation> getAllWarehouseLocation() {
        logger.trace("Entering getAllWarehouseLocation()");
        List<WarehouseLocation> locations = warehouseLocationRepository.listAll();
        logger.trace("Exiting getAllWarehouseLocation()" );
        return locations;
    }


    public WarehouseLocation getWarehouseLocationById(Long warehouseLocationId) {
        logger.trace("Entering getWarehouseLocationById() " );
        WarehouseLocation location = warehouseLocationRepository.findById(warehouseLocationId);
        logger.trace("Exiting getWarehouseLocationById()" );
        return location;
    }

    @Transactional
    public WarehouseLocation createWarehouseLocation(WarehouseLocation warehouseLocation) {
        logger.trace("Entering createWarehouseLocation() " );
        warehouseLocationRepository.persist(warehouseLocation);
        logger.trace("Exiting createWarehouseLocation()" );
        return warehouseLocation;
    }

    @Transactional
    public WarehouseLocation updateWarehouseLocation(WarehouseLocation updatedWarehouseLocation) {
        logger.trace("Entering updateWarehouseLocation()" );
        Optional<WarehouseLocation> existingWarehouseLocation = Optional.ofNullable(getWarehouseLocationById(updatedWarehouseLocation.getWarehouseLocationId()));
        if (existingWarehouseLocation.isPresent()) {
            WarehouseLocation mergedLocation = warehouseLocationRepository.getEntityManager().merge(updatedWarehouseLocation);
            logger.trace("Exiting updateWarehouseLocation()" );
            return mergedLocation;
        } else {
            String errorMessage = "WarehouseLocation with ID " + updatedWarehouseLocation.getWarehouseLocationId() + "does not exist";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Transactional
    public boolean deleteWarehouseLocation(Long warehouseLocationId) {
        logger.trace("Entering deleteWarehouseLocation()" );
        boolean deleted = warehouseLocationRepository.deleteById(warehouseLocationId);
        logger.trace("Exiting deleteWarehouseLocation() " );
        return deleted;
    }
}
