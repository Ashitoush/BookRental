package com.example.BookRental.converter;

import com.example.BookRental.dto.BookTransactionDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.mapper.MemberMapper;
import com.example.BookRental.model.Book;
import com.example.BookRental.model.BookTransaction;
import com.example.BookRental.model.Member;
import com.example.BookRental.repo.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BookTransactionConverter {

    private final BookRepo bookRepo;
    private final MemberMapper memberMapper;

    public BookTransaction toEntity(BookTransactionDto bookTransactionDto) {
        BookTransaction bookTransaction = new BookTransaction();

        Optional<Book> book = bookRepo.findById(bookTransactionDto.getBookId());
        if (!book.isPresent()) {
            throw new CustomException("Book ID: " + bookTransactionDto.getBookId() + " not found");
        }

        Member member = memberMapper.getMemberById(bookTransactionDto.getMemberId());
        if(member == null) {
            throw new CustomException("Member ID: " + bookTransactionDto.getMemberId() + " not found");
        }

        bookTransaction.setId(bookTransactionDto.getId());
        bookTransaction.setCode(bookTransactionDto.getCode());
        bookTransaction.setFromDate(bookTransactionDto.getFromDate());
        bookTransaction.setToDate(bookTransactionDto.getToDate());
        bookTransaction.setRentStatus(bookTransactionDto.getRentStatus());
        bookTransaction.setBook(book.get());
        bookTransaction.setMember(member);

        return bookTransaction;
    }

    public BookTransactionDto toDto(BookTransaction bookTransaction) {
        BookTransactionDto bookTransactionDto = new BookTransactionDto();

        bookTransactionDto.setId(bookTransaction.getId());
        bookTransactionDto.setCode(bookTransaction.getCode());
        bookTransactionDto.setFromDate(bookTransaction.getFromDate());
        bookTransactionDto.setToDate(bookTransaction.getToDate());
        bookTransactionDto.setRentStatus(bookTransaction.getRentStatus());
        bookTransactionDto.setBookId(bookTransaction.getBook().getId());
        bookTransactionDto.setMemberId(bookTransaction.getMember().getId());

        return bookTransactionDto;
    }

    public List<BookTransactionDto> toDto(List<BookTransaction> bookTransactionList) {
        List<BookTransactionDto> bookTransactionDtoList = new ArrayList<>();

        for (BookTransaction bookTransaction : bookTransactionList) {
            BookTransactionDto bookTransactionDto = new BookTransactionDto();

            bookTransactionDto.setId(bookTransaction.getId());
            bookTransactionDto.setCode(bookTransaction.getCode());
            bookTransactionDto.setFromDate(bookTransaction.getFromDate());
            bookTransactionDto.setToDate(bookTransaction.getToDate());
            bookTransactionDto.setRentStatus(bookTransaction.getRentStatus());
            bookTransactionDto.setBookId(bookTransaction.getBook().getId());
            bookTransactionDto.setMemberId(bookTransaction.getMember().getId());

            bookTransactionDtoList.add(bookTransactionDto);
        }
        return bookTransactionDtoList;
    }

}
