package org.bonbloc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    @Column(name = "size_of_product(in m³)")
    private Integer sizeOfProduct;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ProductStorageLocation> productStorageLocation;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public Product(){}
}
