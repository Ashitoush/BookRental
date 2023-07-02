package com.example.BookRental.helper;

import com.example.BookRental.exception.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckValidation {
    public void checkValidation(BindingResult result) {
        if (result.hasErrors()) {
            List<String> message = new ArrayList<>();
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors) {
                message.add(error.getDefaultMessage());
            }
            throw new CustomException(message);
        }
    }
}
