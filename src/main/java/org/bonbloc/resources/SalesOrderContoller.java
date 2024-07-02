package org.bonbloc.resources;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.SalesOrder;
import org.bonbloc.service.SalesOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/salesOrder")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SalesOrderContoller {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderContoller.class);

    @Inject
    SalesOrderService salesOrderService;

    @POST
    @Transactional
    public Response createPurchaseOrder(@Valid SalesOrder salesOrder) {
        LOGGER.info("Creating new SalesOrder");
        try {
            SalesOrder createdSalesOrder = salesOrderService.createPurchaseOrder(salesOrder);
            LOGGER.trace("Purchase order created: + salesOrder");
            return Response.status(Response.Status.CREATED).entity(createdSalesOrder).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create purchase order");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create purchase order: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public List<SalesOrder> getAllPurchaseOrder() {
        return salesOrderService.getAllPurchaseOrders();
    }

    @GET
    @Path("/{purchaseOrderId}")
    public Response getPurchaseOrderById(@PathParam("purchaseOrderId") Long purchaseOrderId) {
        try {
            SalesOrder salesOrder = salesOrderService.getPurchaseOrderById(purchaseOrderId);
            if (salesOrder != null) {
                LOGGER.trace("Purchase order found ");
                return Response.ok(salesOrder).build();
            } else {
                LOGGER.warn("Purchase order  not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase Order with ID " + purchaseOrderId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve purchase order: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Purchase Order: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{purchaseOrderId}")
    @Transactional
    public Response updatePurchaseOrder(@PathParam("purchaseOrderId") Long purchaseOrderId, @Valid SalesOrder updatedSalesOrder) {
        LOGGER.trace("Updating purchase order");
        try {
            SalesOrder updated = salesOrderService.updatePurchaseOrder(updatedSalesOrder);
            LOGGER.trace("Purchase order updated successfully ");
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.error("Failed to update purchase order: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Purchase Order: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{purchaseOrderId}")
    @Transactional
    public Response deletePurchaseOrder(@PathParam("purchaseOrderId") Long purchaseOrderId) {
        LOGGER.trace("Deleting purchase order");
        try {
            boolean deleted = salesOrderService.deletePurchaseOrder(purchaseOrderId);
            if (deleted) {
                LOGGER.trace("Purchase order deleted successfully ");
                return Response.noContent().build();
            } else {
                LOGGER.warn("Purchase order not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase Order with ID " + purchaseOrderId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete purchase order: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Purchase Order: " + e.getMessage()).build();
        }
    }

}
