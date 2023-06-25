package com.example.BookRental.exception;

import java.util.List;

public class CustomException extends RuntimeException{
    public CustomException(List<String> message) {
        super(String.join(", ", message));
    }

    public CustomException(String message) {
        super(message);
    }
}
