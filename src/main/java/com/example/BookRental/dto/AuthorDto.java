package com.example.BookRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private Long id;
    @NotBlank(message = "Name must not be empty")
    private String name;
    @Email(message = "Invalid Email")
    private String email;
    @Size(min = 10, max = 10, message = "Phone Number must be 10 characters")
    private String mobileNumber;
    private List<Long> bookIds;
}