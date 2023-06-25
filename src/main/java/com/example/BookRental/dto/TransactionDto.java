package com.example.BookRental.dto;

import com.example.BookRental.model.RENT_TYPE;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private String code;
    private LocalDate fromDate;
    private LocalDate toDate;
    @NotNull(message = "Days cannot be empty")
    private Long days;
    private RENT_TYPE rentStatus;
    @NotNull(message = "Book cannot be null")
    private Long bookId;
    @NotNull(message = "Member cannot be null")
    private Long memberId;
}