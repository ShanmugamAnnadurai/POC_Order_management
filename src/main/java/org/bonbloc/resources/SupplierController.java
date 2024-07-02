package org.bonbloc.resources;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.Supplier;
import org.bonbloc.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/supplier")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SupplierController {

    Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Inject
    SupplierService supplierService;

    @POST
    @Transactional
    public Response createSupplier(@Valid Supplier supplier) {

        try {
            Supplier createdSupplier = supplierService.createSupplier(supplier);
            logger.debug("Supplier Created");
            return Response.status(Response.Status.CREATED).entity(createdSupplier).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Product: " + e.getMessage()).build();
        }
    }

    @GET
    public List<Supplier> getAllSupplier() {
        logger.debug("Get All Suppliers");
        return supplierService.getAllSupplier();
    }

    @GET
    @Path("/{supplierId}")
    public Response getSupplierById(@PathParam("supplierId") Long supplierId) {
        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier != null) {
                logger.debug("Get the Supplier by Id");
                return Response.ok(supplier).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Supplier with ID " + supplier + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve supplier: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{supplierId}")
    @Transactional
    public Response updateSupplier(@PathParam("supplierId") Long supplierId, @Valid Supplier updatedSupplier) {
        try {
            Supplier updated = supplierService.updateSupplier(updatedSupplier);
            logger.debug("Supplier Updated");
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Supplier: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{supplierId}")
    @Transactional
    public Response deleteSupplier(@PathParam("supplierId") Long supplierId) {
        try {
            boolean deleted = supplierService.deleteSupplier(supplierId);
            if (deleted) {
                logger.debug("Supplier Deleted");
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Supplier with ID " + supplierId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Supplier: " + e.getMessage()).build();
        }
    }
}

