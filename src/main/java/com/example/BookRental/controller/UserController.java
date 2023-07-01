package com.example.BookRental.controller;

import com.example.BookRental.config.CustomUserDetail;
import com.example.BookRental.config.CustomUserDetailService;
import com.example.BookRental.converter.UserConverter;
import com.example.BookRental.dto.*;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.helper.JwtHelper;
import com.example.BookRental.model.PasswordResetToken;
import com.example.BookRental.repo.RoleRepo;
import com.example.BookRental.repo.UserRepo;
import com.example.BookRental.service.EmailService;
import com.example.BookRental.service.PasswordResetTokenService;
import com.example.BookRental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final UserConverter userConverter;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
        checkValidation(result);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.createUser(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult result) {
        checkValidation(result);
        String password = loginDto.getPassword();
        this.authenticate(loginDto.getEmail(), password);
//        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(loginDto.getEmail());
        UserDto user = userConverter.toDto(this.userRepo.findByEmail(loginDto.getEmail()).get());
        String token = jwtHelper.generateToken(new CustomUserDetail(user, roleRepo));
        String bearerToken = "Bearer " + token;
        return new ResponseEntity<>(bearerToken, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto, @RequestParam("token") String token, BindingResult result) {
        checkValidation(result);
        if (!passwordResetDto.getPassword().equals(passwordResetDto.getConfirmPassword())) {
            throw new CustomException("Password and Confirm Password does not match");
        }
        String message = this.passwordResetTokenService.resetPassword(passwordResetDto, token);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/resetPasswordRequest")
    public ResponseEntity<?> resetPasswordEmailSend(@Valid @RequestBody PasswordResetTokenDto passwordResetTokenDto, BindingResult result) {
        checkValidation(result);
        PasswordResetToken passwordResetToken = this.passwordResetTokenService.generatePasswordResetToken(passwordResetTokenDto);
        sendEmail(passwordResetToken, passwordResetTokenDto.getEmail());
        return new ResponseEntity<>("Reset Token Sent Successfully", HttpStatus.OK);
    }
    private void sendEmail(PasswordResetToken passwordResetToken, String email){
        CompletableFuture<String> message = emailService.sendEmail(passwordResetToken, email);
    }

    private void authenticate(String userName, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
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
