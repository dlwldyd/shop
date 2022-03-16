package com.example.shop.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ItemStatus {

    SELL("판매 중"), SOLD_OUT("품절");

    private final String description;
}
