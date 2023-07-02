package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.config.CustomMessageSource;
import com.example.BookRental.converter.CategoryConverter;
import com.example.BookRental.dto.CategoryDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.mapper.CategoryMapper;
import com.example.BookRental.model.Book;
import com.example.BookRental.model.Category;
import com.example.BookRental.repo.BookRepo;
import com.example.BookRental.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryConverter categoryConverter;
    private final CategoryMapper categoryMapper;
    private final BookRepo bookRepo;
    private final CustomMessageSource messageSource;

    @Override
    public ResponseEntity<Object> insertCategory(CategoryDto categoryDto) {
        Category category = categoryConverter.toEntity(categoryDto);

        if(category == null) {
            throw new CustomException(messageSource.get("category.from.dto.error"));
        }

        Integer count = categoryMapper.insertCategory(category);

        if(count == 0) {
            throw new CustomException(messageSource.get("category.insert.error"));
        }

        return new ResponseEntity<>(messageSource.get("category.insert.success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllCategory() {
        List<Category> categoryList = categoryMapper.getAllCategory();

        if(categoryList.isEmpty()) {
            throw new CustomException(messageSource.get("category.read.error"));
        }

        List<CategoryDto> categoryDto = categoryConverter.toDto(categoryList);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getCategoryById(Long id) {
        Category category = categoryMapper.getCategoryById(id);

        if(category == null) {
            throw new CustomException(messageSource.get("category.not.found"));
        }

        CategoryDto categoryDto = categoryConverter.toDto(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateCategory(CategoryDto categoryDto) {

        Category category = categoryMapper.getCategoryById(categoryDto.getId());

        if (category == null) {
            throw new CustomException(messageSource.get("category.not.found"));
        }

        category = categoryConverter.toEntity(categoryDto);

        Integer count = categoryMapper.updateCategory(category);

        if (count == 0) {
            throw new CustomException(messageSource.get("category.update.error"));
        }

        return new ResponseEntity<>(messageSource.get("category.update.success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteCategory(Long id) {
        Category category = categoryMapper.getCategoryById(id);

        if (category == null) {
            throw new CustomException(messageSource.get("category.not.found"));
        }

        List<Book> bookList = bookRepo.findByCategoryId(id);

        for (Book book : bookList) {
            book.setCategory(null);
        }

        categoryMapper.deleteCategory(id);
        return new ResponseEntity<>(messageSource.get("category.delete.success"), HttpStatus.OK);
    }
}
