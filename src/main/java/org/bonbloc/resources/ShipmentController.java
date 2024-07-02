package org.bonbloc.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.Shipment;
import org.bonbloc.service.SalesOrderService;
import org.bonbloc.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@Path("/shipment")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShipmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentController.class);


    @Inject
    ShipmentService shipmentService;

    @Inject
    SalesOrderService salesOrderService;

    @POST
    @Transactional
    public Response createShipment(@Valid Shipment shipment) {
        LOGGER.trace("Attempting to create shipment");
        try {
            Shipment createdShipment = shipmentService.createShipment(shipment);
            LOGGER.info("Shipment created successfully");
            return Response.status(Response.Status.CREATED).entity(createdShipment).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create shipment: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Shipment: " + e.getMessage()).build();
        }
    }


    @GET
    public List<Shipment> getAllShipment() {
        return shipmentService.getAllShipment();
    }

    @GET
    @Path("/{shipmentId}")
    public Response getShipmentById(@PathParam("shipmentId") Long shipmentId) {
        try {
            Shipment shipment = shipmentService.getShipmentById(shipmentId);
            return Response.ok(shipment).build();
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve shipment");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve shipment: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{shipmentId}")
    @Transactional
    public Response updateShipment(@PathParam("shipmentId") Long shipmentId, @Valid Shipment updatedShipment) {
        LOGGER.trace("Updating shipment ");
        try {
            Shipment updated = shipmentService.updateShipment(updatedShipment);
            LOGGER.info("Shipment updated successfully");
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.error("Failed to update shipment: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update shipment: " + e.getMessage()).build();
        }
    }


    @PUT
    @Path("forDelivery/{shipmentId}")
    @Transactional
    public Response updateShipmentForDelivery(@PathParam("shipmentId") Long shipmentId) {
        LOGGER.info("Updating shipment for delivery");
        try {
            Shipment updated = shipmentService.updateShipmentForDelivery(shipmentId);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update shipment: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{shipmentId}")
    @Transactional
    public Response deleteShipment(@PathParam("shipmentId") Long shipmentId) {
        LOGGER.trace("Deleting shipment ");
        try {
            boolean deleted = shipmentService.deleteShipment(shipmentId);
            if (deleted) {
                LOGGER.info("Shipment deleted successfully");
                return Response.noContent().build();
            } else {
                LOGGER.info("Shipment not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Shipment with ID " + shipmentId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete shipment: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete shipment: " + e.getMessage()).build();
        }
    }
}
