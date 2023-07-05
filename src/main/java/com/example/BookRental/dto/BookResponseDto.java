package com.example.BookRental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto implements Serializable {
    private Long id;
    private String name;
    private Integer noOfPages;
    private String isbn;
    private Double rating;
    private Integer stockCount;
    private String photo;
}
