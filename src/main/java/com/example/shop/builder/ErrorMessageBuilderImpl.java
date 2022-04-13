package com.example.shop.builder;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class ErrorMessageBuilderImpl implements ErrorMessageBuilder{

    @Override
    public String buildErrorMessage(BindingResult bindingResult) {

        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage());
        }
        return sb.toString();
    }
}
