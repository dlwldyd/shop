package com.example.shop.service;

import com.example.shop.Dtos.item.*;
import com.example.shop.domain.Item;
import com.example.shop.domain.ItemImg;
import com.example.shop.enumtype.ItemStatus;
import com.example.shop.exception.DeletedItemException;
import com.example.shop.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    /**
     * 데이터베이스에 상품을 저장,
     * Item 엔티티, ItemImg 엔티티, 이미지 파일을 저장한다.
     * @param itemFormDto 저장할 상품에 대한 정보가 담겨있는 DTO
     * @return 데이터베이스에 저장된 상품을 반환
     */
    @Transactional
    public Item saveItem(AdminItemFormDto itemFormDto) throws IOException {

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

    /**
     * 상품을 데이터베이스에서 삭제함
     * @param itemId 살제할 상품의 id
     */
    @Transactional
    public void deleteItem(Long itemId) {
        itemImgService.deleteNonItemRepImg(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        item.setStatus(ItemStatus.DELETED);
    }

    /**
     * 데이터베이스에 저장된 상품을 수정함,
     * Item 엔티티, ItemImg 엔티티, 이미지 파일을 수정한다.
     * itemFormDto 에 이미지 파일이 담겨 있지 않다면 ItemImg 엔티티와 이미지 파일은 수정되지 않음
     * @param itemFormDto 수정할 상품에 대한 정보가 담긴 DTO
     * @return 수정된 상품 엔티티가 반환
     */
    @Transactional
    public Item updateItem(AdminItemFormDto itemFormDto) throws IOException {

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

    /**
     * 검색 조건에 맞는 상품 정보와 페이징 정보를 담은 Page 객체를 반환함,
     * createdDate, lastModifiedDate 정보도 담김
     * @param itemSearchDto 검색 조건
     * @param pageable 페이징
     */
    public Page<AdminItemFormDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    /**
     * 검색 조건에 맞는 상품 정보와 페이징 정보를 담은 Page 객체를 반환함,
     * createdDate, lastModifiedDate 정보는 제외됨
     * @param itemSearchDto 검색 조건
     * @param pageable 페이징
     */
    public Page<UserItemFormDto> getItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getItemPage(itemSearchDto, pageable);
    }

    /**
     * 홈 화면에서 상품을 표시하기 위한 용도,
     * 3개의 상품에 대한 정보가 있는 List 를 반환함
     */
    public List<UserItemFormDto> getItemList() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Item> itemList = itemRepository.getItemList(pageable);

        return itemList.stream().map(UserItemFormDto::of).collect(Collectors.toList());
    }

    /**
     * 특정 상품의 정보를 가져오는 메서드,
     * createdDate, lastModifiedDate 포함시킴
     * @param itemId 상품 id
     * @return 상품에 대한 정보를 담은 ItemFormDto
     */
    public AdminItemFormDto getAdminItemData(Long itemId) {

        Item findItem = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        ItemImg itemRepImg = itemImgService.getItemRepImg(itemId);
        List<ItemImgDto> itemImgDtoList = getItemNonRepImg(itemId);

        AdminItemFormDto itemFormDto = AdminItemFormDto.of(findItem);
        itemFormDto.setItemRepImgDto(ItemImgDto.of(itemRepImg));
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    /**
     * 특정 상품의 정보를 가져오는 메서드,
     * createdDate, lastModifiedDate 제외
     * @param itemId 상품 id
     * @return 상품에 대한 정보를 담은 ItemFormDto
     */
    public UserItemFormDto getItemData(Long itemId) {

        Item findItem = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        if (findItem.getStatus() == ItemStatus.DELETED) {
            throw new DeletedItemException("삭제된 상품입니다.");
        }

        ItemImg itemRepImg = itemImgService.getItemRepImg(itemId);
        List<ItemImgDto> itemImgDtoList = getItemNonRepImg(itemId);

        UserItemFormDto itemFormDto = UserItemFormDto.of(findItem);
        itemFormDto.setItemRepImgDto(ItemImgDto.of(itemRepImg));
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
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
     * 상품 대표 이미지를 제외한 상품이미지를 넣은 리스트 반환
     * @param itemId 상품 아이디
     */
    private List<ItemImgDto> getItemNonRepImg(Long itemId) {
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        List<ItemImg> itemImgList = itemImgService.getItemImgList(itemId);

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        return itemImgDtoList;
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
