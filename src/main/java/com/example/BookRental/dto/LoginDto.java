package com.example.BookRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    @NotBlank(message = "{email.not.empty}")
    @Email(message = "{email.invalid}")
    private String email;

    @NotBlank(message = "{password.not.empty}")
    private String password;
}
