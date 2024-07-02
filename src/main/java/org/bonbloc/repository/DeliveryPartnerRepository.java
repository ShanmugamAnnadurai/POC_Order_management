package org.bonbloc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.DeliveryPartner;

@ApplicationScoped
public class DeliveryPartnerRepository implements PanacheRepository<DeliveryPartner> {
}
