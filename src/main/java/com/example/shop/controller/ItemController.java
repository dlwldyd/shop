package com.example.shop.controller;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.Dtos.item.ItemSearchDto;
import com.example.shop.domain.Item;
import com.example.shop.service.ItemImgService;
import com.example.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/registration")
    public String register(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/registration";
    }

    @PostMapping("/registration")
    public String newItem(@Validated @ModelAttribute ItemFormDto itemFormDto, BindingResult bindingResult) throws IOException {

        Tika tika = new Tika();

        if (itemFormDto.getItemRepImg().isEmpty()) {
            bindingResult.rejectValue("itemRepImg", "noFile");
        } else {
            if (!tika.detect(itemFormDto.getItemRepImg().getInputStream()).startsWith("image")) {
                bindingResult.rejectValue("itemRepImg", "extensionNotMatch");
            }
        }

        for (MultipartFile itemImg : itemFormDto.getItemImgs()) {
            if (!itemImg.isEmpty()) {
                if (!tika.detect(itemImg.getInputStream()).startsWith("image")) {
                    bindingResult.rejectValue("itemImgs", "extensionNotMatch");
                    break;
                }
            }
        }

        if (itemFormDto.getItemImgs().size() > 5) {
            bindingResult.rejectValue("itemImgs", "fileExceed");
        }

        if (bindingResult.hasErrors()) {
            return "item/registration";
        }

        try {
            itemService.saveItem(itemFormDto);
        } catch (IOException e) {
            bindingResult.reject("itemSaveError");
            return "item/registration";
        }
        return "redirect:/";
    }

    @GetMapping("/manage")
    public String itemManage(@ModelAttribute ItemSearchDto itemSearchDto,
                             @PageableDefault(size = 12) Pageable pageable,
                             Model model) {
        Page<ItemFormDto> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("maxPage", 5);
        return "item/manage";
    }

    @GetMapping("/edit/{itemId}")
    public String itemEdit(@PathVariable Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemData(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "item/edit";
    }
}