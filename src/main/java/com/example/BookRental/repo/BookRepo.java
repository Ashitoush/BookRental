package com.example.BookRental.repo;

import com.example.BookRental.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BookRepo extends JpaRepository<Book, Long> {
}
