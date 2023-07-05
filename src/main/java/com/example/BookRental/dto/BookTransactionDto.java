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
public class BookTransactionDto {
    private Long id;
    @NotBlank(message = "{bookTransaction.code.not.empty}")
    private String code;
    private LocalDate fromDate;
    private LocalDate toDate;
    @NotNull(message = "{bookTransaction.days.not.empty}")
    private Long days;
    private RENT_TYPE rentStatus;
    @NotNull(message = "{book.not.null}")
    private Long bookId;
    @NotNull(message = "{member.not.null}")
    private Long memberId;
}
