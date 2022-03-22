package com.example.shop.controller;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.Dtos.item.ItemSearchDto;
import com.example.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ItemService itemService;

    /**
     * SHOP 화면
     */
    @GetMapping("/shop")
    public String shop(@ModelAttribute ItemSearchDto itemSearchDto,
                       @PageableDefault(size = 12) Pageable pageable,
                       Model model) {
        Page<ItemFormDto> items = itemService.getItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("maxPage", 5);
        return "shop/main";
    }

    /**
     * 상품 상세 화면
     */
    @GetMapping("/shop/{itemId}")
    public String singleItem(@PathVariable Long itemId, Model model) {
        ItemFormDto itemFormDto = itemService.getItemData(itemId);
        model.addAttribute("itemFormDto", itemFormDto);
        return "shop/single";
    }
}