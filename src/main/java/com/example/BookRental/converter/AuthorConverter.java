package com.example.BookRental.converter;

import com.example.BookRental.dto.AuthorDto;
import com.example.BookRental.model.Author;

import com.example.BookRental.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AuthorConverter {

    public AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();

        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setEmail(author.getEmail());
        authorDto.setMobileNumber(author.getMobileNumber());
        if (author.getBooks() != null) {
            authorDto.setBookIds(author.getBooks().stream().map(Book::getId).collect(Collectors.toList()));
        }

        return authorDto;
    }
    public Author toEntity(AuthorDto authorDto, List<Book> bookList) {
        Author author = new Author();

        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        author.setEmail(authorDto.getEmail());
        author.setMobileNumber(authorDto.getMobileNumber());

        if(bookList != null) {
            author.setBooks(bookList);
        }

        return author;
    }

    public List<AuthorDto> toDto(List<Author> authors) {
        List<AuthorDto> authorDtoList = new ArrayList<>();

        for (Author author : authors) {
            AuthorDto authorDto = new AuthorDto();

            authorDto.setId(author.getId());
            authorDto.setName(author.getName());
            authorDto.setEmail(author.getEmail());
            authorDto.setMobileNumber(author.getMobileNumber());
            if(author.getBooks() != null) {
                authorDto.setBookIds(author.getBooks().stream().map(Book::getId).collect(Collectors.toList()));
            }

            authorDtoList.add(authorDto);
        }
        return authorDtoList;
    }
}
