package com.example.shop.Dtos.cart;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    @NotBlank
    private String impUid;

    @NotBlank
    private String merchantUid;

    private List<Long> cartItemIdList;
}
