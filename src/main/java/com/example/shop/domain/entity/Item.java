package com.example.shop.domain.entity;

import com.example.shop.domain.entity.baseentity.DateBaseEntity;
import com.example.shop.domain.enumtype.ItemStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends DateBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    public Item(String itemName, int price, int stockQuantity, ItemStatus status, String itemDetail) {
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.itemDetail = itemDetail;
    }
}
