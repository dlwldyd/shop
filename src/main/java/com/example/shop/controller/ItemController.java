package com.example.shop.controller;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/registration")
    public String register(Model model) {
        model.addAttribute("item", new ItemFormDto());
        return "item/registration";
    }

    @PostMapping("/registration")
    public String newItem(@Validated @ModelAttribute("item") ItemFormDto itemFormDto, BindingResult bindingResult) throws IOException {

        List<String> imgExtension = List.of(".jpg", ".jpeg", ".gif", ".png");
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

    String extractExt(String originalFileName) {
        int index = originalFileName.lastIndexOf(".");
        return originalFileName.substring(index);
    }
}
