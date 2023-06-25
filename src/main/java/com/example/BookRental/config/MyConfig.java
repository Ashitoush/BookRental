package com.example.BookRental.config;

import com.example.BookRental.exception.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class MyConfig{

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final BCryptPasswordEncoder passwordEncoder;

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder().username("admin@gmail.com").password(passwordEncoder().encode("admin123")).authorities("ADMIN").roles("ADMIN").build();
//        UserDetails userDetails1 = User.builder().username("librarian@gmail.com").password(passwordEncoder().encode("librarian123")).authorities("LIBRARIAN").roles("LIBRARIAN").build();
//
//        return new InMemoryUserDetailsManager(userDetails, userDetails1);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/user/**").permitAll()
                .anyRequest()
                .authenticated().and()
                .formLogin()
                .and()
                .httpBasic()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(this.customAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.customUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
