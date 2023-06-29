package com.example.BookRental.service;

import com.example.BookRental.dto.PasswordResetDto;
import com.example.BookRental.dto.PasswordResetTokenDto;
import com.example.BookRental.model.PasswordResetToken;

public interface PasswordResetTokenService {
    PasswordResetToken generatePasswordResetToken(PasswordResetTokenDto passwordResetTokenDto);
    String resetPassword(PasswordResetDto passwordResetDto, String token);
}
