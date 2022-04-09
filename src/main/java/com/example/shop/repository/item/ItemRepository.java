package com.example.shop.repository.item;

import com.example.shop.domain.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Query("select distinct i from Item i join fetch i.itemImgList order by i.id desc")
    List<Item> getItemList(Pageable pageable);
}
