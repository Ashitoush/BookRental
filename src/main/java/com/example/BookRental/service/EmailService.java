package com.example.BookRental.service;

import com.example.BookRental.model.PasswordResetToken;

public interface EmailService {
    String sendEmail(PasswordResetToken passwordResetToken, String email);
}