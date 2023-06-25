package com.example.BookRental.controller;

import com.example.BookRental.dto.CategoryDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDto categoryDto, BindingResult result) {
        checkValidation(result);
        return categoryService.insertCategory(categoryDto);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryDto categoryDto, BindingResult result) {
        checkValidation(result);
        categoryDto.setId(id);
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        return categoryService.deleteCategory(id);
    }

    private void checkValidation(BindingResult result) {
        if (result.hasErrors()) {
            List<String> message = new ArrayList<>();
            List<FieldError> fieldErrorList = result.getFieldErrors();

            for (FieldError fieldError : fieldErrorList) {
                message.add(fieldError.getDefaultMessage());
            }
            throw new CustomException(message);
        }
    }


}
