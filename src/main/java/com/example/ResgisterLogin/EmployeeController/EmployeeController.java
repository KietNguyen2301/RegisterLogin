package com.example.ResgisterLogin.EmployeeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.example.ResgisterLogin.Dto.ChangePasswordDTO;
import com.example.ResgisterLogin.Dto.EmployeeDTO;
import com.example.ResgisterLogin.Dto.LoginDTO;
import com.example.ResgisterLogin.Dto.RegisterDTO;
import com.example.ResgisterLogin.Service.AuthService;
import com.example.ResgisterLogin.Service.EmployeeService;
import com.example.ResgisterLogin.response.LoginResponse;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("api/v1/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AuthService authService;
	
	 @PostMapping("/register")
	 
	    public String register( @RequestBody RegisterDTO registerDTO) {
	        if (registerDTO != null) {
	            String result = authService.Register(registerDTO);
	            return result;
	        } else {
	            return "Can not null";
	        }
	    }
	
	@PostMapping(path = "/login")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginDTO loginDTO) {
        try {
            LoginResponse loginResponse = employeeService.loginEmployee(loginDTO);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login");
        }
	}
	
	// EmployeeController.java
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
	    try {
	        // Gọi đến authService để xử lý logic thay đổi mật khẩu
	        String result = authService.changePassword(changePasswordDTO);
	        // Trả về một phản hồi thành công với thông điệp
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        // Nếu có lỗi, in ra stack trace và trả về phản hồi lỗi máy chủ nội bộ
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during password change");
	    }
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
	    try {
	        // Gọi đến authService để xử lý logic thay đổi mật khẩu
	        String result = authService.forgotPassword(changePasswordDTO);
	        // Trả về một phản hồi thành công với thông điệp
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        // Nếu có lỗi, in ra stack trace và trả về phản hồi lỗi máy chủ nội bộ
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during password change");
	    }
	}
	
	// Trong EmployeeController.java
	@PostMapping("/send-otp-for-password-reset")
	 public ResponseEntity<?> sendOtpForPasswordReset(@RequestParam String email) {
        try {
            String result = authService.sendChangePasswordOtp(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending OTP");
        }
    }
	
	
	@PostMapping("/edit-profile")
    public ResponseEntity<?> editProfile(@RequestBody EmployeeDTO employeeDTO) {
        try {
            String result = employeeService.editProfile(employeeDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during profile edit");
        }
    }

}
