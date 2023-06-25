package com.example.BookRental.config;

import com.example.BookRental.converter.UserConverter;
import com.example.BookRental.dto.UserDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.model.User;
import com.example.BookRental.repo.RoleRepo;
import com.example.BookRental.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserConverter userConverter;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email.toLowerCase());

        if (user.isEmpty()) {
            throw new CustomException("Email: " + email + " Not found");
        }

        UserDto userDto = userConverter.toDto(user.get());
        CustomUserDetail customUserDetail = new CustomUserDetail(userDto, roleRepo);

        return customUserDetail;
    }
}
