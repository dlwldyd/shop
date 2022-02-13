package com.example.shop.domain.entity;

import com.example.shop.domain.entity.baseentity.DateBaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class OrderItem extends DateBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;
}
