package com.example.BookRental.controller;

import com.example.BookRental.dto.BookTransactionDto;
import com.example.BookRental.dto.TransactionDto;
import com.example.BookRental.helper.CheckValidation;
import com.example.BookRental.model.RENT_TYPE;
import com.example.BookRental.service.BookTransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@RequiredArgsConstructor
@RestController
@RequestMapping("bookTransaction")
public class BookTransactionController {

    private final BookTransactionService bookTransactionService;
    private final CheckValidation validation;

//    @PostMapping("/create")
//    public ResponseEntity<?> insertBookTransaction(@Valid @RequestBody BookTransactionDto bookTransactionDto, BindingResult result) {
//        validation.checkValidation(result);
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
    public ResponseEntity<?> updateBookTransaction(HttpServletRequest request, @PathVariable("id") Long id, @Valid @RequestBody TransactionDto bookTransactionDto, BindingResult result) {
        validation.checkValidation(result);
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
        validation.checkValidation(result);
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
    @GetMapping("/generateReport")
//    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('ADMIN')")
    public ResponseEntity<Resource> generateReport() {
        String fileName = "Book Transaction.xlsx";
        InputStreamResource inputStreamResource = new InputStreamResource(bookTransactionService.generateReport());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(inputStreamResource);
    }

    @PostMapping(value = "/insertFromExcel")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<?> insertFromExcel(@RequestParam("file")MultipartFile file) {
        return bookTransactionService.insertFromExcel(file);
    }
}
