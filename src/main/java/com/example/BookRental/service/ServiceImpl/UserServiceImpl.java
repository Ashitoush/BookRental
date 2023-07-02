package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.config.CustomMessageSource;
import com.example.BookRental.converter.UserConverter;
import com.example.BookRental.dto.UserDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.model.User;
import com.example.BookRental.repo.UserRepo;
import com.example.BookRental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserConverter userConverter;
    private final CustomMessageSource messageSource;

    @Override
    public ResponseEntity<Object> createUser(UserDto userDto) {
        User user = userConverter.toEntity(userDto);
        user.setEmail(user.getEmail().toLowerCase());

        if (userRepo.save(user) == null) {
            throw new CustomException(messageSource.get("user.create.error"));
        }
        return new ResponseEntity<>(messageSource.get("user.create.success"), HttpStatus.OK);
    }
}
