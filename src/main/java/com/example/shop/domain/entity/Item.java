package com.example.shop.domain.entity;

import com.example.shop.domain.enumtype.ItemStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
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

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private LocalDateTime registerTime;

    private LocalDateTime updateTime;
}
