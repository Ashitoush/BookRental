package com.example.BookRental.mapper;

import com.example.BookRental.model.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    List<Book> getAllBook();
    Book getBookById(Long id);
    Integer updateBook(Book book);
    Integer deleteBook(Long id);
}
