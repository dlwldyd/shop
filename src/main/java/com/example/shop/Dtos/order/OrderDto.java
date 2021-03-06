package com.example.shop.Dtos.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderDto {

    @NotNull
    private Long itemId;

    @Min(1)
    @Max(1000)
    private int count;

    @NotBlank
    private String impUid;

    @NotBlank
    private String merchantUid;
}
