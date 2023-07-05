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
    @NotEmpty(message = "{password.not.empty}")
    @Size(min = 6, message = "{password.length.short}")
    private String password;

    @NotEmpty(message = "{password.confirm.not.empty}")
    @Size(min = 6, message = "{password.length.short}")
    private String confirmPassword;
}
