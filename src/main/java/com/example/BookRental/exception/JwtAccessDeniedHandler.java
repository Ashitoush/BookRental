package com.example.BookRental.exception;

import com.example.BookRental.config.CustomMessageSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final CustomMessageSource messageSource;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        List<String> errors = Arrays.asList(messageSource.get("un.authorized.denied"));

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorId(messageSource.get("book.rental"))
                .path(request.getRequestURI())
                .errors(errors)
                .message(messageSource.get("access.denied"))
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        OutputStream os = response.getOutputStream();
        ObjectMapper om = new ObjectMapper();
        om.writeValue(os, apiErrorResponse);
        os.flush();
    }
}
