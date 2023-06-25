package com.example.BookRental.controller;

import com.example.BookRental.dto.AuthorDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> insertAuthor(@Valid @RequestBody AuthorDto authorDto, BindingResult result) {
        checkValidation(result);
        return authorService.insertAuthor(authorDto);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllAuthor() {
        return authorService.getAllAuthor();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return authorService.getAuthorById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateAuthor(@PathVariable("id") Long id,@Valid @RequestBody AuthorDto authorDto, BindingResult result) {
        checkValidation(result);
        authorDto.setId(id);
        return authorService.updateAuthor(authorDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteAuthor(@PathVariable("id") Long id) {
        return authorService.deleteAuthor(id);
    }

    private void checkValidation(BindingResult result) {
        if (result.hasErrors()) {
            List<String> message = new ArrayList<>();
            List<FieldError> fieldErrorList = result.getFieldErrors();

            for (FieldError fieldError : fieldErrorList) {
                message.add(fieldError.getDefaultMessage());
            }
            throw new CustomException(message);
        }
    }
}
