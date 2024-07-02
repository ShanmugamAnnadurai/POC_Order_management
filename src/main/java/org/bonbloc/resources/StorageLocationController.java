package org.bonbloc.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.StorageLocation;
import org.bonbloc.service.StorageLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/storageLocation")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StorageLocationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageLocationController.class);

    @Inject
    StorageLocationService storageLocationService;

    @POST
    @Transactional
    public Response createStorageLocation(@Valid StorageLocation storageLocation) {
        LOGGER.trace("Attempting to create storage location: {}", storageLocation);
        try {
            StorageLocation createdStorageLocation = storageLocationService.createStorageLocation(storageLocation);
            LOGGER.info("Storage location created successfully");
            return Response.status(Response.Status.CREATED).entity(createdStorageLocation).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create storage location: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Storage Location: " + e.getMessage()).build();
        }
    }

    @GET
    public List<StorageLocation> getAllStorageLocation() {
        return storageLocationService.getAllStorageLocation();
    }

    @GET
    @Path("/{storageLocationId}")
    public Response getStorageLocationById(@PathParam("storageLocationId") Long storageLocationId) {
        try {
            StorageLocation storageLocation = storageLocationService.getStorageLocationById(storageLocationId);
            if (storageLocation != null) {
                return Response.ok(storageLocation).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Storage Location with ID " + storageLocationId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve storage location");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Storage Location: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{storageLocationId}")
    @Transactional
    public Response updateStorageLocation(@PathParam("storageLocationId") Long storageLocationId, @Valid StorageLocation updatedStorageLocation) {
        LOGGER.trace("Updating storage location ");
        try {
            StorageLocation updated = storageLocationService.updateStorageLocation(updatedStorageLocation);
            LOGGER.info("Storage location updated successfully");
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.error("Failed to update storage location: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Storage Location: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{storageLocationId}")
    @Transactional
    public Response deleteStorageLocation(@PathParam("storageLocationId") Long storageLocationId) {
        LOGGER.trace("Deleting storage location");
        try {
            boolean deleted = storageLocationService.deleteStorageLocation(storageLocationId);
            if (deleted) {
                LOGGER.info("Storage location deleted successfully");
                return Response.noContent().build();
            } else {
                LOGGER.info("Storage location not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Storage Location with ID " + storageLocationId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete storage location: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Storage Location: " + e.getMessage()).build();
        }
    }

}
