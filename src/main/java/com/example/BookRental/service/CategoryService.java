package com.example.BookRental.service;

import com.example.BookRental.dto.CategoryDto;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<Object> insertCategory(CategoryDto categoryDto);
    ResponseEntity<Object> getAllCategory();
    ResponseEntity<Object> getCategoryById(Long id);
    ResponseEntity<Object> updateCategory(CategoryDto categoryDto);
    ResponseEntity<Object> deleteCategory(Long id);
}
