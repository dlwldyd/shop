package com.example.shop.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    ORDER("ORDER"), CANCEL("CANCEL");

    private final String description;
}
