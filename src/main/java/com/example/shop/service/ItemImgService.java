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

    /**
     * ItemImg 엔티티를 생성 후 저장하고 이미지 파일을 저장함
     * @param itemImg 저장할 ItemImg 엔티티
     * @param itemImgFile 저장할 이미지 파일
     */
    @Transactional
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException {

        String imgFullPathName = imgFullPath(itemImg.getImgName());
        itemImgRepository.save(itemImg);
        fileService.uploadFile(imgFullPathName, itemImgFile);
    }

    /**
     * 상품의 모든 ItemImg 엔티티와 이미지 파일을 삭제함
     * @param itemId 삭제할 상품의 id
     */
    @Transactional
    public void deleteAllItemImg(Long itemId) {
        List<ItemImg> findItemImg = itemImgRepository.findItemImgByItemId(itemId);
        findItemImg.forEach(itemImg -> fileService.deleteFile(imgFullPath(itemImg.getImgName())));
        itemImgRepository.deleteItemImgByItemId(itemId);
    }

    /**
     * 상품 대표 이미지를 제외한 모든 ItemImg 엔티티를 삭제하고
     * 상품 대표 이미지를 제외한 모든 이미지 파일을 삭제함
     * @param itemId 삭제할 상품의 id
     */
    @Transactional
    public void deleteNonItemRepImg(Long itemId) {
        List<ItemImg> findItemImg = itemImgRepository.findNonItemRepImgByItemId(itemId);
        findItemImg.forEach(itemImg -> fileService.deleteFile(imgFullPath(itemImg.getImgName())));
        itemImgRepository.deleteNonItemImgByItemId(itemId);
    }

    /**
     * 상품 대표 이미지를 업로드한 상품 대표 이미지로 대체함,
     * ItemImg 엔티티의 경우 update 쿼리를 보내면 createdDate 가 수정되지 않기 때문에
     * delete 쿼리 후 insert 쿼리를 보냄
     * @param itemImg 업데이트할 ItemImg 엔티티
     * @param itemImgFile 새로 대체할 상품 대표 이미지 파일
     */
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

    /**
     * 상품 대표 이미지를 제외한 나머지 이미지에 대한 엔티티를 가져온다.
     */
    public List<ItemImg> getItemImgList(Long itemId) {
        return itemImgRepository.findNonItemRepImgByItemId(itemId);
    }

    public String imgFullPath(String imgName) {
        return itemImgLocation + "/" + imgName;
    }
}
