package org.bonbloc.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bonbloc.entity.Product;
import org.bonbloc.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

import java.util.Collections;
import java.util.List;

@Path("/products")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Inject
    ProductService productService;
    
    @POST
    @Transactional
    public Response createProduct(@Valid Product product) {
        LOGGER.trace("Attempting to create product");
        try {
            Product createdProduct = productService.createProduct(product);
            LOGGER.trace("Product created successfully");
            return Response.status(Response.Status.CREATED).entity(createdProduct).build();
        } catch (Exception e) {
            LOGGER.error("Failed to create product: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Product: " + e.getMessage()).build();
        }
    }
    
    @GET
    @Timeout(value = 1500) 
    @Fallback(fallbackMethod = "getProductFallback", applyOn = TimeoutException.class)
    public List<Product> getAllProducts() {


        return productService.getAllProducts();
//        try {
//
//        } catch (TimeoutException e) {
//            LOGGER.error("Timeout exception occurred: {}", e.getMessage());
////            return getProductFallback();
//        }
    }
    
    @GET
    @Path("/{productId}")
    public Response getProductById(@PathParam("productId") Long productId) {
        try {
            Product product = productService.getProductById(productId);
            if (product != null) {
                LOGGER.trace("Product retrieved: {}", product);
                return Response.ok(product).build();
            } else {
                LOGGER.error("Product not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Product not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve product: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Product: " + e.getMessage()).build();
        }
    }
    
    @PUT
    @Path("/{productId}")
    @Transactional
    public Response updateProduct(@PathParam("productId") Long productId, @Valid Product updatedProduct) {
        LOGGER.trace("Updating product ");
        try {
            Product updated = productService.updateProduct(updatedProduct);
            LOGGER.info("Product updated successfully");
            return Response.ok(updated).build();
        } catch (Exception e) {
            LOGGER.error("Failed to update product: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update product: " + e.getMessage())
                    .build();
        }
    }
    
    @DELETE
    @Path("/{productId}")
    @Transactional
    public Response deleteProduct(@PathParam("productId") Long productId) {
        LOGGER.trace("Deleting product ");
        try {
            boolean deleted = productService.deleteProduct(productId);
            if (deleted) {
                LOGGER.info("Product deleted successfully");
                return Response.noContent().build();
            } else {
                LOGGER.error("Product not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Product not found").build();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete product: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete product: " + e.getMessage()).build();
        }
    }
    
    public List<Product> getProductFallback() {
        return Collections.emptyList();
    }
}
