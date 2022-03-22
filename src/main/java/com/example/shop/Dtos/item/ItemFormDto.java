package com.example.shop.Dtos.item;

import com.example.shop.domain.Item;
import com.example.shop.domain.ItemImg;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Min(0)
    private Integer price;

    @NotBlank
    private String itemDetails;

    @NotNull
    @Min(0)
    private Integer stockQuantity;

    @NotNull
    private ItemStatus status;

    @NotNull
    private ItemCategory category;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private ItemImgDto itemRepImgDto;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<MultipartFile> itemImgs = new ArrayList<>();

    private MultipartFile itemRepImg;

    public ItemFormDto(String itemName, Integer price, String itemDetails, Integer stockQuantity, ItemStatus status, ItemCategory category) {
        this.itemName = itemName;
        this.price = price;
        this.itemDetails = itemDetails;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.category = category;
    }

    public ItemFormDto(Long id, String itemName, Integer price, String itemDetails, Integer stockQuantity, ItemStatus status, ItemCategory category) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.itemDetails = itemDetails;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.category = category;
    }

    public ItemFormDto(Long id, String itemName, Integer price, String itemDetails, Integer stockQuantity, ItemStatus status, ItemCategory category, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.itemDetails = itemDetails;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.category = category;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * ItemFormDto 객체를 Item 객체로 변환하는 메서드
     * @return 변환된 Item 객체
     */
    public Item createItem() {
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

    /**
     * Item 객체를 ItemFormDto 객체로 변환하는 메서드,
     * createdDate, lastModifiedDate 도 포함됨
     * @param item 변환할 Item 객체
     * @return 변환된 ItemFormDto 객체
     */
    public static ItemFormDto adminDtoOf(Item item) {
        ItemFormDto itemFormDto = new ItemFormDto(item.getId(),
                item.getItemName(),
                item.getPrice(),
                item.getItemDetail(),
                item.getStockQuantity(),
                item.getStatus(),
                item.getCategory(),
                item.getCreatedDate(),
                item.getLastModifiedDate());

        List<ItemImg> itemImgList = item.getItemImgList();
        for (ItemImg itemImg : itemImgList) {
            if (itemImg.isRepImg()) {
                itemFormDto.setItemRepImgDto(ItemImgDto.of(itemImg));
            } else {
                itemFormDto.getItemImgDtoList().add(ItemImgDto.of(itemImg));
            }
        }

        return itemFormDto;
    }

    /**
     * Item 객체를 ItemFormDto 객체로 변환하는 메서드,
     * createdDate, lastModifiedDate 는 제외시킴
     * @param item 변환할 Item 객체
     * @return 변환된 ItemFormDto 객체
     */
    public static ItemFormDto of(Item item) {
        ItemFormDto itemFormDto = new ItemFormDto(item.getId(),
                item.getItemName(),
                item.getPrice(),
                item.getItemDetail(),
                item.getStockQuantity(),
                item.getStatus(),
                item.getCategory());

        List<ItemImg> itemImgList = item.getItemImgList();
        for (ItemImg itemImg : itemImgList) {
            if (itemImg.isRepImg()) {
                itemFormDto.setItemRepImgDto(ItemImgDto.of(itemImg));
            } else {
                itemFormDto.getItemImgDtoList().add(ItemImgDto.of(itemImg));
            }
        }

        return itemFormDto;
    }
}
