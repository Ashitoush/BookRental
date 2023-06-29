package com.example.BookRental.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        List<String> errors = Arrays.asList("Unauthorized-Access Denied!!!");

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorId("Book Rental" + ThreadContext.get("requestId"))
                .path(request.getRequestURI())
                .errors(errors)
                .message("Unauthorized Access!!!")
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        OutputStream os = response.getOutputStream();
        ObjectMapper om = new ObjectMapper();
        om.writeValue(os, apiErrorResponse);
        os.flush();
    }
}
