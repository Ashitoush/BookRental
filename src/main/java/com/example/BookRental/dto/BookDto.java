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
    @NotBlank(message = "{name.not.empty}")
    private String name;

    @Positive(message = "{page.not.negative}")
    @NotNull(message = "{page.not.empty}")
    private Integer noOfPages;

    @NotBlank(message = "{book.isbn.not.empty}")
    private String isbn;

    @NotNull(message = "{book.rating.not.null}")
    @Positive(message = "{book.rating.not.negative}")
    private Double rating;

    @NotNull(message = "{stock.count.not.null}")
    @Positive(message = "{stock.count.not.negative}")
    private Integer stockCount;

    private LocalDate publishedDate;
    private MultipartFile photo;
    private  String fullPath;

    @NotNull(message = "{category.id.not.null}")
    private Long categoryId;

    @NotNull(message = "{author.id.not.null}")
    private List<@NotNull Long> authorsId;
}
