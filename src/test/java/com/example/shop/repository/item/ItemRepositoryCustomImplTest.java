package com.example.shop.repository.item;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.Dtos.item.ItemSearchDto;
import com.example.shop.domain.Item;
import com.example.shop.domain.ItemImg;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import com.example.shop.repository.itemimg.ItemImgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class ItemRepositoryCustomImplTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemImgRepository itemImgRepository;

    @BeforeEach
    void initItem() {

        Integer stockQuantity = 100;
        Integer price = 10000;
        String itemDetails = "상품 상세 설명";
        for (int i = 0; i < 10; i++) {
            Item item = new Item("이름1", price, stockQuantity, ItemStatus.SOLD_OUT, ItemCategory.LAPTOP, itemDetails);
            itemRepository.save(item);
            ItemImg itemImg = new ItemImg("이미지", "이미지", "/item/images/aaa.jpg", true, item);
            ItemImg itemImg2 = new ItemImg("이미지", "이미지", "/item/images/aaa.jpg", false, item);
            itemImgRepository.save(itemImg);
        }
        for (int i = 0; i < 10; i++) {
            Item item = new Item("이름2", price, stockQuantity, ItemStatus.SELL, ItemCategory.KEYBOARD, itemDetails);
            itemRepository.save(item);
            ItemImg itemImg = new ItemImg("이미지", "이미지", "/item/images/aaa.jpg", true, item);
            ItemImg itemImg2 = new ItemImg("이미지", "이미지", "/item/images/aaa.jpg", false, item);
            itemImgRepository.save(itemImg);
        }
    }

    @Test
    void searchTest() {
        ItemSearchDto itemSearchDtoSell = new ItemSearchDto();
        itemSearchDtoSell.setItemStatus(ItemStatus.SELL);
        ItemSearchDto itemSearchDtoSoldOut = new ItemSearchDto();
        itemSearchDtoSoldOut.setItemStatus(ItemStatus.SOLD_OUT);
        ItemSearchDto itemSearchDtoAll = new ItemSearchDto();
        ItemSearchDto itemSearchDtoName = new ItemSearchDto();
        itemSearchDtoName.setItemName("이름1");
        Pageable pageable = PageRequest.of(1, 8);

        Page<ItemFormDto> itemPageSell = itemRepository.getAdminItemPage(itemSearchDtoSell, pageable);
        Page<ItemFormDto> itemPageAll = itemRepository.getAdminItemPage(itemSearchDtoAll, pageable);
        Page<ItemFormDto> itemPageSoldOut = itemRepository.getAdminItemPage(itemSearchDtoSoldOut, pageable);
        Page<ItemFormDto> itemPageName = itemRepository.getAdminItemPage(itemSearchDtoName, pageable);

        assertThat(itemPageSell.getContent().size()).isEqualTo(2);
        assertThat(itemPageSoldOut.getContent().size()).isEqualTo(2);
        assertThat(itemPageAll.getContent().size()).isEqualTo(8);
        assertThat(itemPageName.getContent().size()).isEqualTo(2);

        assertThat(itemPageSell.getTotalPages()).isEqualTo(2);
        assertThat(itemPageSoldOut.getTotalPages()).isEqualTo(2);
        assertThat(itemPageAll.getTotalPages()).isEqualTo(3);
        assertThat(itemPageName.getTotalPages()).isEqualTo(2);
    }
}