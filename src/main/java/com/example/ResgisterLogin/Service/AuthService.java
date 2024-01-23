package com.example.ResgisterLogin.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ResgisterLogin.Dto.EmployeeDTO;
import com.example.ResgisterLogin.Dto.RegisterDTO;
import com.example.ResgisterLogin.Entity.Employee;
import com.example.ResgisterLogin.Repo.EmployeeRepo;
import com.example.ResgisterLogin.Dto.ChangePasswordDTO;

@Service
public class AuthService {
	

	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SendOtpToMailService sendOtpToMailService;
	
     public String Register(RegisterDTO registerDTO) {
    	 String otp = registerDTO.getOtp();
    	 String storedOtp = sendOtpToMailService.getStoredOtp(registerDTO.getEmail());
    	 
    	 boolean isValidotp = otp.equals(storedOtp);
    	 if(isValidotp) {
    		 Employee employee = new Employee(
    	               registerDTO.getEmployeename(),
    	               registerDTO.getEmail(),
    	                this.passwordEncoder.encode( registerDTO.getPassword())
    	        );
    		 employeeRepo.save(employee);
    		 return "dawng ki thanh cong";
    	 }
    	 else return "Error";
    }
     
  // Trong AuthService.java
     public String changePassword(ChangePasswordDTO changePasswordDTO) {
         // Lấy thông tin từ DTO
         String email = changePasswordDTO.getEmail();
         String oldpassword = changePasswordDTO.getOldpassword();
         String newpassword = changePasswordDTO.getNewpassword();
         String otp = changePasswordDTO.getOtp(); // Thêm trường OTP

         // Kiểm tra tính hợp lệ của dữ liệu đầu vào
         if (email == null || oldpassword == null || newpassword == null || otp == null) {
             return "Dữ liệu không hợp lệ. Vui lòng cung cấp email, mật khẩu cũ, mật khẩu mới, và mã OTP.";
         }

         // Kiểm tra tính hợp lệ của mật khẩu cũ
         if (!isValidOldPassword(email, oldpassword)) {
             return "Mật khẩu cũ không hợp lệ. Thay đổi mật khẩu thất bại.";
         }

         // Kiểm tra tính hợp lệ của mã OTP
         if (!isValidOtp(email, otp)) {
             return "Mã OTP không hợp lệ. Thay đổi mật khẩu thất bại.";
         }

         // Thực hiện thay đổi mật khẩu
         if (updatePassword(email, newpassword)) {
             return "Thay đổi mật khẩu thành công";
         } else {
             return "Không thể thay đổi mật khẩu. Vui lòng thử lại sau.";
         }
     }
     
     public String forgotPassword(ChangePasswordDTO changePasswordDTO) {
         // Lấy thông tin từ DTO
         String email = changePasswordDTO.getEmail();
         String newpassword = changePasswordDTO.getNewpassword();
         String otp = changePasswordDTO.getOtp(); // Thêm trường OTP

         // Kiểm tra tính hợp lệ của dữ liệu đầu vào
         if (email == null || newpassword == null || otp == null) {
             return "Dữ liệu không hợp lệ. Vui lòng cung cấp email, mật khẩu mới, và mã OTP.";
         }

         // Kiểm tra tính hợp lệ của mã OTP
         if (!isValidOtp(email, otp)) {
             return "Mã OTP không hợp lệ. Thay đổi mật khẩu thất bại.";
         }

         // Thực hiện thay đổi mật khẩu
         if (updatePassword(email, newpassword)) {
             return "Thay đổi mật khẩu thành công";
         } else {
             return "Không thể thay đổi mật khẩu. Vui lòng thử lại sau.";
         }
     }

     // Thêm hàm kiểm tra tính hợp lệ của mã OTP
     private boolean isValidOtp(String email, String otp) {
         // Thực hiện logic kiểm tra mã OTP, có thể kiểm tra trong cơ sở dữ liệu
         String storedOtp = sendOtpToMailService.getStoredOtp(email);
         return otp.equals(storedOtp);
     }


     
     private boolean isValidOldPassword(String email, String oldPassword) {
         // Thực hiện logic kiểm tra mật khẩu cũ, có thể kiểm tra trong cơ sở dữ liệu
         // Đây chỉ là ví dụ đơn giản, bạn cần thay thế bằng logic thực tế

         // Lấy thông tin người dùng từ cơ sở dữ liệu dựa trên email
         Employee existingEmployee = employeeRepo.findByEmail(email);

         // Kiểm tra xem người dùng có tồn tại không
         if (existingEmployee != null) {
             // Lấy mật khẩu đã lưu trong cơ sở dữ liệu
             String storedPassword = existingEmployee.getPassword();

             // So sánh mật khẩu cũ đã nhập với mật khẩu đã lưu
             return passwordEncoder.matches(oldPassword, storedPassword);
         }

         // Người dùng không tồn tại hoặc có lỗi khác
         return false;
     }

     // Hàm cập nhật mật khẩu mới
     private boolean updatePassword(String email, String newPassword) {
         // Thực hiện logic cập nhật mật khẩu mới, có thể cập nhật trong cơ sở dữ liệu
         // Đây chỉ là ví dụ đơn giản, bạn cần thay thế bằng logic thực tế

         // Lấy thông tin người dùng từ cơ sở dữ liệu dựa trên email
         Employee existingEmployee = employeeRepo.findByEmail(email);

         // Kiểm tra xem người dùng có tồn tại không
         if (existingEmployee != null) {
             // Mã hóa mật khẩu mới trước khi lưu vào cơ sở dữ liệu
             String encodedPassword = passwordEncoder.encode(newPassword);

             // Cập nhật mật khẩu mới cho người dùng
             existingEmployee.setPassword(encodedPassword);
             employeeRepo.save(existingEmployee);

             return true; // Cập nhật thành công
         }

         // Người dùng không tồn tại hoặc có lỗi khác
         return false;
     }
     
  // Trong AuthService.java
     public String sendChangePasswordOtp(String email) {
         // Kiểm tra xem email có tồn tại trong hệ thống hay không
         if (isEmailValid(email)) {
             // Gửi mã OTP qua email
             String otp = sendOtpToMailService.sendOtpService(email);
             return "Mã OTP đã được gửi đến email của bạn.";
         } else {
             return "Email không tồn tại trong hệ thống.";
         }
     }
     
     private boolean isEmailValid(String email) {
         // Thực hiện logic kiểm tra tính hợp lệ của email, có thể gọi đến cơ sở dữ liệu
         // Hoặc sử dụng các điều kiện kiểm tra cụ thể
         // Ở đây là ví dụ đơn giản, chỉ kiểm tra xem email có chứa ký tự '@' không
         return email != null && email.contains("@");
     }

}
