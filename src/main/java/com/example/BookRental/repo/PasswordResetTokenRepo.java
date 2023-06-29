package com.example.BookRental.repo;

import com.example.BookRental.model.PasswordResetToken;
import com.example.BookRental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
}
