package org.bonbloc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.SalesOrder;


@ApplicationScoped
public class SalesOrderRepository implements PanacheRepository<SalesOrder> {
}
