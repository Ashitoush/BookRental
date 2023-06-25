package com.example.BookRental.controller;

import com.example.BookRental.dto.BookTransactionDto;
import com.example.BookRental.dto.TransactionDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.model.Book;
import com.example.BookRental.model.RENT_TYPE;
import com.example.BookRental.service.BookTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("bookTransaction")
public class BookTransactionController {

    private final BookTransactionService bookTransactionService;

//    @PostMapping("/create")
//    public ResponseEntity<?> insertBookTransaction(@Valid @RequestBody BookTransactionDto bookTransactionDto, BindingResult result) {
//        checkValidation(result);
//
//        return bookTransactionService.insertBookTransaction(bookTransactionDto);
//    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getAllBookTransaction() {
        return bookTransactionService.getAllBookTransaction();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<?> getBookTransactionById(@PathVariable("id") Long id) {
        return bookTransactionService.getBookTransactionById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBookTransaction(@PathVariable("id") Long id, @Valid @RequestBody TransactionDto bookTransactionDto, BindingResult result) {
        checkValidation(result);
        bookTransactionDto.setId(id);
        return bookTransactionService.updateBookTransaction(bookTransactionDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<?> deleteBookTransaction(@PathVariable("id") Long id) {
        return bookTransactionService.deleteBookTransaction(id);
    }

    @PostMapping("/rentBook")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<?> rentBook(@Valid @RequestBody BookTransactionDto bookTransactionDto, BindingResult result) {
        checkValidation(result);
        bookTransactionDto.setRentStatus(RENT_TYPE.RENT);
        bookTransactionDto.setFromDate(LocalDate.now());
        return bookTransactionService.rentBookTransaction(bookTransactionDto);
    }

    @PutMapping("/returnBook")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<?> returnBook(@RequestBody BookTransactionDto bookTransactionDto) {
        bookTransactionDto.setRentStatus(RENT_TYPE.RETURN);
        bookTransactionDto.setToDate(LocalDate.now());
        return bookTransactionService.returnBookTransaction(bookTransactionDto);
    }

    private void checkValidation(BindingResult result) {
        if (result.hasErrors()) {
            List<String> message = new ArrayList<>();
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors) {
                message.add(error.getDefaultMessage());
            }
            throw new CustomException(message);
        }
    }
}
