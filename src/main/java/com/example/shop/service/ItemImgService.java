package com.example.shop.service;

import com.example.shop.Dtos.item.ItemImgDto;
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

        String imgFullPathName = imgFullPath(itemImg.getImgName());
        itemImgRepository.save(itemImg);
        fileService.uploadFile(imgFullPathName, itemImgFile);
    }

    @Transactional
    public void deleteAllItemImg(Long itemId) {
        List<ItemImg> findItemImg = itemImgRepository.findItemImgByItemId(itemId);
        findItemImg.forEach(itemImg -> fileService.deleteFile(imgFullPath(itemImg.getImgName())));
        itemImgRepository.deleteItemImgByItemId(itemId);
    }

    @Transactional
    public void deleteNonItemRepImg(Long itemId) {
        List<ItemImg> findItemImg = itemImgRepository.findNonItemRepImgByItemId(itemId);
        findItemImg.forEach(itemImg -> fileService.deleteFile(imgFullPath(itemImg.getImgName())));
        itemImgRepository.deleteNonItemImgByItemId(itemId);
    }

    @Transactional
    public void updateItemRepImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException {
        ItemImg itemRepImg = itemImgRepository.findItemRepImgByItemId(itemImg.getItem().getId());
        fileService.deleteFile(imgFullPath(itemRepImg.getImgName()));
        fileService.uploadFile(imgFullPath(itemImg.getImgName()), itemImgFile);

        itemImgRepository.deleteItemRepImgByItemId(itemImg.getItem().getId());
        itemImgRepository.save(itemImg);
    }

    public ItemImg getItemRepImg(Long itemId) {
        return itemImgRepository.findItemRepImgByItemId(itemId);
    }

    public List<ItemImg> getItemImgList(Long itemId) {
        return itemImgRepository.findNonItemRepImgByItemId(itemId);
    }

    public String imgFullPath(String imgName) {
        return itemImgLocation + "/" + imgName;
    }
}
