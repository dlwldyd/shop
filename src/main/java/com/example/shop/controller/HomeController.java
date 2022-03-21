package com.example.shop.controller;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model) {
        List<ItemFormDto> itemList = itemService.getItemList();
        model.addAttribute("items", itemList);
        return "home";
    }
}
