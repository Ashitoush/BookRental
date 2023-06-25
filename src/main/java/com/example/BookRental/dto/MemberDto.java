package com.example.BookRental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class MemberDto {
    private Long id;
    @Email(message = "Invalid Email Format for Member")
    private String email;
    @NotBlank(message = "Member Name cannot be empty")
    private String name;
    @Size(min = 10, max = 10, message = "Mobile Number must be of length 10")
    private String mobileNo;
    private String address;
    private List<Long> bookTransactionIds;
}

