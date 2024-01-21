package com.example.ResgisterLogin.Service;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendOtpToMailService {
    @Autowired
    private JavaMailSender javaMailSender;

    // Map để lưu trữ OTP liên kết với địa chỉ email
    private Map<String, String> otpStorage = new HashMap<>();

    public String sendOtpService(String email) {
        String otp = generateOtp(email);

        try {
            sendOtpToMail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi OTP.");
        }

        return otp;
    }

    private String generateOtp(String email) {
        SecureRandom random = new SecureRandom();
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);

        // Lưu trữ OTP cho địa chỉ email
        otpStorage.put(email, otp);

        return otp;
    }

    private void sendOtpToMail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Mã OTP của bạn là:");
        mimeMessageHelper.setText(otp);
        javaMailSender.send(mimeMessage);
    }
    
    public String getStoredOtp(String email) {
        // Lấy mã OTP đã lưu cho địa chỉ email
        return otpStorage.get(email);
    }
}
