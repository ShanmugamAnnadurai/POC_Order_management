package org.bonbloc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.Shipment;


@ApplicationScoped
public class ShipmentRepository implements PanacheRepository<Shipment> {
}
