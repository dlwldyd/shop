package com.example.shop.Dtos.item;

import com.example.shop.domain.Item;
import com.example.shop.domain.ItemImg;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public abstract class ItemFormDto {

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

    protected static void classifyRepImg(ItemFormDto itemFormDto, List<ItemImg> itemImgList) {
        for (ItemImg itemImg : itemImgList) {
            if (itemImg.isRepImg()) {
                itemFormDto.setItemRepImgDto(ItemImgDto.of(itemImg));
            } else {
                itemFormDto.getItemImgDtoList().add(ItemImgDto.of(itemImg));
            }
        }
    }
}
