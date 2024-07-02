package org.bonbloc.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "storage_location")
@Getter
@Setter
public class StorageLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_location_id")
    private Long storageLocationId;

    @Column(name = "name")
    private String name;

    @Column(name = "capacity(in m³)")
    private Integer capacity;

    @Column(name = "occupied_space(in m³)")
    private Integer occupiedSpace;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    @JsonBackReference
    private WarehouseLocation warehouse;

    public StorageLocation(){}

}
