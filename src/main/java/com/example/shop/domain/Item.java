package com.example.shop.domain;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.domain.baseentity.DateBaseEntity;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    public Item(String itemName, int price, int stockQuantity, ItemStatus status, ItemCategory category, String itemDetail) {
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.itemDetail = itemDetail;
        this.category = category;
    }

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockQuantity = itemFormDto.getStockQuantity();
        this.status = itemFormDto.getStatus();
        this.category = itemFormDto.getCategory();
        this.itemDetail = itemFormDto.getItemDetails();
//        if (!itemFormDto.getItemRepImg().isEmpty()) {
//            this.itemImgList.removeIf(ItemImg::isRepImg);
//        }
//        if (itemFormDto.getItemImgs().size() != 1 || !itemFormDto.getItemImgs().get(0).isEmpty()) {
//            this.itemImgList.removeIf(itemImg -> !itemImg.isRepImg());
//        }
    }
}
