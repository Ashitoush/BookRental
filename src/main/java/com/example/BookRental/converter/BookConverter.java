package com.example.BookRental.converter;

import com.example.BookRental.dto.BookDto;
import com.example.BookRental.helper.PhotoHelper;
import com.example.BookRental.mapper.AuthorMapper;
import com.example.BookRental.mapper.CategoryMapper;
import com.example.BookRental.model.Author;
import com.example.BookRental.model.Book;
import com.example.BookRental.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {

    private final AuthorMapper authorMapper;
    private final CategoryMapper categoryMapper;
    private final PhotoHelper photoHelper;

    public Book toEntity(BookDto bookDto) throws IOException {
        Book book = new Book();

        List<Author> authorList = bookDto.getAuthorsId().stream().map(author -> authorMapper.getAuthorById(author)).collect(Collectors.toList());
        Category categoryList = categoryMapper.getCategoryById(bookDto.getCategoryId());

        String photoPath = photoHelper.storePhoto(bookDto.getPhoto());

        book.setName(bookDto.getName());
        book.setIsbn(bookDto.getIsbn());
        book.setRating(bookDto.getRating());
        book.setPublishedDate(bookDto.getPublishedDate());
        book.setStockCount(bookDto.getStockCount());
        book.setNoOfPages(bookDto.getNoOfPages());
        book.setCategory(categoryList);
        book.setAuthors(authorList);
        book.setId(bookDto.getId());
        book.setPhoto(photoPath);

        return book;
    }

    public BookDto toDto(Book book) throws IOException {
        BookDto bookDto = new BookDto();

        List<Long> authorsId = book.getAuthors().stream().map(author -> author.getId()).collect(Collectors.toList());
        Long categoryId = book.getCategory().getId();

        bookDto.setId(book.getId());
        bookDto.setName(book.getName());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setRating(book.getRating());
        bookDto.setPublishedDate(book.getPublishedDate());
        bookDto.setStockCount(book.getStockCount());
        bookDto.setNoOfPages(book.getNoOfPages());
        bookDto.setCategoryId(categoryId);
        bookDto.setAuthorsId(authorsId);
        bookDto.setFullPath(photoHelper.imageUrl(book));

        return bookDto;
    }

    public List<BookDto> toDto(List<Book> bookList) throws IOException {
        List<BookDto> bookDtoList = new ArrayList<>();

        for (Book book : bookList) {
            List<Long> authorsId = book.getAuthors().stream().map(author -> author.getId()).collect(Collectors.toList());
            Long categoryId = book.getCategory().getId();
            BookDto bookDto = new BookDto();

            bookDto.setId(book.getId());
            bookDto.setName(book.getName());
            bookDto.setIsbn(book.getIsbn());
            bookDto.setRating(book.getRating());
            bookDto.setPublishedDate(book.getPublishedDate());
            bookDto.setStockCount(book.getStockCount());
            bookDto.setNoOfPages(book.getNoOfPages());
            bookDto.setCategoryId(categoryId);
            bookDto.setAuthorsId(authorsId);
            bookDto.setFullPath(photoHelper.imageUrl(book));

            bookDtoList.add(bookDto);
        }



        return bookDtoList;
    }
}
