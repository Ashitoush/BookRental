package com.example.BookRental.dto;

import com.example.BookRental.model.RENT_TYPE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookTransactionResponseDto implements Serializable {
    private Long id;
    private String code;
    private LocalDate fromDate;
    private LocalDate toDate;
    private RENT_TYPE rentStatus;
    private BookResponseDto book;
    private MemberResponseDto member;
}
