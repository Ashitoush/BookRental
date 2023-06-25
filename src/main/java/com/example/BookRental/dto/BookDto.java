package com.example.BookRental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Positive(message = "Page number cannot be negative")
    private Integer noOfPages;
    @NotBlank(message = "ISBN cannot be empty")
    private String isbn;
    @NotNull(message = "Rating cannot be null")
    private Double rating;
    @NotNull(message = "Stock Count cannot be null")
    private Integer stockCount;
    private LocalDate publishedDate;
    private MultipartFile photo;
    private  String fullPath;
    @NotNull(message = "Category Id cannot be null")
    private Long categoryId;
    @NotNull(message = "Authors Id cannot be null")
    private List<@NotNull Long> authorsId;
}
