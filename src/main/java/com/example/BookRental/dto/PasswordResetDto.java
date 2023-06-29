package com.example.BookRental.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class PasswordResetDto {
    @NotEmpty(message = "Password Field Cannot be Empty")
    @Size(min = 6, message = "Password length must be at least 6 character long")
    private String password;

    @NotEmpty(message = "Confirm Password Field Cannot be Empty")
    @Size(min = 6, message = "Password length must be at least 6 character long")
    private String confirmPassword;
}
