package org.bonbloc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.PurchaseOrder;


@ApplicationScoped
public class PurchaseOrderRepository implements PanacheRepository<PurchaseOrder> {
}
