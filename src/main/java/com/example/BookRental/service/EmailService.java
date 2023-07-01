package com.example.BookRental.service;

import com.example.BookRental.model.PasswordResetToken;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<String> sendEmail(PasswordResetToken passwordResetToken, String email);
}