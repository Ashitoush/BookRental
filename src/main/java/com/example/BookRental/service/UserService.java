package com.example.BookRental.service;

import com.example.BookRental.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Object> createUser(UserDto userDto);
}
