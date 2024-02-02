package com.example.ResgisterLogin.Service.impl;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ResgisterLogin.Dto.EmployeeDTO;
import com.example.ResgisterLogin.Dto.LoginDTO;
import com.example.ResgisterLogin.Entity.Employee;
import com.example.ResgisterLogin.Service.EmployeeService;
import com.example.ResgisterLogin.response.LoginResponse;
import com.example.ResgisterLogin.Repo.EmployeeRepo;
import com.example.ResgisterLogin.Service.SendOtpToMailService; // Thêm import này

import java.util.Optional;

import org.springframework.util.StringUtils;

@Service
public class EmployeeIMPL implements EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SendOtpToMailService sendOtpToMailService;

	@Override
    public String addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee(
                employeeDTO.getEmployeeid(),
                employeeDTO.getEmployeename(),
                employeeDTO.getEmail(),
                this.passwordEncoder.encode(employeeDTO.getPassword())
        );

        employeeRepo.save(employee);
        return employee.getEmployeename();
    }

	@Override
	public LoginResponse loginEmployee(LoginDTO loginDTO) {
		Employee employee1 = employeeRepo.findByEmail(loginDTO.getEmail());
		if (employee1 != null) {
			String password = loginDTO.getPassword();
			String encodedPassword = employee1.getPassword();
			Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
			if (isPwdRight) {
				Optional<Employee> employee = employeeRepo.findOneByEmailAndPassword(loginDTO.getEmail(),
						encodedPassword);
				if (employee.isPresent()) {
					return new LoginResponse("Login Success", true);
				} else {
					return new LoginResponse("Login Failed", false);
				}
			} else {
				return new LoginResponse("Password Not Match", false);
			}
		} else {
			return new LoginResponse("Email not exists", false);
		}
	}

	@Override
	public String sendOtpToMailService(String email) {
		// Gửi mã OTP và lưu vào cơ sở dữ liệu
		String otp = sendOtpToMailService.sendOtpService(email);
		return otp;
	}

	@Override
	public String addEmployee(EmployeeDTO employeeDTO, String enteredOtp) {
	    // Kiểm tra rằng password không phải là null
	    if (employeeDTO.getPassword() == null) {
	        throw new IllegalArgumentException("Password cannot be null");
	    }

	    // Mã hóa mật khẩu
	    String encodedPassword = passwordEncoder.encode(employeeDTO.getPassword());

	    String storedOtp = sendOtpToMailService.getStoredOtp(employeeDTO.getEmail());
	    if (storedOtp != null && storedOtp.equals(enteredOtp)) {
	        Employee existingEmployee = employeeRepo.findByEmail(employeeDTO.getEmail());
	        if (existingEmployee == null) {
	            // Kiểm tra mật khẩu không phải là null hoặc trống
	            if (StringUtils.hasText(employeeDTO.getPassword())) {
	                Employee employee = new Employee(
	                        employeeDTO.getEmployeeid(),
	                        employeeDTO.getEmployeename(),
	                        employeeDTO.getEmail(),
	                        encodedPassword 
	                );

	                employeeRepo.save(employee);
	                return "Registration successful for employee: " + employee.getEmployeename();
	            } else {
	                return "Invalid Password. Registration Failed.";
	            }
	        } else {
	            return "Email already exists. Registration Failed.";
	        }
	    } else {
	        return "Invalid OTP. Registration Failed.";
	    }
	}

    @Override
    public String getStoredOtp(String email) {
        return sendOtpToMailService.getStoredOtp(email);
    }
    
    
    @Override
    public String editProfile(EmployeeDTO employeeDTO) {
        try {
            int employeeId = employeeDTO.getEmployeeid();
            Optional<Employee> existingEmployeeOptional = employeeRepo.findById(employeeId);
            if (existingEmployeeOptional.isPresent()) {
                Employee existingEmployee = existingEmployeeOptional.get();
                existingEmployee.setEmployeename(employeeDTO.getEmployeename());
                existingEmployee.setEmail(employeeDTO.getEmail());
                employeeRepo.save(existingEmployee);
                return "Profile updated successfully";
            } else {
                return "Employee not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during profile update";
        }
    }


}
