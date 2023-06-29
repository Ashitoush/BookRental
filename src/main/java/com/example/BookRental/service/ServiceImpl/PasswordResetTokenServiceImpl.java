package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.dto.PasswordResetDto;
import com.example.BookRental.dto.PasswordResetTokenDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.model.PasswordResetToken;
import com.example.BookRental.model.User;
import com.example.BookRental.repo.PasswordResetTokenRepo;
import com.example.BookRental.repo.UserRepo;
import com.example.BookRental.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final UserRepo userRepo;
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PasswordResetToken generatePasswordResetToken(PasswordResetTokenDto passwordResetTokenDto) {
        Optional<User> user = this.userRepo.findByEmail(passwordResetTokenDto.getEmail());
        if (!user.isPresent()) {
            throw new CustomException("No User found for email: " + passwordResetTokenDto.getEmail());
        }


        Optional<PasswordResetToken> passwordResetToken2 = this.passwordResetTokenRepo.findByUser(user.get());
        if (passwordResetToken2.isPresent()) {
            passwordResetToken2.get().setExpirationDate(LocalDateTime.now().plusMinutes(60));
            return this.passwordResetTokenRepo.save(passwordResetToken2.get());
        } else {
            String passwordResetToken = UUID.randomUUID().toString();
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(60);

            PasswordResetToken passwordResetToken1 = PasswordResetToken.builder()
                    .token(passwordResetToken)
                    .expirationDate(expirationTime)
                    .user(user.get())
                    .build();
            return this.passwordResetTokenRepo.save(passwordResetToken1);
        }
    }

    @Override
    public String resetPassword(PasswordResetDto passwordResetDto, String token) {
        Optional<PasswordResetToken> passwordResetToken = this.passwordResetTokenRepo.findByToken(token);
        if (!passwordResetToken.isPresent()) {
            throw new CustomException("Invalid Password Reset Token");
        } else {
            if (passwordResetToken.get().getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new CustomException("Password Reset Token Expired, Create a New Password Reset Token");
            }
        }

        User user = passwordResetToken.get().getUser();
        Optional<User> user1 = this.userRepo.findById(user.getId());
        if (!user1.isPresent()) {
            throw new CustomException("User Does not exists for the given token");
        }

        user = user1.get();
        user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
        userRepo.save(user);

        passwordResetToken.get().setUser(null);
        passwordResetTokenRepo.delete(passwordResetToken.get());

        return "Password reset Successful";
    }
}
