package com.example.BookRental.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @Size(max = 100)
    private String name;

    @Column(name = "no_of_pages")
    private Integer noOfPages;

    @Column(name = "isbn")
    @Size(max = 30)
    private String isbn;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "stock_count")
    private Integer stockCount;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(name = "photo")
    @Size(max = 200)
    private String photo;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    private List<Author> authors;
}
