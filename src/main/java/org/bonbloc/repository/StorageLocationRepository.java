package org.bonbloc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.StorageLocation;


@ApplicationScoped
public class StorageLocationRepository implements PanacheRepository<StorageLocation> {
}
