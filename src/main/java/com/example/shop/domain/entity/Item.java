package com.example.shop.domain.entity;

import com.example.shop.domain.entity.baseentity.DateEntity;
import com.example.shop.domain.enumtype.ItemStatus;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Item extends DateEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name", nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Lob
    @Column(nullable = false)
    private String itemDetail;
}
