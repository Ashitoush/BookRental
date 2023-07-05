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
    @Email(message = "{email.invalid}")
    private String email;
    @NotBlank(message = "{member.not.null}")
    private String name;
    @Size(min = 10, max = 10, message = "{phone.length}")
    private String mobileNo;
    private String address;
    private List<Long> bookTransactionIds;
}

