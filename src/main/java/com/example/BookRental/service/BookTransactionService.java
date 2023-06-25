package com.example.BookRental.service;

import com.example.BookRental.dto.BookTransactionDto;
import org.springframework.http.ResponseEntity;

public interface BookTransactionService {
    ResponseEntity<Object> insertBookTransaction(BookTransactionDto bookTransactionDto);
    ResponseEntity<Object> getAllBookTransaction();
    ResponseEntity<Object> getBookTransactionById(Long id);
    ResponseEntity<Object> updateBookTransaction(BookTransactionDto bookTransactionDto);
    ResponseEntity<Object> rentBookTransaction(BookTransactionDto bookTransactionDto);
    ResponseEntity<Object> returnBookTransaction(BookTransactionDto bookTransactionDto);
    ResponseEntity<Object> deleteBookTransaction(Long id);

}
