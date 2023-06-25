package com.example.BookRental.repo;

import com.example.BookRental.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findByCategoryId(Long id);
}
