package com.example.shop.repository.item;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.Dtos.item.ItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<ItemFormDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
