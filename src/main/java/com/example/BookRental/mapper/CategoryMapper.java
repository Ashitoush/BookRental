package com.example.BookRental.mapper;

import com.example.BookRental.model.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Integer insertCategory(Category category);
    List<Category> getAllCategory();
    Category getCategoryById(Long id);
    Integer updateCategory(Category category);
    Integer deleteCategory(Long id);
}
