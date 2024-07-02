package org.bonbloc.resources;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.SalesOrderItemDetails;
import org.bonbloc.service.SalesOrderItemDetailsService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Path("/salesOrderItem")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Purchase Order Item REST EndPoint")
public class SalesOrderItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderItemController.class);

    @Inject
    SalesOrderItemDetailsService salesOrderItemDetailsService;

    @POST
    @Transactional
    public Response createPurchaseOrderItem(@Valid SalesOrderItemDetails purchaseOrderItem) {
        LOGGER.trace("Attempting to create purchase order item");
        try {
            SalesOrderItemDetails createdPurchaseOrderItem = salesOrderItemDetailsService.createPurchaseOrderItem(purchaseOrderItem);
            LOGGER.info("Purchase order item created successfully");
            return Response.status(Response.Status.CREATED).entity(createdPurchaseOrderItem).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create purchase order item: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Purchase Order Item: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All the Purchase Order Item")
    public List<SalesOrderItemDetails> getAllPurchaseOrderItem() {
        List<SalesOrderItemDetails> items = salesOrderItemDetailsService.getAllPurchaseOrderItem();
        LOGGER.trace("Retrieved purchase order items");
        return items;
    }

    @GET
    @Path("/{purchaseOrderItemId}")
    public Response getPurchaseOrderItemById(@PathParam("purchaseOrderItemId") Long purchaseOrderItemId) {
        try {
            SalesOrderItemDetails purchaseOrderItem = salesOrderItemDetailsService.getPurchaseOrderItemById(purchaseOrderItemId);
            if (purchaseOrderItem != null) {
                LOGGER.info("Purchase order item retrieved");
                return Response.ok(purchaseOrderItem).build();
            } else {
                LOGGER.info("Purchase order item not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase Order Item with ID " + purchaseOrderItemId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve purchase order item: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Purchase Order Item: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{purchaseOrderItemId}")
    @Transactional
    public Response updatePurchaseOrderItem(@PathParam("purchaseOrderItemId") Long purchaseOrderItemId, @Valid SalesOrderItemDetails updatedPurchaseOrderItem) {
        LOGGER.trace("Updating purchase order item ");
        try {
            SalesOrderItemDetails updated = salesOrderItemDetailsService.updatePurchaseOrderItem(updatedPurchaseOrderItem);
            LOGGER.trace("Purchase order item updated successfully");
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.error("Failed to update purchase order item: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Purchase Order Item: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{purchaseOrderItemId}")
    @Transactional
    public Response deletePurchaseOrderItem(@PathParam("purchaseOrderItemId") Long purchaseOrderItemId) {
        LOGGER.trace("Deleting purchase order item ");
        try {
            boolean deleted = salesOrderItemDetailsService.deletePurchaseOrderItem(purchaseOrderItemId);
            if (deleted) {
                LOGGER.trace("Purchase order item deleted successfully");
                return Response.noContent().build();
            } else {
                LOGGER.error("Purchase order item not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase Order Item with ID " + purchaseOrderItemId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete purchase order item: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Purchase Order Item: " + e.getMessage()).build();
        }
    }
}
