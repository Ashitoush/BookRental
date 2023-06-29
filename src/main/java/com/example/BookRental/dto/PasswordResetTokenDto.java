package com.example.BookRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PasswordResetTokenDto {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid Email Format")
    private String email;
}
