package com.example.BookRental.converter;

import com.example.BookRental.dto.CategoryDto;
import com.example.BookRental.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryConverter {

    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        return categoryDto;
    }

    public List<CategoryDto> toDto(List<Category> categoryList) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();

        for (Category category : categoryList) {
            CategoryDto categoryDto = new CategoryDto();

            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setDescription(category.getDescription());

            categoryDtoList.add(categoryDto);
        }
        return categoryDtoList;
    }

    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();

        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return category;
    }

    public List<Category> toEntity(List<CategoryDto> categoryDtoList) {
        List<Category> categoryList = new ArrayList<>();

        for (CategoryDto categoryDto : categoryDtoList) {
            Category category = new Category();

            category.setId(categoryDto.getId());
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());

            categoryList.add(category);
        }
        return categoryList;
    }
}
