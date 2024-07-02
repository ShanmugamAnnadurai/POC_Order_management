package org.bonbloc.repository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.WarehouseLocation;

@ApplicationScoped
public class WarehouseLocationRepository implements PanacheRepository<WarehouseLocation> {
}
