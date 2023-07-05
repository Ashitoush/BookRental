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
    @NotBlank(message = "{name.not.empty}")
    private String name;
    @Email(message = "{email.invalid}")
    private String email;
    @Size(min = 10, max = 10, message = "{phone.length}")
    private String mobileNumber;
    private List<Long> bookIds;
}