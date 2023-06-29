package com.example.BookRental.service;

import com.example.BookRental.dto.BookTransactionDto;
import com.example.BookRental.dto.TransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

public interface BookTransactionService {
    ResponseEntity<Object> insertBookTransaction(BookTransactionDto bookTransactionDto);
    ResponseEntity<Object> getAllBookTransaction();
    ResponseEntity<Object> getBookTransactionById(Long id);
    ResponseEntity<Object> updateBookTransaction(TransactionDto bookTransactionDto);
    ResponseEntity<Object> rentBookTransaction(BookTransactionDto bookTransactionDto);
    ResponseEntity<Object> returnBookTransaction(BookTransactionDto bookTransactionDto);
    ByteArrayInputStream generateReport();
    ResponseEntity<Object> insertFromExcel(MultipartFile file);
    ResponseEntity<Object> deleteBookTransaction(Long id);

}
