package com.example.shop.Dtos.item;

import com.example.shop.domain.ItemImg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String originImgName;

    private String imgUrl;

    private boolean repImg;

    public static ItemImgDto of(ItemImg itemImg) {
        return new ItemImgDto(itemImg.getId(),
                itemImg.getImgName(),
                itemImg.getOriginImgName(),
                itemImg.getImgUrl(),
                itemImg.isRepImg());
    }
}
