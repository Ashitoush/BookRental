package com.example.BookRental.controller;

import com.example.BookRental.helper.PhotoHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {
    private final PhotoHelper photoHelper;

    @GetMapping(value = "/{bookId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void serveImage(@PathVariable("bookId") Long bookId, HttpServletResponse response) throws IOException {
        InputStream inputStream = photoHelper.serveImage(bookId);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
}
