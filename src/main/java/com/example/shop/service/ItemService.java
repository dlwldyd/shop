package com.example.shop.service;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.domain.entity.Item;
import com.example.shop.domain.entity.ItemImg;
import com.example.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;

    @Transactional
    public Item saveItem(ItemFormDto itemFormDto) throws IOException {

        Item savedItem = itemRepository.save(itemFormDto.createItem());

        MultipartFile itemRepImgFile = itemFormDto.getItemRepImg();
        String originImgName = itemRepImgFile.getOriginalFilename();
        String imgName = createStoreFileName(originImgName);
        String imgUrl = "/images/item/" + imgName;

        ItemImg itemRepImg = new ItemImg(imgName, originImgName, imgUrl, true, savedItem);

        itemImgService.saveItemImg(itemRepImg, itemRepImgFile);

        for (MultipartFile itemImgFile : itemFormDto.getItemImgs()) {
            String oriImgName = itemImgFile.getOriginalFilename();
            String imageName = createStoreFileName(oriImgName);
            String imageUrl = "/images/item/" + imageName;

            ItemImg itemImg = new ItemImg(imageName, oriImgName, imageUrl, false, savedItem);

            itemImgService.saveItemImg(itemImg, itemImgFile);
        }
        return savedItem;
    }

    /**
     * 서버에 저장할 때 사용할 파일이름을 뽑아내는 함수
     * @param originalFilename 업로드 될 때의 파일 이름
     * @return 서버에 저장할 때 사용할 파일이름
     */
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);

        return uuid + ext;
    }

    /**
     * 확장자만 뽑아내는 합수
     * @param originalFilename 이미지 파일 이름
     * @return 확장자
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos);
    }
}
