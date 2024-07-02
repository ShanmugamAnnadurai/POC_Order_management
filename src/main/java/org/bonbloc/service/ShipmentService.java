package org.bonbloc.service;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.DeliveryPartner;
import org.bonbloc.entity.SalesOrder;
import org.bonbloc.entity.Shipment;
import org.bonbloc.repository.ShipmentRepository;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@ApplicationScoped
public class ShipmentService {
    private static final Logger logger = LoggerFactory.getLogger(ShipmentService.class);

    @Inject
    ShipmentRepository shipmentRepository;

    @Inject
    SalesOrderService salesOrderService;

    @Inject
    DeliveryPartnerService deliveryPartnerService;


    @CacheResult(cacheName = "Shipment")
    public List<Shipment> getAllShipment() {
        logger.trace("Entering getAllShipment()");
        return shipmentRepository.listAll();
    }

    @Retry(maxRetries = 3, delay = 500)
    public Shipment getShipmentById(Long shipmentId) {
        logger.trace("Entering getShipmentById()");
        throw new RuntimeException("Simulated failure for testing retry");
//        return shipmentRepository.findById(shipmentId);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "Shipment")
    @Retry(maxRetries = 3, delay = 500)
    public Shipment createShipment(Shipment shipment) {
        logger.trace("Entering createShipment()");
        if (shipment.getSalesOrder().getPurchaseOrderId() == null ) {
            throw  new RuntimeException("Purchase Order ID are required to create a Shipment");

        }

        SalesOrder salesOrder = salesOrderService.getPurchaseOrderById(shipment.getSalesOrder().getPurchaseOrderId());
        if (salesOrder == null) {
            logger.error("Purchase order ID not found");
            throw new RuntimeException("Purchase Order with ID " + shipment.getSalesOrder().getPurchaseOrderId() + " not found");
        }

        DeliveryPartner deliveryPartner = deliveryPartnerService.getDeliveryPartnerById(getANumber());
        shipment.setShipmentDate(Instant.now());
        shipment.setStatus("Shipped");
        shipment.setDeliveryPartner(deliveryPartner);
        shipmentRepository.persist(shipment);
        salesOrderService.updatePurchaseOrderStatus(shipment.getSalesOrder().getPurchaseOrderId(),"Shipped");
        logger.trace("Exiting createShipment() ");
        return shipment;
    }


    public static Long getANumber() {
        Random rand = new Random();
        return rand.nextLong(10) + 1;
    }


    @Transactional
    @CacheInvalidateAll(cacheName = "Shipment")
    public Shipment updateShipment( Shipment updatedShipment) {
        logger.trace("Entering updateShipment() ");
        Optional<Shipment> existingShipment = Optional.ofNullable(getShipmentById(updatedShipment.getShipmentId()));
        if (existingShipment.isPresent()) {
            return shipmentRepository.getEntityManager().merge(updatedShipment);
        } else {
            String errorMessage = "Shipment with ID " + updatedShipment.getShipmentId() + " does not exist";
            logger.error(errorMessage);
            throw new IllegalArgumentException("Shipment with ID " + updatedShipment.getShipmentId() + " does not exist");
        }
    }

    @Transactional
    @CacheInvalidate(cacheName = "Shipment")
    public Shipment updateShipmentForDelivery( @CacheKey Long updatedShipmentId) {
        logger.trace("Entering updateShipmentForDelivery() ");
        Shipment existingShipment = shipmentRepository.findById(updatedShipmentId);
        if (existingShipment != null) {
            existingShipment.setArrivalDate(Instant.now());
            existingShipment.setStatus("Delivered");
            logger.trace("Shipment status updated");
            if(existingShipment.getArrivalDate() != null){
                salesOrderService.updatePurchaseOrderStatus(existingShipment.getSalesOrder().getPurchaseOrderId(),"Delivered");
                logger.trace("Purchase Order status updated");
            }
            return shipmentRepository.getEntityManager().merge(existingShipment);
        } else {
            String errorMessage = "Shipment with ID " + updatedShipmentId + " not found";
            logger.error(errorMessage);
            throw new IllegalArgumentException("Shipment with ID " + updatedShipmentId + " not found");
        }
    }

    @Transactional
    public boolean deleteShipment(Long shipmentId) {
        logger.trace("Entering deleteShipment() ");
        return shipmentRepository.deleteById(shipmentId);
    }

}
