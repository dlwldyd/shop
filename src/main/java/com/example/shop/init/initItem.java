package com.example.shop.init;

import com.example.shop.domain.Item;
import com.example.shop.domain.ItemImg;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import com.example.shop.repository.item.ItemRepository;
import com.example.shop.repository.itemimg.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
@RequiredArgsConstructor
public class initItem {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    @PostConstruct
    public void init() {
        Integer stockQuantity = 100;
        Integer price = 10000;
        String itemDetails = "상품 상세 설명";
        for (int i = 0; i < 50; i++) {
            Item item = new Item("이름1", price, stockQuantity, ItemStatus.SOLD_OUT, ItemCategory.LAPTOP, itemDetails);
            itemRepository.save(item);
            ItemImg itemImg = new ItemImg("이미지", "이미지", "/item/images/aaa.jpg", true, item);
            ItemImg itemImg2 = new ItemImg("이미지", "이미지", "/item/images/aaa.jpg", false, item);
            itemImgRepository.save(itemImg);
        }
        for (int i = 0; i < 50; i++) {
            Item item = new Item("이름2", price, stockQuantity, ItemStatus.SELL, ItemCategory.KEYBOARD, itemDetails);
            itemRepository.save(item);
            ItemImg itemImg = new ItemImg("이미지", "이미지", "/item/images/aaa.jpg", true, item);
            ItemImg itemImg2 = new ItemImg("이미지", "이미지", "/item/images/aaa.jpg", false, item);
            itemImgRepository.save(itemImg);
        }
    }
}
