package com.example.BookRental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto implements Serializable {
    private Long id;
    private String email;
    private String name;
    private String mobileNo;
    private String address;
}
