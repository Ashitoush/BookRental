package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.converter.BookTransactionConverter;
import com.example.BookRental.dto.BookTransactionDto;
import com.example.BookRental.dto.TransactionDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.helper.ExcelHelper;
import com.example.BookRental.mapper.MemberMapper;
import com.example.BookRental.model.Book;
import com.example.BookRental.model.BookTransaction;
import com.example.BookRental.model.Member;
import com.example.BookRental.model.RENT_TYPE;
import com.example.BookRental.repo.BookRepo;
import com.example.BookRental.repo.BookTransactionRepo;
import com.example.BookRental.service.BookTransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.ptg.MemErrPtg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepo bookTransactionRepo;
    private final BookTransactionConverter bookTransactionConverter;
    private final BookRepo bookRepo;
    private final MemberMapper memberMapper;
    private final ExcelHelper excelHelper;

    @Override
    public ResponseEntity<Object> insertBookTransaction(BookTransactionDto bookTransactionDto) {
        BookTransaction bookTransaction = bookTransactionConverter.toEntity(bookTransactionDto);

        bookTransaction = bookTransactionRepo.save(bookTransaction);

        if (bookTransaction == null) {
            throw new CustomException("Error while creating book transaction");
        }

        return new ResponseEntity<>("Book Transaction created successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllBookTransaction() {
        List<BookTransaction> bookTransactionList = bookTransactionRepo.findAll();

        List<BookTransactionDto> bookTransactionDtoList = bookTransactionConverter.toDto(bookTransactionList);

        return new ResponseEntity<>(bookTransactionDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getBookTransactionById(Long id) {
        Optional<BookTransaction> bookTransaction = bookTransactionRepo.findById(id);

        if (!bookTransaction.isPresent()) {
            throw new CustomException("Book Transaction with ID: " + id + " not found");
        }
        BookTransactionDto bookTransactionDto = bookTransactionConverter.toDto(bookTransaction.get());
        return new ResponseEntity<>(bookTransactionDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateBookTransaction(TransactionDto bookTransactionDto) {
        Optional<BookTransaction> bookTransaction = bookTransactionRepo.findById(bookTransactionDto.getId());
        if (!bookTransaction.isPresent()) {
            throw new CustomException("Book Transaction with ID: " + bookTransactionDto.getId() + " not found");
        }

        Optional<Book> book = bookRepo.findById(bookTransactionDto.getBookId());
        if (!book.isPresent()) {
            throw new CustomException("Book ID: " + bookTransactionDto.getBookId() + " not found");
        }

        Member member = memberMapper.getMemberById(bookTransactionDto.getMemberId());
        if (member == null) {
            throw new CustomException("Member ID: " + bookTransactionDto.getMemberId() + " not found");
        }

        LocalDate localDate = bookTransaction.get().getFromDate().plusDays(bookTransactionDto.getDays());
        if (bookTransaction.get().getToDate().isBefore(localDate)) {
            bookTransaction.get().setToDate(localDate);
        } else {
            throw new CustomException("The specified day for the rent duration is lower than the day provided earlier");
        }

//        bookTransaction.get().setId(bookTransactionDto.getId());
//        bookTransaction.get().setCode(bookTransactionDto.getCode());
//        bookTransaction.get().setFromDate(bookTransactionDto.getFromDate());
//        bookTransaction.get().setToDate(bookTransactionDto.getToDate());
//        bookTransaction.get().setRentStatus(bookTransactionDto.getRentStatus());
        bookTransaction.get().setBook(book.get());
        bookTransaction.get().setMember(member);

        bookTransactionRepo.save(bookTransaction.get());

        return new ResponseEntity<>("Book Transaction with ID: " + bookTransaction.get().getId() + " updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> rentBookTransaction(BookTransactionDto bookTransactionDto) {

        Optional<Book> book = bookRepo.findById(bookTransactionDto.getBookId());
        if (!book.isPresent()) {
            throw new CustomException("Book with ID: " + bookTransactionDto.getBookId() + " not found");
        }

        Member member = memberMapper.getMemberById(bookTransactionDto.getMemberId());
        if (member == null) {
            throw new CustomException("Member with ID: " + bookTransactionDto.getMemberId() + " not found");
        }

        Integer stockCount = book.get().getStockCount();

        if (stockCount == 0) {
            throw new CustomException("There is no stock for Book: " + book.get().getName());
        }

        LocalDate localDate = bookTransactionDto.getFromDate().plusDays(bookTransactionDto.getDays());
        bookTransactionDto.setToDate(localDate);
        BookTransaction bookTransaction = bookTransactionConverter.toEntity(bookTransactionDto);

        if (bookTransactionRepo.save(bookTransaction) == null) {
            throw new CustomException("Error while renting book");
        }

        book.get().setStockCount(stockCount - 1);
        bookRepo.save(book.get());
        return new ResponseEntity<>("Book rented successfully", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Object> returnBookTransaction(BookTransactionDto bookTransactionDto) {
        Optional<BookTransaction> bookTransaction = bookTransactionRepo.findByCode(bookTransactionDto.getCode());
        if (!bookTransaction.isPresent()) {
            throw new CustomException("Book Transaction not found for the provided code");
        }

        Book book = bookRepo.findById(bookTransaction.get().getBook().getId()).get();
        Integer stockCount = book.getStockCount();

        bookTransaction.get().setToDate(bookTransactionDto.getToDate());
        bookTransaction.get().setRentStatus(bookTransactionDto.getRentStatus());

        if (bookTransactionRepo.save(bookTransaction.get()) == null) {
            throw new CustomException("Error while setting book transaction record rent_status to return");
        }

        book.setStockCount(stockCount + 1);
        bookRepo.save(book);

        return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
    }

    @Override
    public ByteArrayInputStream generateReport() {
        List<BookTransaction> bookTransactionList = bookTransactionRepo.findAll();
        ByteArrayInputStream inputStream = excelHelper.createExcel(bookTransactionList);

        return inputStream;
    }

    @Override
    public ResponseEntity<Object> insertFromExcel(MultipartFile file) {
        List<BookTransaction> bookTransactionList = excelHelper.updateExcel(file);

        for (BookTransaction bookTransaction : bookTransactionList) {
            BookTransaction bookTransaction1 = bookTransactionRepo.findByCode(bookTransaction.getCode()).get();
            if (bookTransaction1 != null) {
                if (bookTransaction1.getRentStatus().toString().equals(RENT_TYPE.RETURN.toString())) {
                    throw new CustomException("Book Transaction data already exists for transaction code: " + bookTransaction.getCode());
                } else if (!bookTransaction1.getFromDate().isEqual(bookTransaction.getFromDate())) {
                    throw new CustomException("The From Date is inconsistent with the existing record");
                } else if (bookTransaction1.getFromDate().isAfter(bookTransaction.getToDate())) {
                    throw new CustomException("The To Date cannot be earlier than the From Date");
                } else {
                    if (bookTransaction.getRentStatus().equals("RENT")) {
                        throw new CustomException("Book Already rented for transaction code: " + bookTransaction.getBook());
                    }
                    bookTransaction1.setMember(bookTransaction.getMember());
                    bookTransaction1.setBook(bookTransaction.getBook());
                    bookTransaction1.setToDate(bookTransaction.getToDate());
                    bookTransaction1.setRentStatus(bookTransaction.getRentStatus());
                    bookTransactionRepo.save(bookTransaction1);
                }
            } else {
                bookTransactionRepo.save(bookTransaction);
            }
        }
        return new ResponseEntity<>("All the book transaction from the file: " + file.getOriginalFilename() + " were successfully added", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteBookTransaction(Long id) {
        Optional<BookTransaction> bookTransaction = bookTransactionRepo.findById(id);
        if (!bookTransaction.isPresent()) {
            throw new CustomException("Book Transaction with ID: " + id + " not found");
        }

        if (bookTransaction.get().getRentStatus() == RENT_TYPE.RENT) {
            throw new CustomException("Book Transaction cannot be deleted until the book is returned");
        }

        bookTransaction.get().setBook(null);
        bookTransaction.get().setMember(null);
        bookTransactionRepo.save(bookTransaction.get());

        bookTransactionRepo.delete(bookTransaction.get());
        return new ResponseEntity<>("Book Transaction with ID: " + id + " deleted successfully", HttpStatus.OK);
    }
}
