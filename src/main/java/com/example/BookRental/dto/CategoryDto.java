package com.example.BookRental.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Category Name cannot be empty")
    private String name;
    @NotBlank(message = "Category description cannot be empty")
    private String description;
}
