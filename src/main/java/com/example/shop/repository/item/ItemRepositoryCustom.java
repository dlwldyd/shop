package com.example.shop.repository.item;

import com.example.shop.Dtos.item.AdminItemFormDto;
import com.example.shop.Dtos.item.ItemSearchDto;
import com.example.shop.Dtos.item.UserItemFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    /**
     * 검색 조건에 맞는 상품 정보와 페이징 정보를 담은 Page 객체를 반환함,
     * createdDate, lastModifiedDate 정보도 담김
     * @param itemSearchDto 검색 조건
     * @param pageable 페이징
     */
    Page<AdminItemFormDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    /**
     * 검색 조건에 맞는 상품 정보와 페이징 정보를 담은 Page 객체를 반환함,
     * createdDate, lastModifiedDate 는 제외됨
     * @param itemSearchDto 검색 조건
     * @param pageable 페이징
     * @return
     */
    Page<UserItemFormDto> getItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
