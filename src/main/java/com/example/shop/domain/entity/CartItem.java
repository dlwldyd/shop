package com.example.shop.domain.entity;

import com.example.shop.domain.entity.baseentity.DateBaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class CartItem extends DateBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;
}
