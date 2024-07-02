package org.bonbloc.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "customer_id")
     private Long customerId;

     @Column(name = "name",nullable = false)
     private String name ;

     @Column(name = "contact_info",nullable = false)
     private String contactInfo;

     @Column(name = "address",nullable = false)
     private String address;

     public Customer(){
     }




}
