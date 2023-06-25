package com.example.BookRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {

    private String name;

    @Email(message = "Email Invalid")
    private String email;

    @Size(min = 6, message = "Password length must be at least 6 character long")
    private String password;

    private String role;
}
