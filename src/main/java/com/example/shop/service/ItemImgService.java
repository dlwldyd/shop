package com.example.shop.service;

import com.example.shop.domain.ItemImg;
import com.example.shop.repository.itemimg.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemImgService {

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    @Transactional
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException {

        String imgFullPathName = itemImgLocation + "/" + itemImg.getImgName();
        itemImgRepository.save(itemImg);
        fileService.uploadFile(imgFullPathName, itemImgFile);
    }

    public ItemImg getItemRepImg(Long itemId) {
        return itemImgRepository.findItemRepImg(itemId);
    }

    public List<ItemImg> getItemImgList(Long itemId) {
        return itemImgRepository.findItemImg(itemId);
    }

}
