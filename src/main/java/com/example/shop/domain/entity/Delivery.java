package com.example.shop.domain.entity;

import com.example.shop.domain.embeddable.Address;
import com.example.shop.domain.entity.baseentity.DateEntity;
import com.example.shop.domain.enumtype.DeliveryStatus;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Delivery extends DateEntity {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
}
