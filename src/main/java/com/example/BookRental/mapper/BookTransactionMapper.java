package com.example.BookRental.mapper;

import com.example.BookRental.dto.BookTransactionResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookTransactionMapper {
    List<BookTransactionResponseDto> getAllBookTransactionWithFilter(@Param("searchParam") String searchParam);
}
