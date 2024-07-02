package org.bonbloc.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.WarehouseLocation;
import org.bonbloc.service.WarehouseLocationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/warehouseLocation")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Warehouse Location REST EndPoint")
public class WarehouseLocationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseLocationController.class);

    @Inject
    WarehouseLocationService warehouseLocationService;

    @POST
    @Transactional
    public Response createWarehouseLocation(@Valid WarehouseLocation warehouseLocation) {
        LOGGER.info("Creating new warehouse location");
        try {
            WarehouseLocation createdWarehouseLocation = warehouseLocationService.createWarehouseLocation(warehouseLocation);
            LOGGER.trace("Warehouse location created successfully");
            return Response.status(Response.Status.CREATED).entity(createdWarehouseLocation).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create warehouse location: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Warehouse Location: " + e.getMessage()).build();
        }
    }

    @GET
    public List<WarehouseLocation> getAllWarehouseLocation() {
        return warehouseLocationService.getAllWarehouseLocation();
    }

    @GET
    @Path("/{warehouseLocationId}")
    public Response getWarehouseLocationById(@PathParam("warehouseLocationId") Long warehouseLocationId) {
        try {
            WarehouseLocation warehouseLocation = warehouseLocationService.getWarehouseLocationById(warehouseLocationId);
            if (warehouseLocation != null) {
                LOGGER.info("Warehouse location retrieved");
                return Response.ok(warehouseLocation).build();
            } else {
                LOGGER.error("Warehouse location not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Warehouse Location with ID " + warehouseLocationId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve warehouse location: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Warehouse Location: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{warehouseLocationId}")
    @Transactional
    public Response updateWarehouseLocation(@PathParam("warehouseLocationId") Long warehouseLocationId,
                                            @Valid WarehouseLocation updatedWarehouseLocation) {
        LOGGER.info("Updating warehouse started");
        try {
            WarehouseLocation updated = warehouseLocationService.updateWarehouseLocation(updatedWarehouseLocation);
            LOGGER.trace("Warehouse location updated successfully");
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.info("Failed to update warehouse location: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Warehouse Location: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{warehouseLocationId}")
    @Transactional
    public Response deleteWarehouseLocation(@PathParam("warehouseLocationId") Long warehouseLocationId) {
        LOGGER.info("Deleting warehouse ");
        try {
            boolean deleted = warehouseLocationService.deleteWarehouseLocation(warehouseLocationId);
            if (deleted) {
                LOGGER.info("Warehouse location deleted successfully");
                return Response.noContent().build();
            } else {
                LOGGER.info("Warehouse location not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Warehouse Location with ID " + warehouseLocationId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.info("Failed to delete warehouse location: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Warehouse Location: " + e.getMessage()).build();
        }
    }
}
