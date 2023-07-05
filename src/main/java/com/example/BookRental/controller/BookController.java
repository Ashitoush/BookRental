package com.example.BookRental.controller;

import com.example.BookRental.dto.BookDto;
import com.example.BookRental.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertBook(@Valid @ModelAttribute BookDto bookDto) throws IOException {
        return bookService.insertBook(bookDto);
    }

    @GetMapping("/getAll")
//    @Secured("ADMIN")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllBook() throws IOException {
        return bookService.getAllBook();
    }

    @GetMapping("getById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getBookById(@PathVariable("id") Long id) throws IOException {
        return bookService.getByBookId(id);
    }

    @PutMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> udpateBook(@PathVariable("id") Long id, @Valid @ModelAttribute BookDto bookDto) throws IOException {
        bookDto.setId(id);
        return bookService.updateBook(bookDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        return bookService.deleteBook(id);
    }
}
