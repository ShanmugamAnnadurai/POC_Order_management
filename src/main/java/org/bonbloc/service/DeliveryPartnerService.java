package org.bonbloc.service;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.DeliveryPartner;
import org.bonbloc.repository.DeliveryPartnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DeliveryPartnerService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryPartnerService.class);

    @Inject
    DeliveryPartnerRepository deliveryPartnerRepository;


    public List<DeliveryPartner> getAllDeliveryPartner() {
        logger.trace("Entering getAllDeliveryPartner()");
        return deliveryPartnerRepository.listAll();
    }


    public DeliveryPartner getDeliveryPartnerById(Long deliveryPartnerId) {
        logger.trace("Entering getDeliveryPartnerById() with ID");
        return deliveryPartnerRepository.findById(deliveryPartnerId);
    }

    @Transactional
    public DeliveryPartner createDeliveryPartner(DeliveryPartner deliveryPartner) {
        logger.trace("Entering createDeliveryPartner() with deliveryPartner" );
        deliveryPartnerRepository.persist(deliveryPartner);
        logger.trace("Exiting createDeliveryPartner() with created deliveryPartner");
        return deliveryPartner;
    }

    @Transactional
    public DeliveryPartner updateDeliveryPartner(DeliveryPartner updatedDeliveryPartner) {
        logger.trace("Entering updateDeliveryPartner() with updatedDeliveryPartner");
        Optional<DeliveryPartner> existingDeliveryPartner = Optional.ofNullable(getDeliveryPartnerById(updatedDeliveryPartner.getDeliveryPartnerId()));
        if (existingDeliveryPartner.isPresent()) {
            return deliveryPartnerRepository.getEntityManager().merge(updatedDeliveryPartner);
        } else {
            throw new IllegalArgumentException("Delivery Partner with ID " + updatedDeliveryPartner.getDeliveryPartnerId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteDeliveryPartner(Long deliveryPartnerId) {
        logger.trace("Entering deleteDeliveryPartner() with ID");
        return deliveryPartnerRepository.deleteById(deliveryPartnerId);
    }
}






