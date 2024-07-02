package org.bonbloc.resources;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.PurchaseOrder;
import org.bonbloc.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Path("/purchaseOrder")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PurchaseOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderController.class);

    @Inject
    PurchaseOrderService purchaseOrderService;

    @POST
    @Transactional
    public Response createInventoryTransaction(@Valid PurchaseOrder inventoryTransaction) {
        LOGGER.trace("Attempting to create inventory transaction");
        try {
            PurchaseOrder createdInventoryTransaction = purchaseOrderService.createInventoryTransaction(inventoryTransaction);
            LOGGER.trace("Inventory transaction created successfully");
            return Response.status(Response.Status.CREATED).entity(createdInventoryTransaction).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create inventory transaction: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Inventory Transaction: " + e.getMessage()).build();
        }
    }

    @GET
    public List<PurchaseOrder> getAllInventoryTransaction() {
        return purchaseOrderService.getAllInventoryTransaction();
    }

    @GET
    @Path("/{inventoryTransactionId}")
    public Response getInventoryTransactionById(@PathParam("inventoryTransactionId") Long inventoryTransactionId) {
        try {
            PurchaseOrder inventoryTransaction = purchaseOrderService.getInventoryTransactionById(inventoryTransactionId);
            if (inventoryTransaction != null) {
                LOGGER.info("Inventory transaction retrieved");
                return Response.ok(inventoryTransaction).build();
            } else {
                LOGGER.error("Inventory transaction  ID not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Inventory Transaction with ID " + inventoryTransactionId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve inventory transaction: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Inventory Transaction: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{inventoryTransactionId}")
    @Transactional
    public Response updateInventoryTransaction(@PathParam("inventoryTransactionId") Long inventoryTransactionId, @Valid PurchaseOrder updatedInventoryTransaction) {
        LOGGER.trace("Updating inventory transaction ");
        try {
            PurchaseOrder updated = purchaseOrderService.updateInventoryTransaction(updatedInventoryTransaction);
            LOGGER.info("Inventory transaction updated successfully: {}", updated);
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.error("Failed to update inventory transaction: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Inventory Transaction: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{inventoryTransactionId}")
    @Transactional
    public Response deleteInventoryTransaction(@PathParam("inventoryTransactionId") Long inventoryTransactionId) {
        LOGGER.trace("Deleting inventory transaction ");
        try {
            boolean deleted = purchaseOrderService.deleteInventoryTransaction(inventoryTransactionId);
            if (deleted) {
                LOGGER.info("Inventory transaction deleted successfully");
                return Response.noContent().build();
            } else {
                LOGGER.info("Inventory transaction  ID not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Inventory Transaction  ID  not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete inventory transaction: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Inventory Transaction: " + e.getMessage()).build();
        }
    }
}
