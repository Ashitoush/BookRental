package com.example.BookRental.helper;

import com.example.BookRental.exception.CustomException;
import com.example.BookRental.mapper.BookMapper;
import com.example.BookRental.model.Book;
import com.example.BookRental.repo.BookRepo;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PhotoHelper {

    private final BookRepo bookRepo;
//    private ServletContext servletContext;

    public String storePhoto(MultipartFile multipartFile) throws IOException {

        final String FOLDER_PATH = "C:\\Users\\admin\\Searches\\Desktop\\Projects\\BookRental\\BookRental\\src\\main\\resources\\static\\photo";



        File directoryFile = new File(FOLDER_PATH);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }

        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png")
                || ext.equalsIgnoreCase("jpeg")) {

            UUID uuid = UUID.randomUUID();

            String filePath = FOLDER_PATH + File.separator + uuid + "_" + multipartFile.getOriginalFilename();
            File photoToStore = new File(filePath);
            multipartFile.transferTo(photoToStore);

            return filePath;
        } else {
            throw new CustomException("Photo is not of valid format");
        }
    }

    public InputStream serveImage(Long bookId) throws FileNotFoundException {
        Book book = bookRepo.findById(bookId).get();
        String fullPath = book.getPhoto();
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }

    public String imageUrl(Book book) {
        Long bookId = book.getId();
        String imageUrl = "localhost:8080/image/" + bookId;
        return imageUrl;
    }

    public void deleteImage(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.delete(path);
        } catch (IOException ex) {
            throw new CustomException("Cannot find the image file to delete");
        }
    }

//    public String returnFileAsBase64(String filePath) throws IOException {
//        File file = new File(filePath);
//        try {
//            byte[] bytes = Files.readAllBytes(file.toPath());
//            String base64EncodedImage = "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
//            return base64EncodedImage;
//        } catch (IOException ex) {
//            throw new IOException("Cannot encode image into base64");
//        }
//    }
}
