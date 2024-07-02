package org.bonbloc.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bonbloc.entity.Product;


@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
}
