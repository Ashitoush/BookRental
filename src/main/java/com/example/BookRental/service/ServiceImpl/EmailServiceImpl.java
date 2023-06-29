package com.example.BookRental.service.ServiceImpl;

import com.example.BookRental.exception.CustomException;
import com.example.BookRental.model.PasswordResetToken;
import com.example.BookRental.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public String sendEmail(PasswordResetToken passwordResetToken, String email) {
        MimeMessage message = javaMailSender.createMimeMessage();

        String htmlMessage = "<div style='text-align:center; font-size:1rem;border:2px solid green;'>"
                + "<h1 style='text-align:center;color:green;'>BOOK RENTAL</h1>"
                + "<hr/>"
                + "<p style='margin-top:5rem;'>Here is a link to reset your password</p>"
                + "<p style='margin-bottom:5rem;'>This link will be active for only 1 hour</p>"
                + "<a href='http://localhost:8080/user/resetPassword?token=" + passwordResetToken.getToken()
                + "' style='text-decoration:none;color:white; background-color:green;padding:10px;border-radius:10px;'>Reset Password</a>"
                + "<p style='margin-top:5rem;'>If you cannot click the above link then you can copy and paste the below link to reset your password from other device</p>"
                + "<a href='http://localhost:8080/user/resetPassword?token='" + passwordResetToken.getToken()
                + "style='text-decoration:none;color:black;'>http://localhost:8080/user/resetPassword?token=" + passwordResetToken.getToken()
                + "</a>"
                + "<p style='margin-top:4rem;'>If you did not try to reset your password, then do not worry, you can just ignore this email</p>"
                + "</div>";

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setContent(htmlMessage, "text/html");
            helper.setTo(email);
            helper.setSubject("Request for Password Reset");
            try {
                javaMailSender.send(message);
            } catch (Exception e) {
                throw new CustomException(e.getMessage());
            }
        } catch (MessagingException e) {
            throw new CustomException(e.getMessage());
        }

        return "Mail was sent Successfully";
    }
}
