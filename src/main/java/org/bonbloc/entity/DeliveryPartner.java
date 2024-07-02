package org.bonbloc.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "delivery_partner")
@Getter
@Setter
public class DeliveryPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_partner_id")
    private Long deliveryPartnerId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "contact_info",nullable = false)
    private String contactInfo;

    public DeliveryPartner(){

    }
}
