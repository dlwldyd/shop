package com.example.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "삭제된 상품")
public class DeletedItemException extends RuntimeException {

    public DeletedItemException(String message) {
        super(message);
    }
}
