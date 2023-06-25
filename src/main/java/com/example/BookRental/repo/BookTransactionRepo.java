package com.example.BookRental.repo;

import com.example.BookRental.model.BookTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookTransactionRepo extends JpaRepository<BookTransaction, Long> {
    Optional<BookTransaction> findByCode(String code);
    List<BookTransaction> findByBookId(Long id);
    List<BookTransaction> findByMemberId(Long id);
}
