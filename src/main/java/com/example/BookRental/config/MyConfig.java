package com.example.BookRental.config;

import com.example.BookRental.Constant.AppConstant;
import com.example.BookRental.exception.JwtAccessDeniedHandler;
import com.example.BookRental.exception.JwtAuthenticationEntryPoint;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MyConfig{

    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final PasswordEncoder passwordEncoder;

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder().username("admin@gmail.com").password(passwordEncoder().encode("admin123")).authorities("ADMIN").roles("ADMIN").build();
//        UserDetails userDetails1 = User.builder().username("librarian@gmail.com").password(passwordEncoder().encode("librarian123")).authorities("LIBRARIAN").roles("LIBRARIAN").build();
//
//        return new InMemoryUserDetailsManager(userDetails, userDetails1);
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .cors(cors -> cors.disable())
                .authorizeHttpRequests()
                .requestMatchers(AppConstant.PUBLIC_URL).permitAll()
                .anyRequest()
                .authenticated()
//                .and()
//                .httpBasic()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.customUserDetailService);
        authenticationProvider.setPasswordEncoder(this.passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
