package com.example.shop.Dtos.item;

import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private ItemStatus itemStatus;

    private ItemCategory itemCategory;

    private String itemName;
}
