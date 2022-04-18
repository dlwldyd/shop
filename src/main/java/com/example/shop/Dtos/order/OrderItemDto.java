package com.example.shop.Dtos.order;

import com.example.shop.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String itemName;

    private int count;

    private int price;

    private long totalPrice;

    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.price = orderItem.getItemPrice();
        this.imgUrl = imgUrl;
        this.totalPrice = orderItem.getTotalPrice();
    }
}
