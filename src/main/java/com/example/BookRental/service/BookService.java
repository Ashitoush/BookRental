package com.example.BookRental.service;

import com.example.BookRental.dto.BookDto;
import com.example.BookRental.model.Book;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface BookService {
    ResponseEntity<Object> insertBook(BookDto bookDto) throws IOException;
    ResponseEntity<Object> getAllBook() throws IOException;
    ResponseEntity<Object> getByBookId(Long id) throws IOException;
    ResponseEntity<Object> updateBook(BookDto bookDto) throws IOException;
    ResponseEntity<Object> deleteBook(Long id);
}
