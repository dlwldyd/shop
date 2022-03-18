package com.example.shop.domain;

import com.example.shop.domain.baseentity.DateBaseEntity;
import com.example.shop.enumtype.DeliveryStatus;
import lombok.Getter;

import javax.persistence.*;

//@Entity
@Getter
public class Delivery extends DateBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private String address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
}
