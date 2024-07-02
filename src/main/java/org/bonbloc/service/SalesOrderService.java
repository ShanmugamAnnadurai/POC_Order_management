package org.bonbloc.service;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.*;
import org.bonbloc.repository.SalesOrderRepository;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SalesOrderService {
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderService.class);
    @Inject
    SalesOrderRepository salesOrderRepository;

    @Inject
    CustomerService customerService;

    @Inject
    ProductService productService;

    @Inject
    SalesOrderItemDetailsService salesOrderItemDetailsService;

    @Inject
    StorageLocationService storageLocationService;

    @CacheResult(cacheName = "Purchase")
    public List<SalesOrder> getAllPurchaseOrders() {
        logger.trace("Entering getAllPurchaseOrders()");
        return salesOrderRepository.listAll();
    }


    @Transactional
    @Retry(maxRetries = 3, delay = 500)
    public SalesOrder getPurchaseOrderById(Long purchaseOrderId) {
        logger.trace("Entering getPurchaseOrderById()");
        return salesOrderRepository.findById(purchaseOrderId);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "Purchase")
    @Retry(maxRetries = 3, delay = 500)
    public SalesOrder createPurchaseOrder(SalesOrder request) {
        logger.trace("Entering createPurchaseOrder() ");
        if (request.getCustomer().getCustomerId() == null && request.getOrderItems() == null ) {
            logger.error("CUSTOMER ID REQUIRED");
            throw new RuntimeException("Customer ID or Order Items are required to create a purchase order");
        }

        if (request.getCustomer().getCustomerId() != null) {
            Customer customer = customerService.getCustomerById(request.getCustomer().getCustomerId());
            if (customer == null) {
                logger.error("CUSTOMER IS NULL");
                throw new RuntimeException("Customer with ID " + request.getCustomer().getCustomerId() + " not found");

            }
        }

        if (request.getOrderItems() != null) {
            boolean allProductsAvailable = request.getOrderItems().stream()
                    .allMatch(orderItem -> {
                        Product product = productService.getProductById(orderItem.getProduct().getProductId());
                        return product != null && product.getQuantityAvailable() >= orderItem.getQuantity();
                    });

            if (!allProductsAvailable) {
                logger.error("PRODUCTS ARE OUT OF STOCK OR NOT FOUND");
                throw new RuntimeException("One or more products are out of stock or not found");
            }


            for (SalesOrderItemDetails orderItem : request.getOrderItems()) {
                Product product = productService.getProductById(orderItem.getProduct().getProductId());
                productService.updateProductToReduceQuantity(product, orderItem.getQuantity());
                logger.trace("Updated product to reduce quantity");
            }
        }

        Double totalPrice = request.getOrderItems().stream()
                .mapToDouble(orderItem -> {
                    Product product = productService.getProductById(orderItem.getProduct().getProductId());
                    return product.getPrice() * orderItem.getQuantity();
                })
                .sum();
        logger.trace("Total price calculated");
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderDate(Instant.now());
        salesOrder.setCustomer(request.getCustomer().getCustomerId() != null ? customerService.getCustomerById(request.getCustomer().getCustomerId()) : null);
        salesOrder.setOrderItems(request.getOrderItems());
        salesOrder.setDeliveryDate(request.getDeliveryDate());
        salesOrder.setPaymentMethod(request.getPaymentMethod());
        salesOrder.setTotalPrice(totalPrice);
        salesOrder.setStatus("Pending");
        salesOrder.setPaymentMethod("Cash");
        salesOrderRepository.persist(salesOrder);

        if (request.getOrderItems() != null) {
            for (SalesOrderItemDetails orderItem : request.getOrderItems()) {
                Product product = productService.getProductById(orderItem.getProduct().getProductId());
                orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
                orderItem.setSalesOrder(salesOrder);
                salesOrderItemDetailsService.createPurchaseOrderItem(orderItem);
            }
        }
        logger.trace("Exiting createPurchaseOrder() ");
        return salesOrder;

    }


    @Transactional
    @CacheInvalidate(cacheName = "Purchase")
    public void updatePurchaseOrderStatus( @CacheKey Long purchaseOrderId, String shipmentStatus) {
        logger.trace("Entering updatePurchaseOrderStatus() ");
        SalesOrder salesOrder = getPurchaseOrderById(purchaseOrderId);
        if (salesOrder != null) {
            if (shipmentStatus.equals("Shipped")) {
                salesOrder.setStatus("Shipped");
                logger.trace("Purchase order status updated");
            } else if (shipmentStatus.equals("Delivered")) {
                salesOrder.setStatus("Delivered");
                salesOrder.setDeliveryDate(Instant.now());
                logger.trace("Purchase order status  and delivery date updated");

            }
            updatePurchaseOrder(salesOrder);
            logger.trace("Exiting updatePurchaseOrderStatus()");
        }
    }



    @Transactional
    @CacheInvalidateAll(cacheName = "Purchase")
    public SalesOrder updatePurchaseOrder(SalesOrder updatedSalesOrder) {
        logger.trace("Entering updatePurchaseOrder() ");
        Optional<SalesOrder> existingPurchaseOrder = Optional.ofNullable(getPurchaseOrderById(updatedSalesOrder.getPurchaseOrderId()));
        if (existingPurchaseOrder.isPresent()) {
            logger.trace("Exiting updatePurchaseOrder() with updated purchaseOrder");
            return salesOrderRepository.getEntityManager().merge(updatedSalesOrder);
        } else {
            String errorMessage = "Purchase Order with ID " + updatedSalesOrder.getPurchaseOrderId() + " does not exist";
            logger.error(errorMessage);
            throw new IllegalArgumentException("Purchase Order with ID " + updatedSalesOrder.getPurchaseOrderId() + " does not exist");
        }
    }

    @Transactional
    public boolean deletePurchaseOrder(Long purchaseOrderId) {
        logger.trace("Entering deletePurchaseOrder() ");
        return salesOrderRepository.deleteById(purchaseOrderId);
    }
}
