package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.converter.AuthorConverter;
import com.example.BookRental.dto.AuthorDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.exception.ResourceNotFoundException;
import com.example.BookRental.mapper.AuthorMapper;
import com.example.BookRental.mapper.BookMapper;
import com.example.BookRental.model.Author;
import com.example.BookRental.model.Book;
import com.example.BookRental.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;
    private final AuthorConverter authorConverter;

    @Override
    public ResponseEntity<Object> insertAuthor(AuthorDto authorDto) {
        List<Book> bookList = new ArrayList<>();
        List<Long> bookIds = authorDto.getBookIds();

        if (bookIds != null) {
            for (Long id : bookIds) {
                Book book = new Book();
                book = bookMapper.getBookById(id);
                bookList.add(book);
            }
        }

        Author author = authorConverter.toEntity(authorDto, bookList);

        Integer result = authorMapper.insertAuthor(author);

        if (result > 0) {
            return new ResponseEntity<>("Author Inserted Successfully", HttpStatus.OK);
        } else {
            throw new CustomException("Author Cannot Be Inserted !!!");
        }
    }

    @Override
    public ResponseEntity<Object> getAllAuthor() {
        List<Author> authors = authorMapper.getAllAuthor();
        List<AuthorDto> authorDtoList = authorConverter.toDto(authors);

        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("Error while retrieving Authors");
        }
        return new ResponseEntity<>(authorDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAuthorById(Long id) {
        Author author = authorMapper.getAuthorById(id);

        if (author == null) {
            throw new ResourceNotFoundException("Author with ID: " + id + " not found");
        }

        AuthorDto authorDto = authorConverter.toDto(author);
        return new ResponseEntity<>(authorDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateAuthor(AuthorDto authorDto) {
        List<Book> bookList = new ArrayList<>();
        List<Long> bookIds = authorDto.getBookIds();

        if (bookIds != null) {
            for (Long id : bookIds) {
                Book book = new Book();
                book = bookMapper.getBookById(id);
                bookList.add(book);
            }
        }
        Author author = authorConverter.toEntity(authorDto, bookList);

        Integer count = authorMapper.updateAuthor(author);

        if (count == 0) {
            throw new CustomException("Cannot update author");
        }
        return new ResponseEntity<>("Author Updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteAuthor(Long id) {
        Author author = authorMapper.getAuthorById(id);

        if (author == null) {
            throw new CustomException("Author With ID: " + id + " not found");
        }

        Integer count = authorMapper.deleteAuthor(id);

        if (count == 0) {
            throw new CustomException("Error while deleting Author with ID: " + id);
        }

        return new ResponseEntity<>("Author with ID: " + id + " deleted successfully", HttpStatus.OK);
    }


}
