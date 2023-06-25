package com.example.BookRental.config;

import com.example.BookRental.dto.UserDto;
import com.example.BookRental.model.Role;
import com.example.BookRental.repo.RoleRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetail implements UserDetails {

    private UserDto userDto;
    private RoleRepo roleRepo;

    public CustomUserDetail(UserDto userDto, RoleRepo roleRepo) {
        super();
        this.userDto = userDto;
        this.roleRepo = roleRepo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = roleRepo.findByName(userDto.getRole()).get();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(role.getName()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
