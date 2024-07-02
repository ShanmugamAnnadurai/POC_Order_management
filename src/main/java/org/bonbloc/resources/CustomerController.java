package org.bonbloc.resources;
import io.quarkus.security.Authenticated;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.Customer;
import org.bonbloc.service.CustomerService;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Path("/customers")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Inject
    CustomerService customerService;

    @POST
    @Transactional
    public Response createCustomer(@Valid Customer customer) {
        LOGGER.info("Creating new customer");
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            LOGGER.trace("Customer created successfully");
            return Response.status(Response.Status.CREATED).entity(createdCustomer).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create customer: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create customer: " + e.getMessage()).build();
        }
    }



    @CircuitBreaker(
            requestVolumeThreshold = 5,
            failureRatio = 0.2,
            delay = 10000,
            successThreshold = 3,
            failOn = {TimeoutException.class, IOException.class}
    )
    @GET
    public List<Customer> getAllCustomers() throws IOException {
        return customerService.getAllCustomer();
    }

    @GET
    @Path("/{customerId}")
    public Response getCustomerById(@PathParam("customerId") Long customerId) {
        try {
            Customer customer = customerService.getCustomerById(customerId);
            if (customer != null) {
                LOGGER.info("Customer retrieved: {}", customer);
                return Response.ok(customer).build();
            } else {
                LOGGER.warn("Customer with ID {} not found", customerId);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer with ID " + customerId + " not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve customer: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve customer: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{customerId}")
    @Transactional
    public Response updateCustomer(@PathParam("customerId") Long customerId, @Valid Customer updatedCustomer) {
        LOGGER.info("Updating customer ");
        try {
            Customer updated = customerService.updateCustomer(updatedCustomer);
            LOGGER.info("Customer updated successfully: {}", updated);
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.error("Failed to update customer: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update customer: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{customerId}")
    @Transactional
    public Response deleteCustomer(@PathParam("customerId") Long customerId) {
        LOGGER.info("Deleting customer ");
        try {
            boolean deleted = customerService.deleteCustomer(customerId);
            if (deleted) {
                LOGGER.info("Customer deleted successfully");
                return Response.noContent().build();
            } else {
                LOGGER.warn("Customer ID not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer  ID  not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete customer: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete customer: " + e.getMessage()).build();
        }
    }
}
