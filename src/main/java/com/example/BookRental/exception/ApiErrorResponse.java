package com.example.BookRental.exception;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ApiErrorResponse {
    private String errorId;
    private Integer status;
    private String message;
    private List<String> errors;
    private String path;
}
