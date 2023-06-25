package com.example.BookRental.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_book_transaction")
public class BookTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "rent_status")
    @Enumerated(value = EnumType.STRING)
    private RENT_TYPE rentStatus;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Member member;

    //Not needed
//    @Column(name = "active_closed")
//    private Boolean activeClosed;
}