package com.example.shop.builder;

import org.springframework.validation.BindingResult;

public interface ErrorMessageBuilder {

    String buildErrorMessage(BindingResult bindingResult);
}
