package com.example.shop.domain;

import com.example.shop.domain.baseentity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImg extends DateBaseEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue
    private Long id;

    private String imgName;

    private String originImgName;

    private String imgUrl;

    private boolean repImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public ItemImg(String imgName, String originImgName, String imgUrl, boolean repImg, Item item) {
        this.imgName = imgName;
        this.originImgName = originImgName;
        this.imgUrl = imgUrl;
        this.repImg = repImg;
        this.item = item;
    }

    public void updateItemImg(String originImgName, String imgName, String imgUrl) {
        this.originImgName = originImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}