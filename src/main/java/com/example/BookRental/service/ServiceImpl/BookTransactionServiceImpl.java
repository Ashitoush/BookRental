package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.config.CustomMessageSource;
import com.example.BookRental.converter.BookTransactionConverter;
import com.example.BookRental.dto.BookTransactionDto;
import com.example.BookRental.dto.BookTransactionResponseDto;
import com.example.BookRental.dto.TransactionDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.helper.ExcelHelper;
import com.example.BookRental.mapper.BookTransactionMapper;
import com.example.BookRental.mapper.MemberMapper;
import com.example.BookRental.model.Book;
import com.example.BookRental.model.BookTransaction;
import com.example.BookRental.model.Member;
import com.example.BookRental.model.RENT_TYPE;
import com.example.BookRental.repo.BookRepo;
import com.example.BookRental.repo.BookTransactionRepo;
import com.example.BookRental.service.BookTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepo bookTransactionRepo;
    private final BookTransactionConverter bookTransactionConverter;
    private final BookRepo bookRepo;
    private final MemberMapper memberMapper;
    private final BookTransactionMapper bookTransactionMapper;
    private final ExcelHelper excelHelper;
    private final CustomMessageSource messageSource;

    @Override
    public ResponseEntity<Object> insertBookTransaction(BookTransactionDto bookTransactionDto) {
        BookTransaction bookTransaction = bookTransactionConverter.toEntity(bookTransactionDto);

        bookTransaction = bookTransactionRepo.save(bookTransaction);

        if (bookTransaction == null) {
            throw new CustomException(messageSource.get("bookTransaction.create.error"));
        }

        return new ResponseEntity<>(messageSource.get("bookTransaction.create.success"), HttpStatus.OK);
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
            throw new CustomException(messageSource.get("bookTransaction.not.found"));
        }
        BookTransactionDto bookTransactionDto = bookTransactionConverter.toDto(bookTransaction.get());
        return new ResponseEntity<>(bookTransactionDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateBookTransaction(TransactionDto bookTransactionDto) {
        Optional<BookTransaction> bookTransaction = bookTransactionRepo.findById(bookTransactionDto.getId());
        if (!bookTransaction.isPresent()) {
            throw new CustomException(messageSource.get("bookTransaction.not.found"));
        }

        Optional<Book> book = bookRepo.findById(bookTransactionDto.getBookId());
        if (!book.isPresent()) {
            throw new CustomException(messageSource.get("book.not.found"));
        }

        Member member = memberMapper.getMemberById(bookTransactionDto.getMemberId());
        if (member == null) {
            throw new CustomException(messageSource.get("member.not.found"));
        }

        LocalDate localDate = bookTransaction.get().getFromDate().plusDays(bookTransactionDto.getDays());
        if (bookTransaction.get().getToDate().isBefore(localDate)) {
            bookTransaction.get().setToDate(localDate);
        } else {
            String message = messageSource.get("bookTransaction.rentDay.lower");
            throw new CustomException(messageSource.get("bookTransaction.rentDay.lower"));
        }

//        bookTransaction.get().setId(bookTransactionDto.getId());
//        bookTransaction.get().setCode(bookTransactionDto.getCode());
//        bookTransaction.get().setFromDate(bookTransactionDto.getFromDate());
//        bookTransaction.get().setToDate(bookTransactionDto.getToDate());
//        bookTransaction.get().setRentStatus(bookTransactionDto.getRentStatus());
        bookTransaction.get().setBook(book.get());
        bookTransaction.get().setMember(member);

        bookTransactionRepo.save(bookTransaction.get());

        return new ResponseEntity<>(messageSource.get("bookTransaction.update.success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> rentBookTransaction(BookTransactionDto bookTransactionDto) {

        Optional<Book> book = bookRepo.findById(bookTransactionDto.getBookId());
        if (!book.isPresent()) {
            throw new CustomException(messageSource.get("book.not.found"));
        }

        Member member = memberMapper.getMemberById(bookTransactionDto.getMemberId());
        if (member == null) {
            throw new CustomException(messageSource.get("member.not.found"));
        }

        Integer stockCount = book.get().getStockCount();

        if (stockCount == 0) {
            throw new CustomException(messageSource.get("book.stock.zero"));
        }

        LocalDate localDate = bookTransactionDto.getFromDate().plusDays(bookTransactionDto.getDays());
        bookTransactionDto.setToDate(localDate);
        BookTransaction bookTransaction = bookTransactionConverter.toEntity(bookTransactionDto);

        if (bookTransactionRepo.save(bookTransaction) == null) {
            throw new CustomException(messageSource.get("bookTransaction.rent.error"));
        }

        book.get().setStockCount(stockCount - 1);
        bookRepo.save(book.get());
        return new ResponseEntity<>(messageSource.get("bookTransaction.rent.success"), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Object> returnBookTransaction(BookTransactionDto bookTransactionDto) {
        Optional<BookTransaction> bookTransaction = bookTransactionRepo.findByCode(bookTransactionDto.getCode());
        if (!bookTransaction.isPresent()) {
            throw new CustomException(messageSource.get("bookTransaction.not.found.code"));
        }

        Book book = bookRepo.findById(bookTransaction.get().getBook().getId()).get();
        Integer stockCount = book.getStockCount();

        bookTransaction.get().setToDate(bookTransactionDto.getToDate());
        bookTransaction.get().setRentStatus(bookTransactionDto.getRentStatus());

        if (bookTransactionRepo.save(bookTransaction.get()) == null) {
            throw new CustomException(messageSource.get("bookTransaction.status.return.error"));
        }

        book.setStockCount(stockCount + 1);
        bookRepo.save(book);

        return new ResponseEntity<>(messageSource.get("bookTransaction.return.success"), HttpStatus.OK);
    }

    @Override
    public ByteArrayInputStream generateReport() {
        List<BookTransaction> bookTransactionList = bookTransactionRepo.findAll();
        ByteArrayInputStream inputStream = excelHelper.createExcel(bookTransactionList);

        return inputStream;
    }

    @Override
    public ResponseEntity<Object> reportDataWithFilter(String searchParam) {
        List<BookTransactionResponseDto> bookTransactionList = null;
        try {
            bookTransactionList = bookTransactionMapper.getAllBookTransactionWithFilter(searchParam);
        } catch (Exception e) {
            throw new CustomException(Arrays.toString(e.getStackTrace()));
        }
        return new ResponseEntity<>(bookTransactionList, HttpStatus.OK);
    }

    @Override
    public ByteArrayInputStream generateReportWithFilter(String searchParam) {
        List<BookTransactionResponseDto> bookTransactionResponseDtoList = bookTransactionMapper.getAllBookTransactionWithFilter(searchParam);
        if (bookTransactionResponseDtoList.isEmpty()) {
            throw new CustomException(messageSource.get("bookTransaction.not.found"));
        }
        List<BookTransaction> bookTransactionList = bookTransactionConverter.toEntity(bookTransactionResponseDtoList);
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
                    throw new CustomException(messageSource.get("bookTransaction.exists.code"));
                } else if (!bookTransaction1.getFromDate().isEqual(bookTransaction.getFromDate())) {
                    throw new CustomException(messageSource.get("bookTransaction.from.date.inconsistent"));
                } else if (bookTransaction1.getFromDate().isAfter(bookTransaction.getToDate())) {
                    throw new CustomException(messageSource.get("bookTransaction.to.date.inconsistent"));
                } else {
                    if (bookTransaction.getRentStatus().equals("RENT")) {
                        throw new CustomException(messageSource.get("bookTransaction.already.rented"));
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
        return new ResponseEntity<>(messageSource.get("bookTransaction.excel.upload.success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteBookTransaction(Long id) {
        Optional<BookTransaction> bookTransaction = bookTransactionRepo.findById(id);
        if (!bookTransaction.isPresent()) {
            throw new CustomException(messageSource.get("bookTransaction.not.found"));
        }

        if (bookTransaction.get().getRentStatus() == RENT_TYPE.RENT) {
            throw new CustomException(messageSource.get("bookTransaction.delete.rent.error"));
        }

        bookTransaction.get().setBook(null);
        bookTransaction.get().setMember(null);
        bookTransactionRepo.save(bookTransaction.get());

        bookTransactionRepo.delete(bookTransaction.get());
        return new ResponseEntity<>(messageSource.get("bookTransaction.delete.success"), HttpStatus.OK);
    }
}
