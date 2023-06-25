package com.example.BookRental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Entity
@Table(name = "tbl_member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_no", unique = true)
    @Size(max = 10)
    private String mobileNo;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "member")
    private List<BookTransaction> bookTransaction;
}
