package org.bonbloc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.Supplier;

@ApplicationScoped
public class SupplierRepository implements PanacheRepository<Supplier> {
}
