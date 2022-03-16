package com.example.shop.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ItemCategory {

    LAPTOP("노트북"), PC("PC"), PERIPHERALS("주변장치"), MONITOR("모니터"), KEYBOARD("키보드"), MOUSE("마우스");

    private final String description;
}
