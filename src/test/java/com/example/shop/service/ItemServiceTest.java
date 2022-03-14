package com.example.shop.service;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.domain.entity.Item;
import com.example.shop.domain.entity.ItemImg;
import com.example.shop.domain.enumtype.ItemStatus;
import com.example.shop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.shop.MakeMockMultipartFile.getMockMultipartFile;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemImgService itemImgService;

    @Test
    void itemServiceTest() throws IOException {
        String itemName = "아이템";
        Integer price = 10000;
        Integer stockQuantity = 100;
        String itemDetails = "아이템";
        ItemStatus status = ItemStatus.SELL;
        ItemFormDto itemFormDto = new ItemFormDto(null, itemName, price, itemDetails, stockQuantity, status);
        String fileName = "test";
        String contentType = "jpg";
        String path = "C:/shop/item/tikaTestFile.jpg";
        MockMultipartFile multipartFile = getMockMultipartFile(fileName, contentType, path);
        itemFormDto.setItemRepImg(multipartFile);
        Item saveItem = itemFormDto.createItem();

        when(itemRepository.save(any(Item.class))).thenReturn(saveItem);

        //void 를 반환하는 메서드는 doNothing() 활용
        doNothing().when(itemImgService).saveItemImg(any(ItemImg.class), any(MultipartFile.class));

        Item savedItem = itemService.saveItem(itemFormDto);


        assertThat(savedItem).isEqualTo(saveItem);
    }
}