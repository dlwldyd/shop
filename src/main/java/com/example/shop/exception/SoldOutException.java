package com.example.shop.exception;

public class SoldOutException extends RuntimeException {

    public SoldOutException(String message) {
        super(message);
    }
}
