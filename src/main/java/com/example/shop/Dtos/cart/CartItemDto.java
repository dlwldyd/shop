package com.example.shop.Dtos.cart;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartItemDto {

    @NotNull
    private Long itemId;

    @Min(1)
    private int count;
}
