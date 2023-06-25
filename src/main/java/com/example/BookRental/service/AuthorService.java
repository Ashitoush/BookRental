package com.example.BookRental.service;

import com.example.BookRental.dto.AuthorDto;
import org.springframework.http.ResponseEntity;

public interface AuthorService {
    ResponseEntity<Object> insertAuthor(AuthorDto authorDto);
    ResponseEntity<Object> getAllAuthor();
    ResponseEntity<Object> getAuthorById(Long id);
    ResponseEntity<Object> updateAuthor(AuthorDto authorDto);
    ResponseEntity<Object> deleteAuthor(Long id);
}
