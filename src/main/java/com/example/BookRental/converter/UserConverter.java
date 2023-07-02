package com.example.BookRental.converter;

import com.example.BookRental.config.CustomMessageSource;
import com.example.BookRental.dto.UserDto;
import com.example.BookRental.exception.CustomException;
import com.example.BookRental.model.Role;
import com.example.BookRental.model.User;
import com.example.BookRental.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserConverter {

    private final RoleRepo roleRepo;
    private final CustomMessageSource messageSource;

    public User toEntity(UserDto userDto) {
        User user = new User();

        String roleName = userDto.getRole().toUpperCase();

        Optional<Role> role = roleRepo.findByName(roleName);
        if (!role.isPresent()) {
//           throw new CustomException("Role: " + roleName + " not found");
           throw new CustomException(messageSource.get("role.not.found"));
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setId(user.getId());
        user.setPassword(userDto.getPassword());
        user.setRole(role.get());

        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole().getName());

        return userDto;
    }
}
