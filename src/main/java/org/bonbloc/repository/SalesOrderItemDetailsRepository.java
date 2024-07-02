package org.bonbloc.repository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.SalesOrderItemDetails;

@ApplicationScoped
public class SalesOrderItemDetailsRepository implements PanacheRepository<SalesOrderItemDetails> {
}