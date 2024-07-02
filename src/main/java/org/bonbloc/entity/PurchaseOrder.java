package org.bonbloc.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "inventory_transactions")
@Getter
@Setter
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_transaction_id")
    private Long inventoryTransactionId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "inventoryTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductStorageLocation> productStorageLocations;

    public PurchaseOrder(){}
}
