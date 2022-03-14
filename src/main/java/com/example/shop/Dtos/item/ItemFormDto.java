package com.example.shop.Dtos.item;

import com.example.shop.domain.entity.Item;
import com.example.shop.domain.enumtype.ItemStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private Integer price;

    @NotBlank
    private String itemDetails;

    @NotNull
    private Integer stockQuantity;

    @NotNull
    private ItemStatus status;

    private List<MultipartFile> itemImgs = new ArrayList<>();

    private MultipartFile itemRepImg;

    public ItemFormDto(Long id, String itemName, Integer price, String itemDetails, Integer stockQuantity, ItemStatus status) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.itemDetails = itemDetails;
        this.stockQuantity = stockQuantity;
        this.status = status;
    }

    public Item createItem() {
        return new Item(this.getItemName(),
                this.getPrice(),
                this.getStockQuantity(),
                this.getStatus(),
                this.getItemDetails());
    }

    public static ItemFormDto convertItemFormDtoToItem(Item item) {
        return new ItemFormDto(item.getId(),
                item.getItemName(),
                item.getPrice(),
                item.getItemDetail(),
                item.getStockQuantity(),
                item.getStatus());
    }
}
