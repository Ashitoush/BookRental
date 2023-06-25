package com.example.BookRental.mapper;

import com.example.BookRental.model.Author;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorMapper {
    Integer insertAuthor(Author author);
    Integer updateAuthor(Author author);
    List<Author> getAllAuthor();
    Author getAuthorById(Long id);
    Integer deleteAuthor(Long id);

}
