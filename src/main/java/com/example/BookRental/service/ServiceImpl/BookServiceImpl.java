package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.converter.BookConverter;
import com.example.BookRental.dto.BookDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.helper.PhotoHelper;
import com.example.BookRental.mapper.AuthorMapper;
import com.example.BookRental.mapper.BookMapper;
import com.example.BookRental.mapper.CategoryMapper;
import com.example.BookRental.model.Author;
import com.example.BookRental.model.Book;
import com.example.BookRental.model.Category;
import com.example.BookRental.repo.BookRepo;
import com.example.BookRental.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookConverter bookConverter;
    private final BookRepo bookRepo;
    private final AuthorMapper authorMapper;
    private final CategoryMapper categoryMapper;
    private final PhotoHelper photoHelper;

    @Override
    public ResponseEntity<Object> insertBook(BookDto bookDto) throws IOException {
        Book book = bookConverter.toEntity(bookDto);

//        Integer count = bookMapper.insertBook(book);
//        if(count == 0) {
//            throw new CustomException("Error while inserting book");
//        }

        Book book1 = bookRepo.save(book);

        if (book1 == null) {
            throw new CustomException("Error while inserting book");
        }

        return new ResponseEntity<>("Book Inserted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllBook() throws IOException {
        List<Book> bookList = bookRepo.findAll();
        List<BookDto> bookDtoList = bookConverter.toDto(bookList);

        return new ResponseEntity<>(bookDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getByBookId(Long id) throws IOException {
        Optional<Book> book = bookRepo.findById(id);

        if (!book.isPresent()) {
            throw new CustomException("Book with ID: " + id + " not found");
        }

        BookDto bookDto = bookConverter.toDto(book.get());

        return new ResponseEntity<>(bookDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateBook(BookDto bookDto) throws IOException {
        Optional<Book> optionalBook = bookRepo.findById(bookDto.getId());

        if(!optionalBook.isPresent()) {
            throw new CustomException("Book with ID: " + bookDto.getId() + " not found");
        }

        List<Author> authorList = bookDto.getAuthorsId().stream().map(author -> authorMapper.getAuthorById(author)).collect(Collectors.toList());
        if(authorList.isEmpty() || authorList.contains(null)) {
            throw new CustomException("Author Id is wrong");
        }

        Category category = categoryMapper.getCategoryById(bookDto.getCategoryId());
        if(category == null) {
            throw new CustomException("Category with Id: " + bookDto.getCategoryId() + " not found");
        }

        Book book = optionalBook.get();


        String photoPath = photoHelper.storePhoto(bookDto.getPhoto());
        book.setName(bookDto.getName());
        book.setIsbn(bookDto.getIsbn());
        book.setRating(bookDto.getRating());
        book.setPublishedDate(bookDto.getPublishedDate());
        book.setStockCount(bookDto.getStockCount());
        book.setNoOfPages(bookDto.getNoOfPages());
        book.setCategory(category);
        book.setAuthors(authorList);
        book.setPhoto(photoPath);

        book = bookRepo.save(book);

        if (book == null) {
            throw new CustomException("Error while updating book");
        }
        return new ResponseEntity<>("Book with ID: " + book.getId() + " Updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepo.findById(id);
        if(!optionalBook.isPresent()) {
            throw new CustomException("Book with ID: " + id + " not found");
        }

        Book book = optionalBook.get();

        book.setAuthors(null);
        bookRepo.save(book);

        Integer count = bookMapper.deleteBook(id);
        if (count == 0) {
            throw new CustomException("Error while deleting Book with ID: " + id);
        }
        return new ResponseEntity<>("Book with ID: " + id + " deleted successfully", HttpStatus.OK);
    }
}