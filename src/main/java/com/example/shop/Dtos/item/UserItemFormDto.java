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
public class UserItemFormDto extends ItemFormDto {

    public UserItemFormDto(String itemName, Integer price, String itemDetails, Integer stockQuantity, ItemStatus status, ItemCategory category) {
        super(itemName, price, itemDetails, stockQuantity, status, category);
    }

    public UserItemFormDto(Long id, String itemName, Integer price, String itemDetails, Integer stockQuantity, ItemStatus status, ItemCategory category) {
        super(id, itemName, price, itemDetails, stockQuantity, status, category);
    }

    /**
     * Item 객체를 ItemFormDto 객체로 변환하는 메서드,
     * createdDate, lastModifiedDate 는 제외시킴
     * @param item 변환할 Item 객체
     * @return 변환된 ItemFormDto 객체
     */
    public static UserItemFormDto of(Item item) {
        UserItemFormDto itemFormDto = new UserItemFormDto(item.getId(),
                item.getItemName(),
                item.getPrice(),
                item.getItemDetail(),
                item.getStockQuantity(),
                item.getStatus(),
                item.getCategory());

        List<ItemImg> itemImgList = item.getItemImgList();
        classifyRepImg(itemFormDto, itemImgList);
        return itemFormDto;
    }
}
