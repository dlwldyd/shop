package com.example.shop.Dtos.item;

import com.example.shop.domain.Item;
import com.example.shop.domain.ItemImg;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminItemFormDto extends UserItemFormDto {
    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public AdminItemFormDto(String itemName, Integer price, String itemDetails, Integer stockQuantity, ItemStatus status, ItemCategory category) {
        super(itemName, price, itemDetails, stockQuantity, status, category);
    }

    public AdminItemFormDto(Long id, String itemName, Integer price, String itemDetails, Integer stockQuantity, ItemStatus status, ItemCategory category, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        super(id, itemName, price, itemDetails, stockQuantity, status, category);
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Item 객체를 ItemFormDto 객체로 변환하는 메서드,
     * createdDate, lastModifiedDate 도 포함됨
     * @param item 변환할 Item 객체
     * @return 변환된 ItemFormDto 객체
     */
    public static AdminItemFormDto of(Item item) {
        AdminItemFormDto itemFormDto = new AdminItemFormDto(item.getId(),
                item.getItemName(),
                item.getPrice(),
                item.getItemDetail(),
                item.getStockQuantity(),
                item.getStatus(),
                item.getCategory(),
                item.getCreatedDate(),
                item.getLastModifiedDate());

        List<ItemImg> itemImgList = item.getItemImgList();
        classifyRepImg(itemFormDto, itemImgList);
        return itemFormDto;
    }

    /**
     * ItemFormDto 객체를 Item 객체로 변환하는 메서드
     * @return 변환된 Item 객체
     */
    public final Item createItem() {
        Item item = new Item(this.getItemName(),
                this.getPrice(),
                this.getStockQuantity(),
                this.getStatus(),
                this.getCategory(),
                this.getItemDetails());

        if (item.getStockQuantity() == 0) {
            item.setStatus(ItemStatus.SOLD_OUT);
        }
        return item;
    }
}
