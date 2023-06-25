package com.example.BookRental.controller;

import com.example.BookRental.dto.BookDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertBook(@Valid @ModelAttribute BookDto bookDto, BindingResult result) throws IOException {
        checkValidation(result);

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
    public ResponseEntity<?> udpateBook(@PathVariable("id") Long id, @Valid @ModelAttribute BookDto bookDto, BindingResult result) throws IOException {
        checkValidation(result);
        bookDto.setId(id);
        return bookService.updateBook(bookDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        return bookService.deleteBook(id);
    }

    private void checkValidation(BindingResult result) {
        if(result.hasErrors()) {
            List<String> message = new ArrayList<>();
            List<FieldError> fieldErrors = result.getFieldErrors();

            for (FieldError error : fieldErrors) {
                message.add(error.getDefaultMessage());
            }
            throw new CustomException(message);
        }
    }
}