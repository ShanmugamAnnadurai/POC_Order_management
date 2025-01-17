package org.bonbloc.repository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.Customer;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {
}
