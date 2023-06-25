package com.example.BookRental.controller;

import com.example.BookRental.dto.UserDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
        checkValidation(result);
        return userService.createUser(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDto userDto, BindingResult result) {
        checkValidation(result);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(authentication, HttpStatusCode.valueOf(200));

    }

    private void checkValidation(BindingResult result) {
        if (result.hasErrors()) {
            List<String> message = new ArrayList<>();
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors) {
                message.add(error.getDefaultMessage());
            }
            throw new CustomException(message);
        }
    }
}
