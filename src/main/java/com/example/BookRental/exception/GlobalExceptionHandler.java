package com.example.BookRental.exception;

import com.example.BookRental.config.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.binding.BindingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final CustomMessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        List<String> details = new ArrayList<>();

        details.add(ex.getMessage());

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                messageSource.get("resource.not.found"),
                details
        );

        return ResponseEntityBuilder.build(error);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        List<String> details = new ArrayList<>();

        details.add(ex.getMessage());

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                messageSource.get("error"),
                details
        );
        return ResponseEntityBuilder.build(apiError);
    }

//    if (e.hasErrors()) {
//        List<String> message = new ArrayList<>();
//        List<FieldError> errors = result.getFieldErrors();
//
//        for (FieldError error : errors) {
//            message.add(error.getDefaultMessage());
//        }
//        throw new CustomException(message);
//    }

    @Override
    public ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> message = new ArrayList<>();
        List<FieldError> errors = ex.getFieldErrors();

        for (FieldError error : errors) {
            message.add(error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                messageSource.get("error"),
                message
        );
        return ResponseEntityBuilder.build(apiError);
    }
}
