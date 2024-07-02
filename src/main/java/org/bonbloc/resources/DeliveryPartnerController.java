package org.bonbloc.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.DeliveryPartner;
import org.bonbloc.service.DeliveryPartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/deliveryPartner")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryPartnerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryPartnerController.class);

    @Inject
    DeliveryPartnerService deliveryPartnerService;

    @POST
    @Transactional
    public Response createDeliveryPartner(@Valid DeliveryPartner deliveryPartner) {
        LOGGER.info("Creating new delivery partner");
        try {
            DeliveryPartner createdDeliveryPartner = deliveryPartnerService.createDeliveryPartner(deliveryPartner);
            LOGGER.trace("Delivery partner created successfully");
            return Response.status(Response.Status.CREATED).entity(createdDeliveryPartner).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create delivery partner: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create delivery Partner: " + e.getMessage()).build();
        }
    }

    @GET
    public List<DeliveryPartner> getAllDeliveryPartner() {
        return deliveryPartnerService.getAllDeliveryPartner();
    }

    @GET
    @Path("/{deliveryPartnerId}")
    public Response getDeliveryPartnerById(@PathParam("deliveryPartnerId") Long deliveryPartnerId) {
        try {
            DeliveryPartner deliveryPartner = deliveryPartnerService.getDeliveryPartnerById(deliveryPartnerId);
            if (deliveryPartner != null) {
                LOGGER.trace("Delivery partner retrieved");
                return Response.ok(deliveryPartner).build();
            } else {
                LOGGER.error("Delivery partner ID not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Delivery Partner with ID  not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve delivery partner: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve delivery Partner: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{deliveryPartnerId}")
    @Transactional
    public Response updateDeliveryPartner(@PathParam("deliveryPartnerId") Long deliveryPartnerId, @Valid DeliveryPartner updatedDeliveryPartner) {
        LOGGER.trace("Updating delivery partner");
        try {
            DeliveryPartner updated = deliveryPartnerService.updateDeliveryPartner(updatedDeliveryPartner);
            LOGGER.info("Delivery partner updated successfully");
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.error("Failed to update delivery partner: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Delivery Partner: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{deliveryPartnerId}")
    @Transactional
    public Response deleteDeliveryPartner(@PathParam("deliveryPartnerId") Long deliveryPartnerId) {
        LOGGER.trace("Deleting delivery partner");
        try {
            boolean deleted = deliveryPartnerService.deleteDeliveryPartner(deliveryPartnerId);
            if (deleted) {
                LOGGER.info("Delivery partner deleted successfully");
                return Response.noContent().build();
            } else {
                LOGGER.error("Delivery partner ID not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Delivery Partner ID  not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete delivery partner: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete delivery Partner: " + e.getMessage()).build();
        }
    }
}
