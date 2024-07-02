package org.bonbloc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.ProductStorageLocation;

import java.util.List;


@ApplicationScoped
public class ProductStorageLocationRepository implements PanacheRepository<ProductStorageLocation> {

}
