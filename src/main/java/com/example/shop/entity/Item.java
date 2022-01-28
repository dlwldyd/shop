package com.example.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name", nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(name = "stock_number", nullable = false)
    private int stockNumber;
}
