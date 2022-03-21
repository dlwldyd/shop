package com.example.shop.service;

import com.example.shop.Dtos.item.ItemFormDto;
import com.example.shop.Dtos.item.ItemImgDto;
import com.example.shop.Dtos.item.ItemSearchDto;
import com.example.shop.domain.Item;
import com.example.shop.domain.ItemImg;
import com.example.shop.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        String imgUrl = "/images/" + imgName;

        ItemImg itemRepImg = new ItemImg(imgName, originImgName, imgUrl, true, savedItem);

        itemImgService.saveItemImg(itemRepImg, itemRepImgFile);

        for (MultipartFile itemImgFile : itemFormDto.getItemImgs()) {
            if (!itemImgFile.isEmpty()) {
                String oriImgName = itemImgFile.getOriginalFilename();
                String imageName = createStoreFileName(oriImgName);
                String imageUrl = "/images/" + imageName;

                ItemImg itemImg = new ItemImg(imageName, oriImgName, imageUrl, false, savedItem);

                itemImgService.saveItemImg(itemImg, itemImgFile);
            }
        }
        return savedItem;
    }

    @Transactional
    public void deleteItem(Long itemId) {
        itemImgService.deleteAllItemImg(itemId);
        itemRepository.deleteById(itemId);
    }

    @Transactional
    public Item updateItem(ItemFormDto itemFormDto) throws IOException {

        Optional<Item> item = itemRepository.findById(itemFormDto.getId());

        if (item.isPresent()) {

            if (!itemFormDto.getItemRepImg().isEmpty()) {

                MultipartFile itemRepImgFile = itemFormDto.getItemRepImg();
                String originImgName = itemRepImgFile.getOriginalFilename();
                String imgName = createStoreFileName(originImgName);
                String imgUrl = "/images/" + imgName;

                ItemImg itemImg = new ItemImg(imgName, originImgName, imgUrl, true, item.get());
                itemImgService.updateItemRepImg(itemImg, itemFormDto.getItemRepImg());
            }
            if (itemFormDto.getItemImgs().size() != 1 || !itemFormDto.getItemImgs().get(0).isEmpty()) {

                itemImgService.deleteNonItemRepImg(itemFormDto.getId());
                for (MultipartFile itemImgFile : itemFormDto.getItemImgs()) {

                    String originImgName = itemImgFile.getOriginalFilename();
                    String imgName = createStoreFileName(originImgName);
                    String imgUrl = "/images/" + imgName;

                    ItemImg itemImg = new ItemImg(imgName, originImgName, imgUrl, false, item.get());

                    itemImgService.saveItemImg(itemImg, itemImgFile);
                }
            }

            item.get().updateItem(itemFormDto);

            return item.get();
        }

        throw new EntityNotFoundException();
    }

    public Page<ItemFormDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    public Page<ItemFormDto> getItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getItemPage(itemSearchDto, pageable);
    }

    public List<ItemFormDto> getItemList() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Item> itemList = itemRepository.getItemList(pageable);

        return itemList.stream().map(ItemFormDto::of).collect(Collectors.toList());
    }

    public ItemFormDto getAdminItemData(Long itemId) {

        Optional<Item> findItem = itemRepository.findById(itemId);

        if (findItem.isPresent()) {
            ItemImg itemRepImg = itemImgService.getItemRepImg(itemId);
            List<ItemImg> itemImgList = itemImgService.getItemImgList(itemId);

            List<ItemImgDto> itemImgDtoList = new ArrayList<>();

            for (ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
                itemImgDtoList.add(itemImgDto);
            }

            ItemFormDto itemFormDto = ItemFormDto.adminDtoOf(findItem.get());
            itemFormDto.setItemRepImgDto(ItemImgDto.of(itemRepImg));
            itemFormDto.setItemImgDtoList(itemImgDtoList);
            return itemFormDto;
        }else{
            throw new EntityNotFoundException();
        }
    }

    public ItemFormDto getItemData(Long itemId) {

        Optional<Item> findItem = itemRepository.findById(itemId);

        if (findItem.isPresent()) {
            ItemImg itemRepImg = itemImgService.getItemRepImg(itemId);
            List<ItemImg> itemImgList = itemImgService.getItemImgList(itemId);

            List<ItemImgDto> itemImgDtoList = new ArrayList<>();

            for (ItemImg itemImg : itemImgList) {
                ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
                itemImgDtoList.add(itemImgDto);
            }

            ItemFormDto itemFormDto = ItemFormDto.of(findItem.get());
            itemFormDto.setItemRepImgDto(ItemImgDto.of(itemRepImg));
            itemFormDto.setItemImgDtoList(itemImgDtoList);
            return itemFormDto;
        }else{
            throw new EntityNotFoundException();
        }
    }

    public Item getItem(Long itemId) {

        return itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("그런 상품이 없습니다."));
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
