package com.example.ResgisterLogin.EmployeeController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ResgisterLogin.Service.SendOtpToMailService;
@CrossOrigin("*")
@RestController
public class EmailController {
	
	@Autowired
	private SendOtpToMailService sendOtpToMailService;
	
//	@GetMapping("/")
//	public String home() {
//		return " Welcom to send otp to email spring boot project!!!";
//	}
	
	@PostMapping("/sendOtp")
	public ResponseEntity<String> sendOtpToMail(@RequestParam("email") String email) {
	    try {
	        sendOtpToMailService.sendOtpService(email);
	        return ResponseEntity.ok("OTP sent successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending OTP");
	    }
	}
}
