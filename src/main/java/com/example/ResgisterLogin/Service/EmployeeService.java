package com.example.ResgisterLogin.Service;



import com.example.ResgisterLogin.Dto.EmployeeDTO;
import com.example.ResgisterLogin.Dto.LoginDTO;
import com.example.ResgisterLogin.response.LoginResponse;


public interface EmployeeService {

	String addEmployee(EmployeeDTO employeeDTO);

	LoginResponse loginEmployee(LoginDTO loginDTO);

	String sendOtpToMailService(String email);

	String addEmployee(EmployeeDTO employeeDTO, String enteredOtp);
	
	String getStoredOtp(String email);

	String editProfile(EmployeeDTO employeeDTO);

	

	

}
