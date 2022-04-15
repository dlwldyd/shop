package com.example.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RefundFailException extends RuntimeException {
    public RefundFailException(String message) {
        super(message);
    }
}
