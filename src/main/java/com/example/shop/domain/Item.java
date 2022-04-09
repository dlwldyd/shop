package com.example.shop.domain;

import com.example.shop.Dtos.item.AdminItemFormDto;
import com.example.shop.domain.baseentity.DateBaseEntity;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import com.example.shop.exception.OutOfStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ItemImg> itemImgList = new ArrayList<>();

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Item(String itemName, int price, int stockQuantity, ItemStatus status, ItemCategory category, String itemDetail) {
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.itemDetail = itemDetail;
        this.category = category;
    }

    public void updateItem(AdminItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockQuantity = itemFormDto.getStockQuantity();
        this.status = itemFormDto.getStatus();
        this.category = itemFormDto.getCategory();
        this.itemDetail = itemFormDto.getItemDetails();
    }

    /**
     * 주문한 개수만큼 재고를 감소시키는 메서드,
     * 재고 이상으로 주문시 예외를 발생시킴
     * @param orderNumber 주문한 상품의 개수
     */
    public void removeStock(int orderNumber) {
        int restStock = this.stockQuantity - orderNumber;
        if (restStock < 0) {
            throw new OutOfStockException("재고가 부족합니다.(현재 재고량 : " + this.stockQuantity + ")");
        } else if (restStock == 0) {
            this.status = ItemStatus.SOLD_OUT;
        }
        this.stockQuantity = restStock;
    }

    public void addStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }
}
