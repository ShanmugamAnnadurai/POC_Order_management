package org.bonbloc.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bonbloc.entity.Customer;
import org.bonbloc.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {

    private static int requestCounter = 0;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Inject
    CustomerRepository customerRepository;

    public List<Customer> getAllCustomer() throws IOException{
        logger.trace("Entering getAllCustomer()");
        requestCounter++;
        if (requestCounter < 5) {
            if (requestCounter % 2 == 0) {
                throw new RuntimeException(new IOException("Simulated IO Exception"));
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        List<Customer> customers = customerRepository.listAll();
        logger.trace("Exiting getAllCustomer() with customers");
        return customers;
    }

    @Transactional
    public Customer getCustomerById(Long customerId) {
        logger.trace("Entering getCustomerById() with customerId ");
        Customer customer = customerRepository.findById(customerId);
        logger.trace("Exiting getCustomerById()");
        return customer;
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        logger.trace("Entering createCustomer() ");
        customerRepository.persist(customer);
        logger.trace("Exiting createCustomer() with persisted customer ");
        return customer;
    }

    @Transactional
    public Customer updateCustomer( Customer updatedCustomer) {
        logger.trace("Entering updateCustomer() with updatedCustomer");
        Optional<Customer> existingCustomer = Optional.ofNullable(getCustomerById(updatedCustomer.getCustomerId()));
        if (existingCustomer.isPresent()) {
            Customer mergedCustomer = customerRepository.getEntityManager().merge(updatedCustomer);
            logger.trace("Exiting updateCustomer() with mergedCustomer" );
            return mergedCustomer;
        } else {
            String errorMessage = "Customer with ID " + updatedCustomer.getCustomerId() + " does not exist";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Transactional
    public boolean deleteCustomer( Long customerId) {
        logger.trace("Entering deleteCustomer() with customerId");
        boolean deleted = customerRepository.deleteById(customerId);
        logger.trace("Exiting deleteCustomer() with result");
        return deleted;
    }
}
