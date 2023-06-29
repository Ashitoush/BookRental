package com.example.BookRental.config;

import com.example.BookRental.exception.CustomException;
import com.example.BookRental.helper.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String userName = null;
        String jwtToken = null;

        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                userName = this.jwtHelper.getUserNameFromToken(jwtToken);
            } catch (ExpiredJwtException e) {
                throw new CustomException(e.getMessage());
            } catch (Exception e) {
                throw new CustomException(e.getMessage());
            }
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = this.customUserDetailService.loadUserByUsername(userName);
            if (userDetails !=null && this.jwtHelper.validateToken(jwtToken, userDetails.getUsername())) {

                List<GrantedAuthority> authorities = this.jwtHelper.getAuthoritiesClaimFromToken(jwtToken);

                Authentication authentication = this.jwtHelper.getAthentication(userName, authorities, request);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
