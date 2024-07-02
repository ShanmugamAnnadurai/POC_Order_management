package org.bonbloc.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.SalesOrderItemDetails;
import org.bonbloc.repository.SalesOrderItemDetailsRepository;
import org.jboss.logging.Logger;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SalesOrderItemDetailsService {
    @Inject
    SalesOrderItemDetailsRepository salesOrderItemDetailsRepository;

    private static final Logger logger = Logger.getLogger(SalesOrderItemDetailsService.class);


    public List<SalesOrderItemDetails> getAllPurchaseOrderItem() {
        logger.trace("Entering getAllPurchaseOrderItem()");
        List<SalesOrderItemDetails> items = salesOrderItemDetailsRepository.listAll();
        logger.trace("Exiting getAllPurchaseOrderItem()" );
        return items;
    }


    public SalesOrderItemDetails getPurchaseOrderItemById(Long purchaseOrderItemId) {
        logger.trace("Entering getPurchaseOrderItemById()" );
        SalesOrderItemDetails item = salesOrderItemDetailsRepository.findById(purchaseOrderItemId);
        logger.trace("Exiting getPurchaseOrderItemById() " );
        return item;
    }

    @Transactional
    public SalesOrderItemDetails createPurchaseOrderItem(SalesOrderItemDetails purchaseOrderItem) {
        logger.trace("Entering createPurchaseOrderItem() " );
        salesOrderItemDetailsRepository.persist(purchaseOrderItem);
        logger.trace("Exiting createPurchaseOrderItem()");
        return purchaseOrderItem;
    }

    @Transactional
    public SalesOrderItemDetails updatePurchaseOrderItem(SalesOrderItemDetails updatedPurchaseOrderItem) {
        logger.trace("Entering updatePurchaseOrderItem() ");
        Optional<SalesOrderItemDetails> existingPurchaseOrderItem = Optional.ofNullable(getPurchaseOrderItemById(updatedPurchaseOrderItem.getOrderDetailsId()));
        if (existingPurchaseOrderItem.isPresent()) {
            SalesOrderItemDetails mergedItem = salesOrderItemDetailsRepository.getEntityManager().merge(updatedPurchaseOrderItem);
            logger.trace("Exiting updatePurchaseOrderItem() " );
            return mergedItem;
        } else {
            logger.trace("Purchase Order Item  does not exist");
            throw new IllegalArgumentException("Purchase Order Item with ID " +  updatedPurchaseOrderItem.getOrderDetailsId() + " does not exist");
        }
    }

    @Transactional
    public boolean deletePurchaseOrderItem(Long purchaseOrderItemId) {
        logger.trace("Entering deletePurchaseOrderItem()  " );
        boolean deleted = salesOrderItemDetailsRepository.deleteById(purchaseOrderItemId);
        logger.trace("Exiting deletePurchaseOrderItem() " );
        return deleted;
    }
}
