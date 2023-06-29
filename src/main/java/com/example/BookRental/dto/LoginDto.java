package com.example.BookRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotBlank(message = "Message cannot be empty")
    private String password;
}
