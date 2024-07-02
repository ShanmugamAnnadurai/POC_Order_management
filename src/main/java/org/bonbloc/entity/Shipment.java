package org.bonbloc.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "shipment")
@Getter
@Setter
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @Column(name = "shipment_date", updatable = false,nullable = false)
    private Instant shipmentDate;

    @Column(name = "arrival_date")
    private Instant arrivalDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_partner_id")
    private DeliveryPartner deliveryPartner;

    private String status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_id",updatable = false)
    private SalesOrder salesOrder;
    public Shipment(){
    }
}
