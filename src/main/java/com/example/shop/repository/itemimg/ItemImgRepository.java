package com.example.shop.repository.itemimg;

import com.example.shop.domain.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    @Query("select im from ItemImg im where im.repImg = true and im.item.id = :itemId")
    ItemImg findItemRepImgByItemId(@Param("itemId") Long itemId);

    @Query("select im from ItemImg im where im.repImg = false and im.item.id = :itemId")
    List<ItemImg> findNonItemRepImgByItemId(@Param("itemId") Long itemId);

    @Query("select im from ItemImg im where im.item.id = :itemId")
    List<ItemImg> findItemImgByItemId(@Param("itemId") Long itemId);

    @Modifying
    @Query("delete from ItemImg im where im.repImg = true and im.item.id = :itemId")
    void deleteItemRepImgByItemId(@Param("itemId") Long itemId);

    @Modifying
    @Query("delete from ItemImg im where im.repImg = false and im.item.id = :itemId")
    void deleteNonItemImgByItemId(@Param("itemId") Long itemId);

    @Modifying
    @Query("delete from ItemImg im where im.item.id = :itemId")
    void deleteItemImgByItemId(@Param("itemId") Long itemId);
}
