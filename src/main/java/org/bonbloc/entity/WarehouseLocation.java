package org.bonbloc.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "warehouse_location")
@Getter
@Setter
public class WarehouseLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_location_id")
    private Long warehouseLocationId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "location",nullable = false)
    private String location;

    @Column(name = "warehouse_manager",nullable = false)
    private String warehouseManager;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<StorageLocation> storageLocations;

    public WarehouseLocation(){}
}
